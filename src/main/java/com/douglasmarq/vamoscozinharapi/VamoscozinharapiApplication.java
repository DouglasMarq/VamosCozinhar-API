package com.douglasmarq.vamoscozinharapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableAspectJAutoProxy
public class VamoscozinharapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VamoscozinharapiApplication.class, args);
    }
}
