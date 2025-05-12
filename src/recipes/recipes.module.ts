import { Module } from '@nestjs/common';
import { RecipesService } from './recipes.service';
import { RecipesController } from './recipes.controller';
import { MikroOrmModule } from '@mikro-orm/nestjs';
import { Recipe } from './entities/recipe.entity';

@Module({
  imports: [MikroOrmModule.forFeature([Recipe])],
  controllers: [RecipesController],
  providers: [RecipesService],
})
export class RecipesModule {}
