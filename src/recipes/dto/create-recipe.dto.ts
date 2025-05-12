import {
  IsArray,
  IsNotEmpty,
  IsNumber,
  IsString,
  ValidateNested,
} from 'class-validator';
import { Type } from 'class-transformer';

export class CreateRecipeDto {
  @IsString()
  @IsNotEmpty()
  name!: string;

  @IsString()
  @IsNotEmpty()
  description!: string;

  @IsNumber()
  @IsNotEmpty()
  difficulty!: number;

  @IsString()
  image?: string;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => RecipeIngredientDto)
  ingredients!: RecipeIngredientDto[];

  @IsArray()
  @IsString({ each: true })
  prepare!: string[];
}

export class RecipeIngredientDto {
  @IsString()
  @IsNotEmpty()
  ingredient!: string;

  @IsString()
  @IsNotEmpty()
  description!: string;

  @IsString()
  @IsNotEmpty()
  image!: string;
}
