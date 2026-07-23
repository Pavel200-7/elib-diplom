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
    RETURN to_tsvector('russian',
       COALESCE(reader_book_number, '') || ' ' ||
       COALESCE(email, '') || ' ' ||
       COALESCE(phone, '') || ' ' ||
       COALESCE(first_name, '') || ' ' ||
       COALESCE(last_name, '') || ' ' ||
       COALESCE(patronymic, '') || ' ' ||
       COALESCE(birth_date::text, '') || ' ' ||
       COALESCE(status, '')
   );
END;
$$ LANGUAGE plpgsql IMMUTABLE;

CREATE INDEX user_full_text_search_idx ON users
USING GIN (user_search_vector(
    reader_book_number,
    email,
    phone,
    first_name,
    last_name,
    patronymic,
    birth_date,
    status
));