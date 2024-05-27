CREATE TABLE profile (
    id SERIAL PRIMARY KEY,
    nickname VARCHAR(255),
    bio TEXT,
    auth_id VARCHAR(255),
    count_subscriber NUMERIC DEFAULT 0
);

CREATE TABLE link (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    value VARCHAR(255),
    profile_id INT REFERENCES profile(id) ON DELETE CASCADE
);