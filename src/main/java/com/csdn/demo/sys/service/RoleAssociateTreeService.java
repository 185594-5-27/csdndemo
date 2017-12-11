package com.csdn.demo.sys.service;

import com.csdn.demo.common.base.dao.GenericDao;
import com.csdn.demo.common.base.service.GenericService;
import com.csdn.demo.sys.dao.RoleAssociateTreeDao;
import com.csdn.demo.sys.entity.QueryRoleAssociateTree;
import com.csdn.demo.sys.entity.RoleAssociateTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *@author linzf
 **/
@Service("roleAssociateTreeService")
@Transactional(rollbackFor={IllegalArgumentException.class})
public class RoleAssociateTreeService extends GenericService<RoleAssociateTree, QueryRoleAssociateTree> {
	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private RoleAssociateTreeDao roleAssociateTreeDao;
	@Override
	protected GenericDao<RoleAssociateTree, QueryRoleAssociateTree> getDao() {
		return roleAssociateTreeDao;
	}
}