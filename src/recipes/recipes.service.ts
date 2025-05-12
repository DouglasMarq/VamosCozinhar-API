import { Injectable, Logger } from '@nestjs/common';
import { CreateRecipeDto } from './dto/create-recipe.dto';
import { UpdateRecipeDto } from './dto/update-recipe.dto';
import { InjectRepository } from '@mikro-orm/nestjs';
import { Recipe } from './entities/recipe.entity';
import { RecipesRepository } from '../repository/recipes-repository';

@Injectable()
export class RecipesService {
  private readonly logger = new Logger(RecipesService.name);

  constructor(
    @InjectRepository(Recipe)
    private readonly repository: RecipesRepository,
  ) {}

  async create(createRecipeDto: CreateRecipeDto) {
    if (await this.repository.findOne({ name: createRecipeDto.name }))
      throw new Error('Recipe already exists');

    const recipe = this.repository.create(createRecipeDto);

    await this.repository.insert(recipe);

    return recipe;
  }

  findAll() {
    return this.repository.findAll();
  }

  async findOne(id: number) {
    this.logger.log(`Searching for recipe with id ${id}`);
    return await this.repository.findById(id);
  }

  update(id: number, updateRecipeDto: UpdateRecipeDto) {
    return `This action updates a #${id} recipe`;
  }

  remove(id: number) {
    return `This action removes a #${id} recipe`;
  }
}
