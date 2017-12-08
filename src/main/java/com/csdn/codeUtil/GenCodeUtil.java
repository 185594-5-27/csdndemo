package com.csdn.codeUtil;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* 类描述：
* @auther linzf
* @create 2017/12/8 0008 
*/
public class GenCodeUtil {

    /**
     * 从表结构中去生成javabean
     * @param author
     * @param table
     * @param beanName
     * @param packagePath
     * @return
     */
    private static String genJavaBeanFromTableStructure(String author, TableModel table,String beanName, String packagePath){
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotEmpty(packagePath)){
            sb.append("package "+packagePath+";\n");
        }
        for(String imp : table.getImports()){
            sb.append("import "+imp+";\n");
        }
        sb.append("\n");
        sb.append("/**\n *@author "+author+"\n **/\n");
        List<ColumnModel> columnModelList = table.getColumns();
        try {
            sb.append("public class "+toFirstCharUpCase(beanName)+" {\r\n");
            for (ColumnModel columnModel : columnModelList) {
                if(StringUtils.isNotBlank(columnModel.getRemarks())){
                    sb.append("	//"+columnModel.getRemarks()+" \r\n");
                }
                sb.append("	private "+columnModel.getFieldType()+" "+columnModel.getFieldName()+";\r\n");
            }
            sb.append("\r\n");
            //get set
            for (ColumnModel columnModel : columnModelList) {
                sb.append(
                        "\tpublic "+columnModel.getColumnClassName()+" get"+toFirstCharUpCase((String) columnModel.getFieldName())+"() {\r\n" +
                                "\t\treturn "+columnModel.getFieldName()+";\r\n" +
                                "\t}\r\n" +
                                "\r\n" +
                                "\tpublic void set"+toFirstCharUpCase((String) columnModel.getFieldName())+"("+columnModel.getColumnClassName()+" "+columnModel.getFieldName()+") {\r\n" +
                                "\t\tthis."+columnModel.getFieldName()+" = "+columnModel.getFieldName()+";\r\n" +
                                "\t}\r\n\r\n");
            }
            sb.append("}\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 从表结构中去生成查询实体类
     * @param author
     * @param table
     * @param beanName
     * @param extendsBasePackage
     * @param packagePath
     * @return
     */
    private static String genQueryModelFromTableStructure(String author, TableModel table,String beanName, String extendsBasePackage, String packagePath){
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotEmpty(packagePath)){
            sb.append("package "+packagePath+";\n\n");
        }
        sb.append("import "+extendsBasePackage+".entity.QueryBase;\n\n");
        sb.append("/**\n *@author "+author+"\n **/\n");
        try {
            sb.append("public class "+toFirstCharUpCase(beanName)+" extends QueryBase {\r\n");
            List<ColumnModel> columns = getQueryFields(table);
            for (ColumnModel columnModel : columns) {
                if(StringUtils.isNotBlank(columnModel.getRemarks())){
                    sb.append("	//"+columnModel.getRemarks()+" \r\n");
                }
                String qFieldType = getQueryModelFieldType(columnModel.getFieldType());
                sb.append("	private "+qFieldType+" "+columnModel.getFieldName()+";\r\n");
            }
            sb.append("\r\n");
            //get set
            for (ColumnModel columnModel : columns) {
                String qFieldType = getQueryModelFieldType(columnModel.getFieldType());
                sb.append(
                        "\tpublic "+qFieldType+" get"+toFirstCharUpCase((String) columnModel.getFieldName())+"() {\r\n" +
                                "\t\treturn "+columnModel.getFieldName()+";\r\n" +
                                "\t}\r\n" +
                                "\r\n" +
                                "\tpublic void set"+toFirstCharUpCase((String) columnModel.getFieldName())+"("+qFieldType+" "+columnModel.getFieldName()+") {\r\n" +
                                "\t\tthis."+columnModel.getFieldName()+" = "+columnModel.getFieldName()+";\r\n" +
                                "\t}\r\n\r\n");
            }
            sb.append("}\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    /**
     * 生成Dao
     * */
    private static String genDao(String author, String packagePath, String beanName, String queryModelName,String extendsBasePackage){
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotEmpty(packagePath)){
            sb.append("package "+packagePath+";\n\n");
        }
        String businessPackage = packagePath.substring(0, packagePath.lastIndexOf("."));
        String basePackage = businessPackage.substring(0, businessPackage.lastIndexOf("."));
        sb.append("import "+extendsBasePackage+".dao.GenericDao;\n\n");
        sb.append("import "+businessPackage+".entity."+beanName+";\n");
        sb.append("import "+businessPackage+".entity."+queryModelName+";\n\n");
        sb.append("/**\n *@author "+author+"\n **/\n");
        sb.append("public interface "+beanName+"Dao extends GenericDao<"+beanName+", "+queryModelName+"> {\r\n");
        sb.append("\n\t\n}");
        return sb.toString();
    }
    /**
     * 生成Service
     * */
    private static String genService(String author, String packagePath, String beanName, String queryModelName,String extendsBasePackage){
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotEmpty(packagePath)){
            sb.append("package "+packagePath+";\n\n");
        }
        String businessPackage = packagePath.substring(0, packagePath.lastIndexOf("."));
        String basePackage = businessPackage.substring(0, businessPackage.lastIndexOf("."));
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.stereotype.Service;\n\n");
        sb.append("import org.springframework.transaction.annotation.Transactional;\n\n");
        sb.append("import "+extendsBasePackage+".service.GenericService;\n");
        sb.append("import "+basePackage+".common.base.dao.GenericDao;\n\n");
        sb.append("import "+businessPackage+".entity."+beanName+";\n");
        sb.append("import "+businessPackage+".entity."+queryModelName+";\n");
        sb.append("import "+businessPackage+".dao."+beanName+"Dao;\n\n");
        sb.append("/**\n *@author "+author+"\n **/\n");
        sb.append("@Service(\""+toFirstCharLowerCase(beanName)+"Service\")\n");
        sb.append("@Transactional(rollbackFor={IllegalArgumentException.class})\n");
        sb.append("public class "+beanName+"Service extends GenericService<"+beanName+", "+queryModelName+"> {\r\n");
        sb.append("\t@Autowired\n");
        sb.append("\t@SuppressWarnings(\"SpringJavaAutowiringInspection\")\n");
        sb.append("\tprivate "+beanName+"Dao "+toFirstCharLowerCase(beanName)+"Dao;\n");
        sb.append("\t@Override\n\tprotected GenericDao<"+beanName+", "+queryModelName+"> getDao() {\n");
        sb.append("\t\treturn "+toFirstCharLowerCase(beanName)+"Dao;\n");
        sb.append("\t}\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 生成controller
     * */
    private static String genController(String author, String packagePath, String beanName, String queryModelName,String extendsBasePackage){
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotEmpty(packagePath)){
            sb.append("package "+packagePath+";\n\n");
        }
        String businessPackage = packagePath.substring(0, packagePath.lastIndexOf("."));
        String basePackage = businessPackage.substring(0, businessPackage.lastIndexOf("."));
        sb.append("import javax.inject.Inject;\n\n");
        sb.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
        sb.append("import org.springframework.stereotype.Controller;\n");
        sb.append("import "+extendsBasePackage+".controller.GenericController;\n");
        sb.append("import "+basePackage+".common.base.service.GenericService;\n\n");
        sb.append("import "+businessPackage+".entity."+beanName+";\n");
        sb.append("import "+businessPackage+".entity."+queryModelName+";\n");
        sb.append("import "+businessPackage+".service."+beanName+"Service;\n\n");
        sb.append("/**\n *@author "+author+"\n **/\n");
        sb.append("@Controller\n");
        sb.append("@RequestMapping(\"/"+toFirstCharLowerCase(beanName)+"\")\n");
        sb.append("public class "+beanName+"Controller extends GenericController<"+beanName+", "+queryModelName+"> {\r\n");
        sb.append("\t@Inject\n");
        sb.append("\tprivate "+beanName+"Service "+toFirstCharLowerCase(beanName)+"Service;\n");
        sb.append("\t@Override\n\tprotected GenericService<"+beanName+", "+queryModelName+"> getService() {\n");
        sb.append("\t\treturn "+toFirstCharLowerCase(beanName)+"Service;\n");
        sb.append("\t}\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 将首字母变大写
     * @param str
     * @return
     */
    private static String toFirstCharUpCase(String str){
        char[]  columnCharArr = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columnCharArr.length; i++) {
            char cur = columnCharArr[i];
            if(i==0){
                sb.append(Character.toUpperCase(cur));
            }else{
                sb.append(cur);
            }
        }
        return sb.toString();
    }
    /**
     * 将首字母变小写
     * @param str
     * @return
     */
    public static String toFirstCharLowerCase(String str){
        char[]  columnCharArr = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columnCharArr.length; i++) {
            char cur = columnCharArr[i];
            if(i==0){
                sb.append(Character.toLowerCase(cur));
            }else{
                sb.append(cur);
            }
        }
        return sb.toString();
    }
    /**
     * 获取查询实体类的字段类型
     * */
    private static String getQueryModelFieldType(String javaType){
        switch(javaType){
            case "byte": return "Byte";
            case "short": return "Short";
            case "int": return "Integer";
            case "float": return "Float";
            case "double": return "Double";
            case "long": return "Long";
        }
        return "String";
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

    /**
     * 创建文件夹，防止文件路径不存在
     * */
    private static String createFloder(String src, String packagePath) throws IOException{
        String path = GenCodeUtil.class.getResource("/").getPath();
        File pf = new File(path);
        pf = pf.getParentFile().getParentFile();
        pf = new File(pf.getAbsolutePath()+"/"+src);
        String[] subF = packagePath.split("/");
        for(String sf : subF){
            pf = new File(pf.getPath()+"/"+sf);
            if(!pf.exists()){
                pf.mkdirs();
            }
        }
        return pf.getAbsolutePath();
    }
    /**
     * 创建文件夹，防止文件路径不存在
     * */
    private static String createFloder(String basePath) throws IOException{
        String path = GenCodeUtil.class.getResource("/").getPath();
        File pf = new File(path);
        pf = pf.getParentFile().getParentFile();
        String[] subF = basePath.split("/");
        for(String sf : subF){
            if(StringUtils.isNotEmpty(sf)){
                pf = new File(pf.getPath()+"/"+sf);
                if(!pf.exists()){
                    pf.mkdirs();
                }
            }
        }
        return pf.getAbsolutePath();
    }

    /**
     * @param author 作者
     * @param tableName 表名
     * @param extendsBasePackage 继承框架类包的基础路径
     * @param basePackage 生成文件的包的基础路径
     * @param mybatisBasePath mybatis配置文件夹路径
     * @param beanName 实体类名称
     * @param queryModelName 查询类名称
     * @since properties keys include 'db.driver'、'db.username'、'db.password' and 'db.url'
     * */
    public static void genFiles(String author, String tableName, String extendsBasePackage, String basePackage, String mybatisBasePath,  String beanName, String queryModelName, String properitesUri) throws IOException {
        String packagePath = basePackage.replaceAll("\\.", "/");
        JdbcUtil.setPropertiesURL(properitesUri);
        TableModel table = JdbcUtil.getTableStructure(tableName);
        String entityPath = createFloder("src/main/java", packagePath+"/entity");
        //生成实体类文件
        File fEntity = new File(entityPath+"/"+beanName+".java");
        if(fEntity.exists()){
            fEntity.delete();
        }
        FileOutputStream fos = new FileOutputStream(fEntity);
        fos.write(genJavaBeanFromTableStructure(author, table, beanName, basePackage+".entity").getBytes());
        fos.close();
        //生成查询实体类文件
        if(StringUtils.isNotEmpty(queryModelName)){
            File fQEntity = new File(entityPath+"/"+queryModelName+".java");
            if(fQEntity.exists()){
                fQEntity.delete();
            }
            fos = new FileOutputStream(fQEntity);
            fos.write(genQueryModelFromTableStructure(author, table, queryModelName, extendsBasePackage, basePackage+".entity").getBytes());
            fos.close();
        }
        //生成mybatis配置文件
        String mybatisPath = createFloder("/src/main"+mybatisBasePath);
        fos = new FileOutputStream(mybatisPath+"/mybatis_"+toFirstCharLowerCase(beanName)+".xml");
        fos.write(MyBatisUtil.genMapperConfig(table, basePackage+".dao."+beanName+"Dao", basePackage+".entity."+beanName, basePackage+".entity."+queryModelName).getBytes());
        fos.close();
        //生成Dao
        String daoPath = createFloder("src/main/java", packagePath+"/dao");
        File fDao = new File(daoPath+"/"+beanName+"Dao.java");
        fos = new FileOutputStream(fDao);
        fos.write(genDao(author, basePackage+".dao", beanName, queryModelName,extendsBasePackage).getBytes());
        fos.close();
        //生成Service
        String servicePath = createFloder("src/main/java", packagePath+"/service");
        File fService = new File(servicePath+"/"+beanName+"Service.java");
        fos = new FileOutputStream(fService);
        fos.write(genService(author, basePackage+".service", beanName, queryModelName,extendsBasePackage).getBytes());
        fos.close();
        // 生成controller
        String controllerPath = createFloder("src/main/java", packagePath+"/controller");
        File fController = new File(controllerPath+"/"+beanName+"Controller.java");
        fos = new FileOutputStream(fController);
        fos.write(genController(author, basePackage+".controller", beanName, queryModelName,extendsBasePackage).getBytes());
        fos.close();
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        genFiles("linzf", "dict", "com.csdn.demo.common.base","com.csdn.demo.sys", "/resources/mybatis/mapper","Dict", "QueryDict", "application-dev.properties");
    }

}
