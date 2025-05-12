import { NestFactory } from '@nestjs/core';
import {
  FastifyAdapter,
  NestFastifyApplication,
} from '@nestjs/platform-fastify';
import { AppModule } from './app.module';
import { MikroORM } from '@mikro-orm/core';
require('dotenv').config();

async function bootstrap() {
  const app = await NestFactory.create<NestFastifyApplication>(
    AppModule,
    new FastifyAdapter(),
  );

  app.enableShutdownHooks();

  app.enableCors({
    origin: 'http://localhost:3000',
    credentials: true,
  });

  // For development, update schema automatically
  if (process.env.NODE_ENV === 'dev') {
    const orm = app.get(MikroORM);
    const generator = orm.getSchemaGenerator();
    await generator.updateSchema();
  }

  await app.listen(process.env.PORT ?? 3001, '0.0.0.0');
}
bootstrap();
