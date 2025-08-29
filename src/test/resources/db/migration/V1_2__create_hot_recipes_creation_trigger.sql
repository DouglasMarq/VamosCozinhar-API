-- Create a trigger function to automatically insert into hot_recipes
CREATE OR REPLACE FUNCTION create_hot_recipe_entry()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO hot_recipes (recipe_id, likes, views)
    VALUES (NEW.id, 0, 0);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
CREATE TRIGGER trigger_create_hot_recipe
    AFTER INSERT ON recipes
    FOR EACH ROW
    EXECUTE FUNCTION create_hot_recipe_entry();