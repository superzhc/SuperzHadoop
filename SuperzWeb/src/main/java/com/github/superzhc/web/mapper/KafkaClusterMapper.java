package com.github.superzhc.web.mapper;

import com.github.superzhc.web.model.KafkaCluster;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface KafkaClusterMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kafka_cluster
     *
     * @mbg.generated Tue Sep 08 00:24:05 CST 2020
     */
    @Delete({
        "delete from kafka_cluster",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kafka_cluster
     *
     * @mbg.generated Tue Sep 08 00:24:05 CST 2020
     */
    @Insert({
        "insert into kafka_cluster (id, url)",
        "values (#{id,jdbcType=INTEGER}, #{url,jdbcType=VARCHAR})"
    })
    int insert(KafkaCluster record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kafka_cluster
     *
     * @mbg.generated Tue Sep 08 00:24:05 CST 2020
     */
    @Select({
        "select",
        "id, url",
        "from kafka_cluster",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="url", property="url", jdbcType=JdbcType.VARCHAR)
    })
    KafkaCluster selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kafka_cluster
     *
     * @mbg.generated Tue Sep 08 00:24:05 CST 2020
     */
    @Select({
        "select",
        "id, url",
        "from kafka_cluster"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="url", property="url", jdbcType=JdbcType.VARCHAR)
    })
    List<KafkaCluster> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kafka_cluster
     *
     * @mbg.generated Tue Sep 08 00:24:05 CST 2020
     */
    @Update({
        "update kafka_cluster",
        "set url = #{url,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(KafkaCluster record);

    @Delete({
            "delete from kafka_cluster",
            "where url = #{url,jdbcType=VARCHAR}"
    })
    int deleteByUrl(String url);
}