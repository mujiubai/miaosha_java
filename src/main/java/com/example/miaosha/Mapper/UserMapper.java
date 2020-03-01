package com.example.miaosha.Mapper;

import com.example.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where id=#{id}")
    User getUser(int id);

    @Insert("insert into user(id,name) values(#{id},#{name})")
    int setUser(User user);

}
