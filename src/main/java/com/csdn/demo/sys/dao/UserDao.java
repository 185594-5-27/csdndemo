package com.csdn.demo.sys.dao;



import com.csdn.demo.common.base.dao.GenericDao;
import com.csdn.demo.sys.entity.QueryUser;
import com.csdn.demo.sys.entity.User;

import java.util.List;

/**
 *@author linzf
 **/
public interface UserDao extends GenericDao<User, QueryUser> {

    /**
     * 功能描述：根据账号来获取用户信息
     * @param login
     * @return
     */
    User findByLogin(String login);

	
}