CREATE TABLE subscriber (
    id SERIAL PRIMARY KEY,
    subscriber_email VARCHAR(255) NOT NULL
);

CREATE TABLE subscriber_profile_ids (
    subscriber_id INT NOT NULL,
    profile_id INT NOT NULL,
    FOREIGN KEY (subscriber_id) REFERENCES subscriber(id)
);
