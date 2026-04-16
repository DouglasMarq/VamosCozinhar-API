CREATE TABLE IF NOT EXISTS recipe_likes (
    user_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id, recipe_id),
    CONSTRAINT fk_recipe_likes_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_recipe_likes_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_recipe_likes_recipe ON recipe_likes(recipe_id);
CREATE INDEX IF NOT EXISTS idx_recipe_likes_user ON recipe_likes(user_id);

CREATE OR REPLACE FUNCTION sync_hot_recipe_likes()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE hot_recipes SET likes = COALESCE(likes, 0) + 1 WHERE recipe_id = NEW.recipe_id;
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE hot_recipes SET likes = GREATEST(COALESCE(likes, 0) - 1, 0) WHERE recipe_id = OLD.recipe_id;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_sync_hot_recipe_likes
    AFTER INSERT OR DELETE ON recipe_likes
    FOR EACH ROW EXECUTE FUNCTION sync_hot_recipe_likes();
