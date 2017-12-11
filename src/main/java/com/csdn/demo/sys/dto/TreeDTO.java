package com.csdn.demo.sys.dto;

import java.io.Serializable;

/*
* 类描述：
* @auther linzf
* @create 2017/9/19 0019 
*/
public class TreeDTO implements Serializable {


    private static final long serialVersionUID = -1594183211347796443L;

    private Long id;
    // 父节点ID
    private Long pId;
    // 菜单节点名字
    private String name;
    // 菜单编码
    private String code;
    // 菜单样式
    private String icon;
    // 菜单状态（0：禁用；1：启用）
    private String state;
    // 菜单顺序
    private Long treeOrder;
    // 菜单节点是否选中的状态(true:选中；false:未选中)
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getTreeOrder() {
        return treeOrder;
    }

    public void setTreeOrder(Long treeOrder) {
        this.treeOrder = treeOrder;
    }
}
