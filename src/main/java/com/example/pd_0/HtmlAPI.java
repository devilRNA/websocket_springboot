package com.example.pd_0;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlAPI {
    @GetMapping("/")
    public String welcome(){
        return "index";
    }


    @GetMapping("/self")
    public String index(){
        return "socket_self";
    }

    @GetMapping("/many")
    public String to_many(){
        return "socket_1vn";
    }



}
