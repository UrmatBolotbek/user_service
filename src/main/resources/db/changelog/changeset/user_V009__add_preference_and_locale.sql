ALTER TABLE users
    ADD COLUMN if not exists preference text,
    ADD COLUMN if not exists locale text;