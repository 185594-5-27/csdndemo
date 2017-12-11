package com.csdn.demo.sys.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *@author linzf
 **/
public class UserRole {
	private long id;
	private String name;
	private String roleName;

	private List<Tree> treeList;
    // 临时采访菜单数集合的数据
	private String treeArray;

	public String getTreeArray() {
		return treeArray;
	}

	public void setTreeArray(String treeArray) {
		this.treeArray = treeArray;
	}

	public List<Tree> getTreeList() {
		return treeList;
	}

	public void setTreeList(List<Tree> treeList) {
		this.treeList = treeList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void packagingTrees(String treeArray){
		Tree tree = null;
		List<Tree> trees = new ArrayList<>();
		for(String id:treeArray.split(",")){
			if(!id.isEmpty()){
				tree = new Tree(Long.parseLong(id));
				trees.add(tree);
			}
		}
		this.setTreeList(trees);
	}

}
