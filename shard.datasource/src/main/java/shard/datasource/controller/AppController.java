package shard.datasource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shard.datasource.entity.T1;
import shard.datasource.entity.T3;
import shard.datasource.service.AppServiceImpl;

@Controller
@RequestMapping(value = "api/app")
public class AppController {

	@Autowired
	private AppServiceImpl appService;

	@RequestMapping(value = "info")
	@ResponseBody
	public T1 getInfo() {

		T1 info = this.appService.getT1();
		return info;
	}

	@RequestMapping(value = "info3")
	@ResponseBody
	public T3 getInfo3() {

		T3 info = this.appService.getT3();
		return info;
	}
}
