package com.csdn.codeUtil;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class JdbcUtil {
	private static String properties_file_uri = null;
	public static Properties configs;

	public static void setPropertiesURL(String uri){
		properties_file_uri = uri;
	}

	/**
	 * 获取连接
	 * @return
	 */
	public static Connection getConnection(){
		try {
			configs = new Properties();
			if(StringUtils.isEmpty(properties_file_uri)){
				properties_file_uri = "/genericCoder.properties";
			}
			String path = GenCodeUtil.class.getResource("/").getPath();
			InputStream in = new FileInputStream(new File(path+properties_file_uri));
			configs.load(in);
			Class.forName(configs.getProperty("spring.datasource.driverClassName"));
			Properties properties = new Properties();
			properties.put("user", configs.getProperty("spring.datasource.username"));
			properties.put("password", configs.getProperty("spring.datasource.password"));
			properties.put("remarksReporting","true");//想要获取数据库结构中的注释，这个值是重点
			return DriverManager.getConnection(configs.getProperty("spring.datasource.url"), properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取表结构
	 * @param tableName
	 * @return
	 */
	public static TableModel getTableStructure(String tableName){
		List<ColumnModel> columnModelList = new ArrayList<ColumnModel>();
		List<ColumnModel> primaryKeyColumns = new ArrayList<ColumnModel>();
		Set<String> imports = new HashSet<String>();
		try {
			//TODO 表相关
			//ResultSet tableSet = metaData.getTables(null, "%",tableName,new String[]{"TABLE"}); 
			//TODO 字段相关
			DatabaseMetaData dbMeta = getConnection().getMetaData();
			List<String> primaryKeys = getPrimaryKeys(dbMeta, tableName);
			ResultSet columnSet = dbMeta.getColumns(null,"%",tableName,"%");
			ColumnModel columnModel = null;
			while(columnSet.next()){
				columnModel = new ColumnModel();
				columnModel.setColumnName(columnSet.getString("COLUMN_NAME"));
				columnModel.setColumnSize(columnSet.getInt("COLUMN_SIZE"));
				columnModel.setDataType(columnSet.getString("DATA_TYPE"));
				columnModel.setRemarks(columnSet.getString("REMARKS"));
				columnModel.setTypeName(columnSet.getString("TYPE_NAME"));
				columnModel.setAutoIncrement(columnSet.getBoolean("IS_AUTOINCREMENT"));
				columnModel.setPrimaryKey(justicPrimaryKey(columnModel.getColumnName(), primaryKeys));
				//String columnClassName = ColumnTypeEnum.getColumnTypeEnumByDBType(columnModel.getTypeName());
				String columnClassName = sqlType2JavaType(columnModel.getTypeName());
				String imp = getImportByJavaType(columnClassName);
				if(StringUtils.isNotEmpty(imp))
					imports.add(imp);
				String fieldName = getFieldName(columnModel.getColumnName());
				String fieldType = null;
				try{
					if(StringUtils.isNotEmpty(columnClassName))
						fieldType = Class.forName(columnClassName).getSimpleName();
					else
						throw new RuntimeException();
				}
				catch(ClassNotFoundException e){
					fieldType = columnClassName;
				}
				columnModel.setFieldName(fieldName);
				columnModel.setColumnClassName(columnClassName);
				columnModel.setFieldType(fieldType);
    			columnModelList.add(columnModel);
    			if(columnModel.isPrimaryKey())
    				primaryKeyColumns.add(columnModel);
    			//System.out.println(columnModel.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TableModel table = new TableModel();
		table.setColumns(columnModelList);
		table.setPrimaryKeyColumns(primaryKeyColumns);
		table.setImports(imports);
		table.setTableName(tableName);
		return table;
	}
	/**
	 * 将数据库字段转换成bean属性
	 * @param columnName
	 * @return
	 */
	private static String getFieldName(String columnName) {
		char[]  columnCharArr = columnName.toCharArray();
		StringBuffer sb = new StringBuffer();
		int ad = -1;
		for (int i = 0; i < columnCharArr.length; i++) {
			  char cur = columnCharArr[i];
			  if(cur=='_'){
				  ad = i;
			  }else{
				  if((ad+1)==i&&ad!=-1){
					  sb.append(Character.toUpperCase(cur));
				  }else{
					  sb.append(cur);
				  }
				  ad=-1;
			  }
		}
		return sb.toString();
	}
	/**
	 * 获取表主键
	 * @throws SQLException 
	 * */
	private static List<String> getPrimaryKeys(DatabaseMetaData dbMeta, String tableName) throws SQLException{
		ResultSet pkRSet = dbMeta.getPrimaryKeys(null, null, tableName);
		List<String> primaryKyes = new ArrayList<String>();
		while(pkRSet.next()){
			primaryKyes.add(pkRSet.getObject("COLUMN_NAME").toString());
		}
		return primaryKyes;
	}
	/**
	 * 判断列是否为主键列
	 * */
	private static boolean justicPrimaryKey(String columnName, List<String> primaryKyes){
		for(String key : primaryKyes)
			if(key.equals(columnName))
				return true;
		return false;
	}
	 /**
	  * 功能：获得列的数据类型
	  * @param sqlType
	  * @return
	  */
	private static String sqlType2JavaType(String sqlType) {
		if(sqlType.equalsIgnoreCase("bit")){
			return "boolean";
		}else if(sqlType.equalsIgnoreCase("tinyint")){
			return "byte";
		}else if(sqlType.equalsIgnoreCase("smallint")){
			return "short";
		}else if(sqlType.equalsIgnoreCase("int")){
			return "int";
		}else if(sqlType.equalsIgnoreCase("bigint")){
			return "long";
		}else if(sqlType.equalsIgnoreCase("float")){
			return "float";
		}else if(sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric") 
		|| sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money") 
		|| sqlType.equalsIgnoreCase("smallmoney")){
			return "double";
		}else if(sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") 
		|| sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar") 
		|| sqlType.equalsIgnoreCase("text")){
			return "String";
		}else if(sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("date")){
			return "Date";
		}else if(sqlType.equalsIgnoreCase("image")){
			return "Blod";
		}else if(sqlType.equalsIgnoreCase("timestamp")){
			return "Timestamp";
		}
		return "String";
	}
	/**
	 * 根据数据类型获取需要引入的类
	 * */
	private static String getImportByJavaType(String javaType){
		switch(javaType){
		case "Date": return "java.util.Date";
		case "Timestamp": return "java.sql.Timestamp";
		case "Blod": return "java.sql.Blod";
		}
		return null;
	}
}
