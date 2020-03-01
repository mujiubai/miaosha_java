package com.example.miaosha.Service;

import com.example.miaosha.Mapper.UserMapper;
import com.example.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired(required=false)
    private UserMapper userMapper;

    public User getUser(int id) {
        return this.userMapper.getUser(id);
    }
    @Transactional
    public int serUser() {
        User user=new User();
        user.setId(3);
        user.setName("mingzi");
        int i=userMapper.setUser(user);
        return i;
    }
}
