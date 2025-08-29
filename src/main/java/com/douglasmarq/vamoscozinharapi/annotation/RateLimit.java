package com.douglasmarq.vamoscozinharapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int requests() default 10;

    int windowSeconds() default 60;

    String message() default "Rate limit exceeded. Please try again later.";
}
