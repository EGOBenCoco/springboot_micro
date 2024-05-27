INSERT INTO Post (account_Id, name, content, author, category, created_at, updated_at)
VALUES (2, 'Test post', 'Test content', 'Kirill', 'IT', NOW(), NOW());




INSERT INTO post_photos_url (post_id, photo_url)
VALUES
    (1, 'http://example.com/photo1.jpg'),
    (1, 'http://example.com/photo2.jpg'),
    (1, 'http://example.com/photo3.jpg');