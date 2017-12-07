package com.csdn.demo.sys.dao;



import com.csdn.demo.sys.entity.User;

import java.util.List;

/**
 *@author linzf
 **/
public interface UserDao {

    /**
     * 功能描述：根据账号来获取用户信息
     * @param login
     * @return
     */
    User findByLogin(String login);

	
}