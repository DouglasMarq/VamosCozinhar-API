import {
  Embeddable,
  Embedded,
  Entity,
  EntityRepositoryType,
  Index,
  PrimaryKey,
  Property,
} from '@mikro-orm/core';
import { RecipesRepository } from '../../repository/recipes-repository';

@Entity({ tableName: 'recipes', repository: () => RecipesRepository })
export class Recipe {
  [EntityRepositoryType]?: RecipesRepository;

  @PrimaryKey({ autoincrement: true, unique: true, nullable: false })
  id!: number;

  @Property({ nullable: false, index: true })
  name!: string;

  @Property({ nullable: false, type: 'text' })
  description!: string;

  @Property({ nullable: false, index: true })
  difficulty!: number;

  @Property({ nullable: true })
  image?: string;

  @Embedded(() => RecipeIngredients, { array: true, nullable: false })
  ingredients: RecipeIngredients[] = [];

  @Property({ nullable: false })
  prepare!: string[];

  @Property({ nullable: false, onCreate: () => new Date() })
  createdAt?: Date = new Date();

  @Property({ nullable: false, onUpdate: () => new Date() })
  updatedAt?: Date = new Date();
}

@Embeddable()
class RecipeIngredients {
  @Property({ nullable: false, index: true })
  ingredient!: string;

  @Property({ nullable: false })
  description!: string;

  @Property({ nullable: false })
  image!: string;
}
