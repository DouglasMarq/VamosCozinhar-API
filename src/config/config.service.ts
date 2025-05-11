import { Injectable } from '@nestjs/common';
import * as dotenvx from '@dotenvx/dotenvx';

@Injectable()
export class ConfigService {
  private readonly envConfig: { [key: string]: string | undefined };

  constructor() {
    dotenvx.config();
    this.envConfig = process.env;
  }

  get(key: string): string {
    const value = this.envConfig[key];
    if (value === undefined) {
      throw new Error(`Configuration key "${key}" is not defined`);
    }
    return value;
  }

  get port(): number {
    return parseInt(this.get('PORT') || '3001', 10);
  }
}
