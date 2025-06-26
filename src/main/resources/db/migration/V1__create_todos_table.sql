CREATE TABLE todos (
                       id SERIAL PRIMARY KEY,
                       description VARCHAR(255) NOT NULL,
                       status VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL
);