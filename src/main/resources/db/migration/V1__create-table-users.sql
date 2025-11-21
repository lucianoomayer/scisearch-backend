CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(70) NOT NULL,
    email VARCHAR(45) UNIQUE NOT NULL,
    password VARCHAR(300) NOT NULL
);

CREATE TABLE favorites(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(45) NOT NULL,
    article_id VARCHAR(100) NOT NULL,
    title VARCHAR(250) NOT NULL,
    url VARCHAR(250) NOT NULL,
    publication_date VARCHAR(15) NOT NULL,
    source VARCHAR(40) NOT NULL,
    favorite_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(email),
    CONSTRAINT unique_favorite UNIQUE (user_id, article_id)
);
