package com.example.elib.test.controller;

import com.example.elib.test.controller.service.UserDataFiller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {

    private final UserDataFiller userDataFiller;

    @GetMapping
    public String test() {
        return "Работает!";
    }

    @GetMapping("/fill-users/{count}")
    public String fillUser(@PathVariable int count) {
        userDataFiller.fillUser(count);
        return "Заполнил";
    }

}
