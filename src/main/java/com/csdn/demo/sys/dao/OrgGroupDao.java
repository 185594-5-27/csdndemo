package com.csdn.demo.sys.dao;


import com.csdn.demo.common.base.dao.GenericDao;
import com.csdn.demo.sys.entity.OrgGroup;
import com.csdn.demo.sys.entity.QueryOrgGroup;

/**
 *@author linzf
 **/
public interface OrgGroupDao extends GenericDao<OrgGroup, QueryOrgGroup> {

    /**
     * 功能描述：根据父节点来查询最大的节点的值
     * @param parentNode
     * @return
     */
    String getMaxOrgGroup(String parentNode);

    /**
     * 功能描述：根据菜单节点NODE来查询节点数据
     * @param node
     * @return
     */
    OrgGroup findByNode(String node);
}