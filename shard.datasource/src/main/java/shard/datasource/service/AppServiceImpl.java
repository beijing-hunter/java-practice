package shard.datasource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shard.datasource.entity.T1;
import shard.datasource.mapper.T1Mapper;

@Service
public class AppServiceImpl {

	@Autowired
	private T1Mapper t1Mapper;

	public T1 getT1() {

		return this.t1Mapper.selectByPrimaryKey(1);
	}
}
