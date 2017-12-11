package com.csdn.demo.common.config.init;


import com.csdn.demo.common.util.dict.DictCache;
import com.csdn.demo.sys.dao.DictDao;
import com.csdn.demo.sys.entity.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/*
* 类描述：程序启动完成以后加载数据字典的数据（Order值为1表示加载顺序为第一个）
* @auther linzf
* @create 2017/8/24 0024 
*/
@Component
@Order(value=1)
public class InitDictCache implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitDictCache.class);

    @Inject
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DictDao dictDao;

    @Override
    public void run(String... strings) throws Exception {
           logger.info("-------------初始化数据字典-------------");
           // 加载所有的数据字典的数据
           List<Dict> dictList = dictDao.loadAll();
           DictCache.load(dictList);
           logger.info("-------------完成数据字典初始化-------------");
    }
}
