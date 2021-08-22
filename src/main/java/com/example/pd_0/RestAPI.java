package com.example.pd_0;

import DB.User;
import DB.UserDao;
import DB.UserDaoImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestAPI {
    @GetMapping("/hallo")// 相当于RequestMapping +Method.GET 属于简化了
    public String returnHello(){
        return "hello";
    }

    @PostMapping("/user_login")
    public String login(User user){
        String result="FAILED";
        UserDao userDao = new UserDaoImpl();
        try {
            if(userDao.verify(user.getId(),user.getPassword())){
                result="OK";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;

    }

}
