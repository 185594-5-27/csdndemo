package com.csdn.demo.sys.controller;

import com.csdn.demo.sys.entity.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/*
* 类描述：用户维护controller
* @auther linzf
* @create 2017/9/7 0007
*/
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String,Object> addUser(User user){
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("success",true);
        result.put("msg","增加数据成功！");
        return result;
    }

}
