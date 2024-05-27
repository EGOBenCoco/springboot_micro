CREATE TABLE Post (
    id SERIAL PRIMARY KEY,
    account_Id INT ,
    name VARCHAR ,
    content TEXT ,
    author VARCHAR ,
    category VARCHAR ,
    created_at TIMESTAMP ,
    updated_at TIMESTAMP
);

CREATE TABLE post_photos_url (
    post_id INT REFERENCES Post(id) ON DELETE CASCADE,
    photo_url VARCHAR
);
