import { EntityRepository } from '@mikro-orm/core';
import { Recipe } from '../recipes/entities/recipe.entity';
import { Injectable } from '@nestjs/common';

@Injectable()
export class RecipesRepository extends EntityRepository<Recipe> {
  public async findById(id: number) {
    return await this.findOne(id);
  }
}
