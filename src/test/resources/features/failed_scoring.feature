# language: ru

Функционал: Скоринг-оценка компании. Неуспешный результат

  Предыстория:
    Дано все файлы этого сценария расположены в директории 'scenario/failed/'
    Если сервис обратится на '/context-api/api/v1.0/context/save' с телом 'context-api/context_save_request.json' он получит код 200

  Сценарий: Неуспешный результат скоринг-оценки компании

    Когда сервис получает запрос на запуск процесса скоринг-оценки с методом 'POST' по адресу '/api/v1.0/scoring/start' и json телом 'start_scoring_request.json', а в ответ отдает код 200
    И был сделан post запрос на '/context-api/api/v1.0/context/save' с телом 'context-api/context_save_request.json' 1 раз
    Тогда процесс должен быть завершен
    И переменная 'is_scoring_failed' должна быть равна 'true'
    И состояние контекста должно быть идентично json представлению 'context.json'