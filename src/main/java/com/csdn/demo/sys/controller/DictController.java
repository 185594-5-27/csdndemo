package com.csdn.demo.sys.controller;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import com.csdn.demo.common.base.controller.GenericController;
import com.csdn.demo.common.base.service.GenericService;

import com.csdn.demo.sys.entity.Dict;
import com.csdn.demo.sys.entity.QueryDict;
import com.csdn.demo.sys.service.DictService;

/**
 *@author linzf
 **/
@Controller
@RequestMapping("/dict")
public class DictController extends GenericController<Dict, QueryDict> {
	@Inject
	private DictService dictService;
	@Override
	protected GenericService<Dict, QueryDict> getService() {
		return dictService;
	}
}