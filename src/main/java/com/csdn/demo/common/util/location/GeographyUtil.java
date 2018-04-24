package com.csdn.demo.common.util.location;

import java.net.URL;

/*
* 类描述：地理位置操作类
* @auther linzf
* @create 2018/3/7 0007 
*/
public class GeographyUtil {

    public static void main(String [] args){
        getAdd("119.2875270000","26.1006680000");

    }

    /**
     * 功能描述：根据经纬度来计算具体地址
     * @param log
     * @param lat
     * @return
     */
    public static String getAdd(String log, String lat ){
        //lat 小  log  大
        //参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
        String urlString = "http://gc.ditu.aliyun.com/regeocoding?l="+lat+","+log+"&type=010";
        String res = "";
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line+"\n";
            }
            in.close();
        } catch (Exception e) {
            System.out.println("error in wapaction,and e is " + e.getMessage());
        }
        System.out.println(res);
        return res;
    }

}
