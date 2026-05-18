export const dictionaryConfigs = {
    countries: {
        apiPath: '/countries',
        title: 'Страны',
        fields: [
            { key: 'name', label: 'Название', type: 'text', required: true, maxLength: 100 }
        ]
    },
    genres: {
        apiPath: '/genres',
        title: 'Жанры',
        fields: [
            { key: 'name', label: 'Название', type: 'text', required: true, maxLength: 100 }
        ]
    },
    authors: {
        apiPath: '/authors',
        title: 'Авторы',
        fields: [
            { key: 'name', label: 'ФИО автора', type: 'text', required: true, maxLength: 255 }
        ]
    },
    publishings: {
        apiPath: '/publishings',
        title: 'Издательства',
        fields: [
            { key: 'name', label: 'Название', type: 'text', required: true, maxLength: 255 },
            { key: 'description', label: 'Описание', type: 'textarea', required: false }
        ]
    },
    languages: {
        apiPath: '/languages',
        title: 'Языки',
        fields: [
            { key: 'name', label: 'Название', type: 'text', required: true, maxLength: 100 }
        ]
    },
    literatureGroups: {
        apiPath: '/literature-groups',
        title: 'Группы литературы',
        fields: [
            { key: 'name', label: 'Название', type: 'text', required: true, maxLength: 100 }
        ]
    },
    rooms: {
        apiPath: '/rooms',
        title: 'Помещения',
        fields: [
            { key: 'name', label: 'Название', type: 'text', required: true, maxLength: 255 }
        ]
    },
    holders: {
        apiPath: '/holders',
        title: 'Места хранения',
        fields: [
            { key: 'name', label: 'Название', type: 'text', required: true, maxLength: 255 },
            { 
                key: 'type', 
                label: 'Тип', 
                type: 'select', 
                required: true,
                options: [
                    { value: 'SHELF', label: 'Стеллаж' },
                    { value: 'CABINET', label: 'Шкаф' },
                    { value: 'DEPOSITORY', label: 'Книгохранилище' }
                ]
            },
            { 
                key: 'roomId', 
                label: 'Помещение', 
                type: 'asyncSelect', 
                required: true,
                apiPath: '/rooms',
                valueProp: 'id',
                labelProp: 'name'
            }
        ]
    }
}