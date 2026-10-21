INSERT INTO users (id, name, email, phone) VALUES
    (1, 'Иван Иванов', 'ivan@example.com', '+79991112233'),
    (2, 'Анна Смирнова', 'anna@example.com', '+79992223344'),
    (3, 'Петр Васильев', 'petr@example.com', '+79993334455')
ON CONFLICT (id) DO NOTHING;

INSERT INTO book_editions (id, title, author, isbn) VALUES
    (1, 'Преступление и наказание', 'Федор Достоевский', '978-5-17-090001-1'),
    (2, 'Мастер и Маргарита', 'Михаил Булгаков', '978-5-17-090002-2'),
    (3, 'Чистый код', 'Роберт Мартин', '978-5-4461-0960-9')
ON CONFLICT (id) DO NOTHING;

INSERT INTO copies (id, book_edition_id, inventory_number, condition) VALUES
    (1, 1, 'INV-DOST-001', 'GOOD'),
    (2, 1, 'INV-DOST-002', 'NEW'),
    (3, 2, 'INV-BULG-001', 'EXCELLENT'),
    (4, 3, 'INV-CLEAN-001', 'GOOD')
ON CONFLICT (id) DO NOTHING;

INSERT INTO loans (id, copy_id, user_id, loan_date, return_date) VALUES
    (1, 1, 1, '2026-09-01 10:00:00', '2026-09-15 14:30:00'),
    (2, 3, 2, '2026-10-01 12:00:00', NULL)
ON CONFLICT (id) DO NOTHING;

INSERT INTO waiting_requests (id, book_edition_id, user_id, request_date, status) VALUES
    (1, 3, 1, '2026-10-02 15:00:00', 'PENDING')
ON CONFLICT (id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('users', 'id'), COALESCE(MAX(id), 1)) FROM users;
SELECT setval(pg_get_serial_sequence('book_editions', 'id'), COALESCE(MAX(id), 1)) FROM book_editions;
SELECT setval(pg_get_serial_sequence('copies', 'id'), COALESCE(MAX(id), 1)) FROM copies;
SELECT setval(pg_get_serial_sequence('loans', 'id'), COALESCE(MAX(id), 1)) FROM loans;
SELECT setval(pg_get_serial_sequence('waiting_requests', 'id'), COALESCE(MAX(id), 1)) FROM waiting_requests;
