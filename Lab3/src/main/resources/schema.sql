CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS book_editions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS copies (
    id BIGSERIAL PRIMARY KEY,
    book_edition_id BIGINT NOT NULL,
    inventory_number VARCHAR(100) NOT NULL UNIQUE,
    condition VARCHAR(50) NOT NULL,
    CONSTRAINT fk_copies_book_edition FOREIGN KEY (book_edition_id)
        REFERENCES book_editions (id) ON DELETE RESTRICT,
    CONSTRAINT chk_copy_condition CHECK (condition IN ('NEW', 'EXCELLENT', 'GOOD', 'FAIR', 'DAMAGED'))
);

CREATE TABLE IF NOT EXISTS loans (
    id BIGSERIAL PRIMARY KEY,
    copy_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    loan_date TIMESTAMP NOT NULL,
    return_date TIMESTAMP,
    CONSTRAINT fk_loans_copy FOREIGN KEY (copy_id)
        REFERENCES copies (id) ON DELETE RESTRICT,
    CONSTRAINT fk_loans_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE RESTRICT,
    CONSTRAINT chk_loan_dates CHECK (return_date IS NULL OR return_date >= loan_date)
);

CREATE TABLE IF NOT EXISTS waiting_requests (
    id BIGSERIAL PRIMARY KEY,
    book_edition_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    request_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_waiting_book_edition FOREIGN KEY (book_edition_id)
        REFERENCES book_editions (id) ON DELETE CASCADE,
    CONSTRAINT fk_waiting_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_request_status CHECK (status IN ('PENDING', 'FULFILLED', 'CANCELLED'))
);
