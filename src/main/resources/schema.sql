CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    CONSTRAINT pk_user_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGSERIAL NOT NULL,
    description VARCHAR(255) NOT NULL,
    created TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items (
    id BIGSERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    available BOOLEAN DEFAULT FALSE,
    owner_id BIGINT NOT NULL,
    request_id BIGINT,
    CONSTRAINT pk_item_id PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (request_id) REFERENCES requests (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL NOT NULL,
    item_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT pk_booking_id PRIMARY KEY (id),
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL NOT NULL,
    text VARCHAR(255) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created TIMESTAMP NOT NULL,
    CONSTRAINT pk_comment_id PRIMARY KEY (id),
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);