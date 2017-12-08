package com.csdn.demo.sys.service;

import com.csdn.demo.common.base.dao.GenericDao;
import com.csdn.demo.common.base.service.GenericService;
import com.csdn.demo.sys.dao.UserDao;
import com.csdn.demo.sys.entity.QueryUser;
import com.csdn.demo.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
* 类描述：
* @auther linzf
* @create 2017/12/8 0008 
*/
@Service("userService")
@Transactional(rollbackFor={IllegalArgumentException.class})
public class UserService extends GenericService<User, QueryUser> {


    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private UserDao userDao;

    @Override
    protected GenericDao<User, QueryUser> getDao() {
        return userDao;
    }
}
