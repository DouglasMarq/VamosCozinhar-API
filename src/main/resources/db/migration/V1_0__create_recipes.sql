CREATE TABLE IF NOT EXISTS recipes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    difficulty INTEGER,
    -- TODO - Needs refactoring/check
    image VARCHAR(500),
    recipe_ingredients JSONB,
    prepare JSONB,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);