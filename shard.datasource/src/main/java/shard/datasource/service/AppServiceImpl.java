package shard.datasource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shard.datasource.entity.T1;
import shard.datasource.entity.T3;
import shard.datasource.mapper.T1Mapper;
import shard.datasource.mapper.T3Mapper;

@Service
public class AppServiceImpl {

	@Autowired
	private T1Mapper t1Mapper;

	@Autowired
	private T3Mapper t3Mapper;

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public T1 getT1() {

		this.getT3();
		return this.t1Mapper.selectByPrimaryKey(1);
	}

	public T3 getT3() {

		return this.t3Mapper.selectByPrimaryKey(2);
	}
}
