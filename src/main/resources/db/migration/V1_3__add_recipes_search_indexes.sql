CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS idx_recipes_name_trgm
    ON recipes USING gin (name gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_recipes_description_trgm
    ON recipes USING gin (description gin_trgm_ops);
