package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.entity.QMember;


@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }


}
