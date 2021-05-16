package com.ssm.service.impl;

import com.ssm.dao.UserMapper;
import com.ssm.model.User;
import com.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eizooo
 * @date 2021/4/14 18:40
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier("userMapper")
    private UserMapper userMapper;

    @Override
    public List<User> getUserList() {
        return userMapper.getUserList();
    }

    @Override
    public ArrayList<String> getUserName() {
        return userMapper.getUserName();
    }

    @Override
    public boolean addUser(List<User> user) throws Exception{
        boolean isAdded = false;
        int sum = 0;
        for (User user1 : user) {
            int num = userMapper.addUser(user1);
            if(num == 1){
                sum++;
            }
        }
        if(sum == user.size()){
            isAdded = true;
        }
        return isAdded;
    }

    @Override
    public User getUser(User user) {
        return userMapper.getUser(user);
    }

    @Override
    public List<User> getUserListRollPage(Integer userRole, String userName, Integer currentPageNo, Integer pageSize) {
        int form = (currentPageNo - 1) * pageSize;
        return userMapper.getUserListRollPage(userRole,userName,form,pageSize);
    }

    @Override
    public Integer getUserCount(Integer userRole, String userName) {
        return userMapper.getUserCount(userRole, userName);
    }

    @Override
    public boolean addUser(User user) {
        int num = userMapper.addUser(user);
        if(num > 0){
            return true;
        }
        return false;
    }

    @Override
    public User getUserById(String uid) {
        int id = Integer.parseInt(uid);
        return userMapper.getUserById(id);
    }

    @Override
    public boolean modifyUser(User user) {
        int num = userMapper.modifyFilter(user);
        if(num > 0){
            return true;
        }
        return false;
    }

    @Override
    public User getUserCodeExist(String userCode) {
        return userMapper.getUserCodeExist(userCode);
    }
}
