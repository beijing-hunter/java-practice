package shard.datasource.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import shard.datasource.entity.T2;
import shard.datasource.entity.T2Example;

public interface T2Mapper {
    int countByExample(T2Example example);

    int deleteByExample(T2Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(T2 record);

    int insertSelective(T2 record);

    List<T2> selectByExample(T2Example example);

    T2 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") T2 record, @Param("example") T2Example example);

    int updateByExample(@Param("record") T2 record, @Param("example") T2Example example);

    int updateByPrimaryKeySelective(T2 record);

    int updateByPrimaryKey(T2 record);
}