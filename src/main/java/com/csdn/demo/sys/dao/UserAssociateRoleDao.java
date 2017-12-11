package com.csdn.demo.sys.dao;


import com.csdn.demo.common.base.dao.GenericDao;
import com.csdn.demo.sys.entity.QueryUserAssociateRole;
import com.csdn.demo.sys.entity.User;
import com.csdn.demo.sys.entity.UserAssociateRole;

/**
 *@author linzf
 **/
public interface UserAssociateRoleDao extends GenericDao<UserAssociateRole, QueryUserAssociateRole> {

    /**
     * 功能描述：根据用户的ID来删除用户的权限数据
     * @param user
     * @return
     */
    int removeUserRole(User user);
}