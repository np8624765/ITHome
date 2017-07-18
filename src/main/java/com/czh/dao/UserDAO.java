package com.czh.dao;

import com.czh.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * Created by Chen on 2017/4/18.
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = "name, password, salt, head_url";
    String SELECT_FIELDS = "id, name, password, salt, head_url";

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ",SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id = #{id}"})
    int updatePwdbyId(User user);

    @Delete({"delete from ", TABLE_NAME, " where id = #{id}"})
    int deleteUserbyId(int id);

    @Select({"select ", SELECT_FIELDS," from ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);
}
