package com.csdn.demo.sys.controller;

import com.csdn.demo.common.base.constant.SystemStaticConst;
import com.csdn.demo.common.base.controller.GenericController;
import com.csdn.demo.common.base.service.GenericService;
import com.csdn.demo.common.util.dict.DictCache;
import com.csdn.demo.common.util.json.JsonHelper;
import com.csdn.demo.common.util.redis.RedisCache;
import com.csdn.demo.sys.entity.Dict;
import com.csdn.demo.sys.entity.QueryDict;
import com.csdn.demo.sys.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 类描述：
* @auther linzf
* @create 2017/10/9 0009 
*/
@Controller
@RequestMapping("/dict")
public class DictController extends GenericController<Dict,QueryDict> {

    @Autowired
    private DictService dictService;
    @Autowired
    private RedisCache redisCache;

    @Override
    protected GenericService getService() {
        return dictService;
    }

    @RequestMapping(value = "/redisTest",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void redisTest(String key,String value){
        List<Dict> dicts = new ArrayList<Dict>();
        Dict dict = new Dict();
        dict.setCode("高达哈工大");
        dicts.add(dict);
        redisCache.setList("dicts",dicts);
        List<Dict> dictss  = redisCache.getList("dicts",new Dict());
        System.out.println("redisCache.getList(\"dicts\",Dict.class===>"+ JsonHelper.list2json(dictss));
        redisCache.setString(key,value);
        System.out.println("redisCache.getString(key)===>"+redisCache.getString(key));
    }

    /**
     * 功能描述：将字典数据初始化到前端js中
     * @return
     */
    @RequestMapping(value = "/loadDict",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String,Object> loadDict(){
        Map<String,Object> result = new HashMap<String, Object>();
        List<Dict> dictList = dictService.query(new QueryDict("1"));
        result.put(SystemStaticConst.RESULT,SystemStaticConst.SUCCESS);
        result.put("data",dictList);
        return result;
    }

    /**
     * 功能描述：重新加载数据字典的数据到内存中
     * @return
     */
    @RequestMapping(value = "/reload",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String,Object> reload(){
        Map<String,Object> result = new HashMap<String, Object>();
        List<Dict> dictList = dictService.query(null);
        DictCache.reload(dictList);
        result.put(SystemStaticConst.RESULT,SystemStaticConst.SUCCESS);
        result.put(SystemStaticConst.MSG,"重新加载数据字典成功！");
        return result;
    }


}
