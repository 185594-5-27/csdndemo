package com.csdn.demo.sys.controller;

import com.csdn.demo.common.util.excel.ExcelUtil;
import com.csdn.demo.common.util.json.JsonHelper;
import com.csdn.demo.common.util.location.Geography;
import com.csdn.demo.common.util.location.GeographyUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/*
* 类描述：
* @auther linzf
* @create 2018/3/7 0007 
*/
@Controller
@RequestMapping("/location")
public class LocationController {

    //文件上传相关代码
    @RequestMapping(value = "uploadFile",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadFile(@RequestParam("file_data") MultipartFile file) {
        Map<String,Object> result = new HashMap<String, Object>();
        ExcelUtil eu = new ExcelUtil();
        System.out.println("----------------------解析开始---------------------"+file.getName());
        if(file !=null){
            try{
                InputStream is = file.getInputStream();
                List<String> gps = eu.readCustomerOrderInfo(is);
                String [] infos;
                String address;
                Geography g = null;
                List<Geography> list = new ArrayList<Geography>();
                for(String info:gps){
                    infos = info.split("-,");
                    if(infos.length<8){
                        System.out.println("----------------------持续解析中---------------------");
                        continue;
                    }
                    g = new Geography();
                    g.setLat(infos[4]);
                    g.setLon(infos[5]);
                    g.setPlateNo(infos[6]);
                    g.setCreateTime(infos[7]);
                    address = GeographyUtil.getAdd(g.getLon(),g.getLat());
                    JSONObject jobj = JSONObject.fromObject(address);
                    String addrList = jobj.getString("addrList");
                    JSONArray jsonArray = JSONArray.fromObject(addrList);
                    if(jsonArray.size()>0){
                        jobj = (JSONObject)jsonArray.get(0);
                        g.setAddress( jobj.getString("admName")+jobj.getString("name"));
                        g.setAdmCode(jobj.getString("admCode"));
                    }
                    list.add(g);
                }
                System.out.println("----------------------解析结束---------------------");
                File f= new File("d:" + File.separator + file.getName()+".xls") ;
                OutputStream out = new FileOutputStream(f)  ;
                LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
                fieldMap.put("address","具体地址");
                fieldMap.put("admCode","编码");
                fieldMap.put("lat","维度");
                fieldMap.put("lon","经度");
                fieldMap.put("plateNo","车牌号");
                fieldMap.put("createTime","创建时间");
                eu.listToExcel(list,fieldMap,"定位信息",200000,out);
            }catch (Exception e){

            }
        }
        return result;
    }

}
