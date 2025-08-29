CREATE TABLE IF NOT EXISTS hot_recipes (
    recipe_id BIGINT PRIMARY KEY,
    likes INTEGER,
    views INTEGER,
    CONSTRAINT fk_hot_recipes_recipe_id 
        FOREIGN KEY (recipe_id) REFERENCES recipes(id)
        ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_hot_recipes_likes ON hot_recipes(likes);
CREATE INDEX IF NOT EXISTS idx_hot_recipes_views ON hot_recipes(views);