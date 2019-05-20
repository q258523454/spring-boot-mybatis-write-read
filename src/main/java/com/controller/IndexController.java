package com.controller;

import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
public class IndexController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

}