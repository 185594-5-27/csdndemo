package com.csdn.demo.common.util.excel;
/**
 * 自定义导出excel异常
 * @author zxn
 * */
public class ExcelException extends RuntimeException{
	private static final long serialVersionUID = -3766733786200320204L;
	
	public ExcelException(){}
	
	public ExcelException(String msg){
		super(msg);
	}
}
