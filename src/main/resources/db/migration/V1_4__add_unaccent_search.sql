CREATE EXTENSION IF NOT EXISTS unaccent;

CREATE OR REPLACE FUNCTION immutable_unaccent(text)
RETURNS text AS $$
  SELECT public.unaccent($1)
$$ LANGUAGE SQL IMMUTABLE PARALLEL SAFE STRICT;

DROP INDEX IF EXISTS idx_recipes_name_trgm;
DROP INDEX IF EXISTS idx_recipes_description_trgm;

CREATE INDEX IF NOT EXISTS idx_recipes_name_unaccent_trgm
    ON recipes USING gin (lower(immutable_unaccent(name)) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_recipes_description_unaccent_trgm
    ON recipes USING gin (lower(immutable_unaccent(description)) gin_trgm_ops);
