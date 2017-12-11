package com.csdn.demo.common.util.dict;





import com.csdn.demo.sys.entity.Dict;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/*
* 类描述：数据字典操作类
* @auther linzf
* @create 2017/8/24 0024 
*/
public class DictCache {

    /**静态数据字典信息*/
    private static Map<String,Map<String,Dict>> dictMap = new Hashtable<String,Map<String,Dict>>();

    /**
     * 获取数据字典
     * @return Map
     * */
    public static Map<String, Map<String, Dict>> getDictMap() {
        return dictMap;
    }

    /**
     * 载入数据字典缓存
     * @param list
     * */
    public static void load(List<Dict> list){
        for(Dict d : list){
            Map<String,Dict> map = dictMap.get(d.getType());
            if(map == null){
                map = new Hashtable<String,Dict>();
                dictMap.put(d.getType(), map);
            }
            map.put(d.getCode(), d);
        }
    }

    /**
     * 重新载入数据字典缓存
     * @param list
     * */
    public static void reload(List<Dict> list){
        dictMap.clear();
        load(list);
    }

    /**
     * 获取数据字典
     * @param type
     * @return Collection<Dict>
     * */
    public static Collection<Dict> getDicts(String type){
        Map<String, Dict> map = dictMap.get(type);
        if(map!=null)
            return map.values();
        return null;
    }
    /**
     * 获取数据字典
     * @param type
     * @param code
     * @return Dict
     * */
    public static Dict getDict(String type, String code){
        Map<String, Dict> map = dictMap.get(type);
        if(map!=null)
            return map.get(code);
        return null;
    }

    /**
     * 获取数据字典
     * @param type
     * @param value
     * @return Dict
     * */
    public static Dict getDictByTypeAndValue(String type, String value){
        Map<String, Dict> map = dictMap.get(type);
        if(map!=null){
            for(Dict dict : map.values()){
                if(value != null && value.equals(dict.getValue())){
                    return dict;
                }
                else if(dict.getValue()==null){
                    return dict;
                }
            }
        }
        return null;
    }
    /**
     * 获取数据字典值
     * @param type
     * @param code
     * @return Dict
     * */
    public static String getDictValue(String type, String code){
        Map<String, Dict> map = dictMap.get(type);
        if(map!=null){
            Dict dict = map.get(code);
            if(dict!=null)
                return dict.getValue();
        }
        return null;
    }

}
