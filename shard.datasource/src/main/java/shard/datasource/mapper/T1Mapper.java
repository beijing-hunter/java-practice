package shard.datasource.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import shard.datasource.entity.T1;
import shard.datasource.entity.T1Example;

public interface T1Mapper {
    int countByExample(T1Example example);

    int deleteByExample(T1Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(T1 record);

    int insertSelective(T1 record);

    List<T1> selectByExample(T1Example example);

    T1 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") T1 record, @Param("example") T1Example example);

    int updateByExample(@Param("record") T1 record, @Param("example") T1Example example);

    int updateByPrimaryKeySelective(T1 record);

    int updateByPrimaryKey(T1 record);
}