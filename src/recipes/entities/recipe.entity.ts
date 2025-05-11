import { Entity, PrimaryKey } from '@mikro-orm/core';

@Entity()
export class Recipe {
  @PrimaryKey({ autoincrement: true, unique: true, nullable: false })
  id: number;
}
