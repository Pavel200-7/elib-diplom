package com.example.elib.test.controller.service.impl.helper;

import com.example.elib.test.controller.service.Generator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailGenerator implements Generator<String> {

    private static final String[] DOMAINS = {
            "gmail.com", "yandex.ru", "mail.ru", "bk.ru", "inbox.ru",
            "list.ru", "yahoo.com", "outlook.com", "hotmail.com", "protonmail.com",
            "icloud.com", "aol.com", "zoho.com", "gmx.com", "tut.by",
            "rambler.ru", "mail.ua", "ukr.net", "meta.ua", "i.ua"
    };

    private static final String[] LOGINS = {
            "user", "admin", "alex", "max", "dima", "sergey", "andrey", "ivan", "mike",
            "reader", "bookworm", "librarian", "student", "teacher", "professor",
            "cat", "dog", "bird", "fish", "wolf", "fox", "bear", "lion", "tiger"
    };

    private final Random random = new Random();
    private int counter = 0;

    @Override
    public String Generate() {
        String login = LOGINS[random.nextInt(LOGINS.length)];
        int number = ++counter + random.nextInt(10000);
        String domain = DOMAINS[random.nextInt(DOMAINS.length)];
        return login + number + "@" + domain;
    }
}