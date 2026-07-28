-- для создания индекса нужна иммутабельная конструкция, а параметр 'russian' в
-- to_tsvector может изменяться с обновлениями. Поэтому появилась необходимость
-- создания иммутабельной функции для создания индекса.
CREATE OR REPLACE FUNCTION user_search_vector(
    reader_book_number TEXT,
    email TEXT,
    phone TEXT,
    first_name TEXT,
    last_name TEXT,
    patronymic TEXT,
    birth_date DATE,
    status TEXT
)
RETURNS tsvector AS $$
BEGIN
    RETURN
       setweight(to_tsvector('russian', coalesce(reader_book_number, '')), 'D') ||
       setweight(to_tsvector('russian', coalesce(email, '')), 'D') ||
       setweight(to_tsvector('russian', coalesce(phone, '')), 'D') ||
       setweight(to_tsvector('russian', coalesce(first_name, '')), 'A') ||
       setweight(to_tsvector('russian', coalesce(last_name, '')), 'A') ||
       setweight(to_tsvector('russian', coalesce(patronymic, '')), 'D') ||
       setweight(to_tsvector('russian', coalesce(birth_date::text, '')), 'D') ||
       setweight(to_tsvector('russian', coalesce(status, '')), 'D');
END;
$$ LANGUAGE plpgsql IMMUTABLE;

ALTER TABLE users
ADD COLUMN tsv tsvector GENERATED ALWAYS AS (
    user_search_vector(
        reader_book_number,
        email,
        phone,
        first_name,
        last_name,
        patronymic,
        birth_date,
        status
    )
) STORED;

CREATE INDEX user_full_search_vector_idx ON users USING GIN(tsv);

-- Функция генерации конвертации строки поискового запроса в форму tsquery
CREATE OR REPLACE FUNCTION make_users_search_query(search_text TEXT)
RETURNS tsquery AS $$
BEGIN
    IF search_text IS NULL OR TRIM(search_text) = '' THEN
        RETURN NULL;
    END IF;

    RETURN to_tsquery('russian',
        (SELECT string_agg(TRIM(word) || ':*', ' & ') -- дополняет каждое найденное слово суфиксом :* (поиск совпадений начала строки) и амперсандом (&) - необходимым фрагментом синтаксиса tsquery
         FROM regexp_split_to_table(TRIM(search_text), '\s+') AS word -- разбивка строки на слова
         WHERE LENGTH(word) > 2) -- удаляет слова меньше 3 букв
    );
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Табличная функция для извлечения пользователей по полнотекстовому поиску
CREATE OR REPLACE FUNCTION get_users_with_full_text_search(search_text TEXT)
RETURNS TABLE (
    id UUID,
    reader_book_number VARCHAR(7),
    email VARCHAR(254),
    phone VARCHAR(20),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    patronymic VARCHAR(255),
    birth_date DATE,
    status VARCHAR(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    rank REAL
) AS $$
DECLARE
    query tsquery := make_users_search_query(search_text);
BEGIN
    IF query IS NULL THEN
        RETURN QUERY
            SELECT
                u.id,
                u.reader_book_number,
                u.email,
                u.phone,
                u.first_name,
                u.last_name,
                u.patronymic,
                u.birth_date,
                u.status,
                u.created_at,
                u.updated_at,
                0.0::REAL AS rank
            FROM users u
            ORDER BY u.last_name, u.first_name;
        RETURN;
    END IF;

    RETURN QUERY
        SELECT
            u.id,
            u.reader_book_number,
            u.email,
            u.phone,
            u.first_name,
            u.last_name,
            u.patronymic,
            u.birth_date,
            u.status,
            u.created_at,
            u.updated_at,
            ts_rank(u.tsv, query, 32)::REAL AS rank
        FROM users u
        WHERE u.tsv @@ query
        ORDER BY rank DESC;
END;
$$ LANGUAGE plpgsql;