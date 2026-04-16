package com.douglasmarq.vamoscozinharapi.exception;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageResolver {

    private final MessageSource messageSource;

    public MessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String resolve(String key, Object... args) {
        return resolve(key, LocaleContextHolder.getLocale(), args);
    }

    public String resolve(String key, Locale locale, Object... args) {
        return messageSource.getMessage(key, args, key, locale);
    }
}
