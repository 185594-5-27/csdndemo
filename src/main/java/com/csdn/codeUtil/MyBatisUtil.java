package com.csdn.codeUtil;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis配置文件生成功能
 * */
public class MyBatisUtil {
	/**
	 * 从表结构中去生成mybatis配置
	 * @param table
	 * @param namespace
	 * @param beanName
	 * @param queryModelName
	 * @return
	 */
	public static String genMapperConfig(TableModel table,String namespace, String beanName, String queryModelName){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
		sb.append("<mapper namespace=\""+namespace+"\">\n");
		//生成resultMap
		String resultMap = beanName.split("\\.")[(beanName.split("\\.").length-1)]+"Map";
		sb.append(genResultMap(beanName, resultMap, table));
		//生成Get SQL
		sb.append(genGETSQL(beanName, resultMap, table));
		//生成插入SQL
		String dbType = "oracle";
		String driver = JdbcUtil.configs.getProperty("spring.datasource.driverClassName");
		if(driver.toLowerCase().indexOf("mysql")>0){
			dbType = "";
		}
		if(dbType.equalsIgnoreCase("oracle")){
			sb.append(genSAVESQLOfORCL(beanName, table));
		}
		else{
			sb.append(genSAVESQL(beanName, table));
		}
		//生成修改SQL
		sb.append(genUPDATESQL(beanName, table));
		//生成删除SQL
		sb.append(genDELETESQL(beanName, table));
		if(StringUtils.isNotEmpty(queryModelName)){
			//生成分页查询
			if(dbType.equalsIgnoreCase("oracle")){
				sb.append(genFINDBYPAGESQLOfORCL(queryModelName, resultMap, table));
			}
			else{
				sb.append(genFINDBYPAGESQL(queryModelName, resultMap, table));
			}
			//统计
			sb.append(genCOUNTSQL(queryModelName, table));
			//查询
			sb.append(genQUERYSQL(queryModelName, resultMap, table));
		}
		sb.append("</mapper>");
		return sb.toString();
	}
	
	private static String genResultMap(String beanName, String resultMap, TableModel table){
		List<ColumnModel> columnModelList = table.getColumns();
		List<ColumnModel> primaryKeys = table.getPrimaryKeyColumns();
		StringBuffer sb = new StringBuffer();
		sb.append("\t<resultMap type=\""+beanName+"\" id=\""+resultMap+"\">\n");
		if(primaryKeys.size()==1){
			ColumnModel primaryKey = primaryKeys.get(0);
			sb.append("\t\t<id property=\""+primaryKey.getFieldName()+"\" column=\""+primaryKey.getColumnName()+"\"/>\n");
			for(ColumnModel cm : columnModelList){
				if(!cm.isPrimaryKey())
					sb.append("\t\t<result property=\""+cm.getFieldName()+"\" column=\""+cm.getColumnName()+"\"/>\n");
			}
		}
		else
			for(ColumnModel cm : columnModelList){
				sb.append("\t\t<result property=\""+cm.getFieldName()+"\" column=\""+cm.getColumnName()+"\"/>\n");
			}
		sb.append("\t</resultMap>\n\n");
		return sb.toString();
	}
	
	private static String genGETSQL(String beanName, String resultMap, TableModel table){
		List<ColumnModel> columnModelList = table.getColumns();
		List<ColumnModel> primaryKeys = table.getPrimaryKeyColumns();
		StringBuffer sb = new StringBuffer();
		sb.append("\t<!--根据主键获取对象-->\n");
		sb.append("\t<select id=\"get\" parameterType=\""+beanName+"\" resultMap=\""+resultMap+"\">\n\t\tSELECT ");
		sb.append(getSelectFields(columnModelList));
		sb.append(" FROM "+table.getTableName() +" \n\t\tWHERE ");
		for(int i=0; i<primaryKeys.size(); i++){
			ColumnModel pk = primaryKeys.get(i);
			sb.append(pk.getColumnName()+"=#{"+pk.getFieldName()+"}");
			if(i<primaryKeys.size()-1){
				sb.append(" and ");
			}
		}
		sb.append("\n\t</select>\n\n");
		return sb.toString();
	}
	
	private static String genSAVESQL(String beanName, TableModel table){
		List<ColumnModel> columnModelList = table.getColumns();
		List<ColumnModel> primaryKeys = table.getPrimaryKeyColumns();
		StringBuffer sb = new StringBuffer();
		sb.append("\t<!--保存-->\n");
		if(primaryKeys.size()==1 && primaryKeys.get(0).isAutoIncrement()){
			//自增主键，并返回主键值
			sb.append("\t<insert id=\"save\" parameterType=\""+beanName+"\" useGeneratedKeys=\"true\" keyProperty=\""+primaryKeys.get(0).getFieldName()+"\">\n");
			sb.append("\t\tINSERT INTO "+table.getTableName()+"(");
			for(ColumnModel cm : columnModelList){
				if(!cm.isPrimaryKey()|| !cm.isAutoIncrement()){
					sb.append(cm.getColumnName());
					sb.append(",");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")\n\t\tVALUES(");
			for(ColumnModel cm : columnModelList){
				if(!cm.isPrimaryKey()|| !cm.isAutoIncrement()){
					sb.append("#{"+cm.getFieldName()+"}");
					sb.append(",");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")\n");
			sb.append("\t</insert>\n\n");
		}
		else{
			sb.append("\t<insert id=\"save\" parameterType=\""+beanName+"\">\n");
			sb.append("\t\tINSERT INTO "+table.getTableName()+"(");
			for(ColumnModel cm : columnModelList){
				if(!cm.isAutoIncrement()){
					sb.append(cm.getColumnName());
					sb.append(",");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")\n\t\tVALUES(");
			for(ColumnModel cm : columnModelList){
				if(!cm.isAutoIncrement()){
					sb.append("#{"+cm.getFieldName()+"}");
					sb.append(",");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")\n");
			sb.append("\t</insert>\n\n");
		}
		return sb.toString();
	}
	
	private static String genSAVESQLOfORCL(String beanName, TableModel table){
		List<ColumnModel> columnModelList = table.getColumns();
		List<ColumnModel> primaryKeys = table.getPrimaryKeyColumns();
		StringBuffer sb = new StringBuffer();
		sb.append("\t<!--保存-->\n");
		sb.append("\t<insert id=\"save\" parameterType=\""+beanName+"\">\n");
		if(primaryKeys.size()==1 && primaryKeys.get(0).isAutoIncrement()){
			String sequence = null;
			if(table.getTableName().toLowerCase().indexOf("t_")==0){
				sequence = table.getTableName().toLowerCase().replace("t_", "s_");
			}
			else{
				sequence = "s_"+table.getTableName().toLowerCase();
			}
			//自增主键，并返回主键值
			sb.append("\t\t<selectKey keyProperty=\""+primaryKeys.get(0).getFieldName()+"\" resultType=\"int\" order=\"BEFORE\">select "+sequence+".nextval from dual</selectKey>\n");
		}
		sb.append("\t\tINSERT INTO "+table.getTableName()+"(");
		for(ColumnModel cm : columnModelList){
			sb.append(cm.getColumnName());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")\n\t\tVALUES(");
		for(ColumnModel cm : columnModelList){
			sb.append("#{"+cm.getFieldName()+"}");
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")\n");
		sb.append("\t</insert>\n\n");
		return sb.toString();
	}
	
	private static String genUPDATESQL(String beanName, TableModel table){
		List<ColumnModel> columnModelList = table.getColumns();
		List<ColumnModel> primaryKeys = table.getPrimaryKeyColumns();
		StringBuffer sb = new StringBuffer();
		sb.append("\t<!--修改-->\n");
		sb.append("\t<update id=\"update\" parameterType=\""+beanName+"\">\n");
		sb.append("\t\tUPDATE "+table.getTableName()+" SET ");
		for(ColumnModel cm : columnModelList){
			if(!cm.isPrimaryKey()){
				sb.append(cm.getColumnName()+"=#{"+cm.getFieldName()+"}");
				sb.append(",");
			}
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("\n\t\tWHERE ");
		for(int i=0; i<primaryKeys.size(); i++){
			ColumnModel pk = primaryKeys.get(i);
			sb.append(pk.getColumnName()+"=#{"+pk.getFieldName()+"}");
			if(i<primaryKeys.size()-1){
				sb.append(" and ");
			}
		}
		sb.append("\n\t</update>\n\n");
		return sb.toString();
	}
	
	private static String genDELETESQL(String beanName, TableModel table){
		List<ColumnModel> primaryKeys = table.getPrimaryKeyColumns();
		StringBuffer sb = new StringBuffer();
		sb.append("\t<!--删除-->\n");
		sb.append("\t<delete id=\"delete\" parameterType=\""+beanName+"\">\n");
		sb.append("\t\t DELETE FROM "+table.getTableName()+" WHERE ");
		for(int i=0; i<primaryKeys.size(); i++){
			ColumnModel pk = primaryKeys.get(i);
			sb.append(pk.getColumnName()+"=#{"+pk.getFieldName()+"}");
			if(i<primaryKeys.size()-1){
				sb.append(" and ");
			}
		}
		sb.append("\n\t</delete>\n\n");
		return sb.toString();
	}
	
	private static String genFINDBYPAGESQL(String queryModelName, String resultMap, TableModel table){
		List<ColumnModel> columnModelList = table.getColumns();
		StringBuffer sb = new StringBuffer();
		sb.append("\t<!--分页查询-->\n");
		sb.append("\t<select id=\"findByPage\" parameterType=\""+queryModelName+"\" resultMap=\""+resultMap+"\">");
		sb.append("\n\t\tSELECT ");
		for(ColumnModel cm : columnModelList){
			sb.append(cm.getColumnName());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(" FROM "+table.getTableName());
		sb.append("\n\t\tWHERE 1=1");
		for(ColumnModel cm :getQueryFields(table)){
			sb.append("\n\t\t<if test=\""+cm.getFieldName()+"!=null and "+cm.getFieldName()+"!='' \"  >");
			sb.append("\n\t\tAND "+cm.getColumnName()+"=#{"+cm.getFieldName()+"}");
			sb.append("\n\t\t</if>");
		}
		sb.append("\n\t\t<if test=\"sort!= null\">\n\t\torder by ${sort} ${order}\n\t\t</if>");
		sb.append("\n\t\tlimit #{offset},#{limit}");
		sb.append("\n\t</select>\n\n");
		return sb.toString();
	}
	
	private static String genFINDBYPAGESQLOfORCL(String queryModelName, String resultMap, TableModel table){
		List<ColumnModel> columnModelList = table.getColumns();
		StringBuffer sb = new StringBuffer();
		sb.append("\t<!--分页查询-->\n");
		sb.append("\t<select id=\"findByPage\" parameterType=\""+queryModelName+"\" resultMap=\""+resultMap+"\">");
		sb.append("\n\t\tSELECT * FROM (SELECT t.*, ROWNUM rn FROM (");
		sb.append("\n\t\tSELECT ");
		for(ColumnModel cm : columnModelList){
			sb.append(cm.getColumnName());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(" FROM "+table.getTableName());
		sb.append("\n\t\tWHERE 1=1");
		for(ColumnModel cm :getQueryFields(table)){
			sb.append("\n\t\t<if test=\""+cm.getFieldName()+"!=null and "+cm.getFieldName()+"!='' \">");
			sb.append("\n\t\tAND "+cm.getColumnName()+"=#{"+cm.getFieldName()+"}");
			sb.append("\n\t\t</if>");
		}
		sb.append("\n\t\t<if test=\"sort!= null\">\n\t\torder by ${sort} ${order}\n\t\t</if>");
		sb.append("\n\t\t)t) WHERE rn>#{offset} AND (#{offset}+#{limit})>=rn");
		sb.append("\n\t</select>\n\n");
		return sb.toString();
	}
	
	private static String genCOUNTSQL(String queryModelName, TableModel table){
		StringBuffer sb = new StringBuffer();
		sb.append("\t<!--统计-->\n");
		sb.append("\t<select id=\"count\" parameterType=\""+queryModelName+"\" resultType=\"int\">");
		sb.append("\n\t\tSELECT count(*) FROM "+table.getTableName());
		sb.append("\n\t\tWHERE 1=1");
		for(ColumnModel cm : getQueryFields(table)){
			sb.append("\n\t\t<if test=\""+cm.getFieldName()+"!=null and "+cm.getFieldName()+"!='' \">");
			sb.append("\n\t\tAND "+cm.getColumnName()+"=#{"+cm.getFieldName()+"}");
			sb.append("\n\t\t</if>");
		}
		sb.append("\n\t</select>\n\n");
		return sb.toString();
	}
	
	private static String genQUERYSQL(String queryModelName, String resultMap, TableModel table){
		List<ColumnModel> columnModelList = table.getColumns();
		StringBuffer sb = new StringBuffer();
		sb.append("\t<!--查询-->\n");
		sb.append("\t<select id=\"query\" parameterType=\""+queryModelName+"\" resultMap=\""+resultMap+"\">\n");
		sb.append("\t\tSELECT ");
		for(ColumnModel cm : columnModelList){
			sb.append(cm.getColumnName());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(" FROM "+table.getTableName());
		sb.append("\n\t\tWHERE 1=1");
		for(ColumnModel cm : getQueryFields(table)){
			sb.append("\n\t\t<if test=\""+cm.getFieldName()+"!=null and "+cm.getFieldName()+"!='' \">");
			sb.append("\n\t\tAND "+cm.getColumnName()+"=#{"+cm.getFieldName()+"}");
			sb.append("\n\t\t</if>");
		}
		sb.append("\n\t\t<if test=\"sort!= null\">\n\t\torder by ${sort} ${order}\n\t\t</if>");
		sb.append("\n\t</select>\n");
		return sb.toString();
	}
	
	public static String getSelectFields(List<ColumnModel> columnModelList){
		StringBuffer sb = new StringBuffer();
		for(ColumnModel cm : columnModelList){
			sb.append(cm.getColumnName());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	/**
	 * 获取查询字段
	 * */
	public static List<ColumnModel> getQueryFields(TableModel table){
		if(table.getPrimaryKeyColumns().size()==1 && table.getPrimaryKeyColumns().get(0).isAutoIncrement()){
			List<ColumnModel> columns = new ArrayList<ColumnModel>();
			for(ColumnModel cm : table.getColumns()){
				if(!cm.isPrimaryKey())
					columns.add(cm);
			}
			return columns;
		}
		return table.getColumns();
	}
}
