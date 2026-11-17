ALTER TABLE users
    ADD CONSTRAINT ck_users_email CHECK (email ~ '^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$');
