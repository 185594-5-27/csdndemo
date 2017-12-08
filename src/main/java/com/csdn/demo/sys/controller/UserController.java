package com.csdn.demo.sys.controller;

import com.csdn.demo.common.base.controller.GenericController;
import com.csdn.demo.common.base.service.GenericService;
import com.csdn.demo.sys.entity.QueryUser;
import com.csdn.demo.sys.entity.User;
import com.csdn.demo.sys.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/*
* 类描述：用户维护controller
* @auther linzf
* @create 2017/9/7 0007
*/
@Controller
@RequestMapping("/user")
public class UserController extends GenericController<User,QueryUser> {

    @Inject
    private UserService userService;

    @Override
    protected GenericService<User, QueryUser> getService() {
        return userService;
    }
}
