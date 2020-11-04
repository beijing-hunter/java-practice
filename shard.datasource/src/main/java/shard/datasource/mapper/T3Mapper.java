package shard.datasource.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import shard.datasource.entity.T3;
import shard.datasource.entity.T3Example;

public interface T3Mapper {
    int countByExample(T3Example example);

    int deleteByExample(T3Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(T3 record);

    int insertSelective(T3 record);

    List<T3> selectByExample(T3Example example);

    T3 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") T3 record, @Param("example") T3Example example);

    int updateByExample(@Param("record") T3 record, @Param("example") T3Example example);

    int updateByPrimaryKeySelective(T3 record);

    int updateByPrimaryKey(T3 record);
}