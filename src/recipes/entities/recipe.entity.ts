import { Entity, PrimaryKey } from '@mikro-orm/core';

@Entity()
export class Recipe {
  @PrimaryKey()
  id: number;
}
