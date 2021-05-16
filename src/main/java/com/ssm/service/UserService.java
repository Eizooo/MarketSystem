package com.ssm.service;

import com.ssm.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eizooo
 * @date 2021/4/14 18:39
 */
public interface UserService {
    /**
     * 获取表中所有用户
     * @return 用户列表
     */
    List<User> getUserList();

    /**
     * 获取表中所有用户名
     * @return 用户名列表
     */
    ArrayList<String> getUserName();

    /**
     * 往smbms_user表中添加用户
     * @param user
     * @return 影响的行数
     */
    boolean addUser(List<User> user) throws Exception;

    /**
     * 获取表中对应的用户(用于登陆)
     * @param user 从客户端login.jsp中获得的对象
     * @return 从数据库中获得的user 可能为null]
     */
    User getUser(User user);

    /**
     * 对符合条件的用户列表进行分页展示
     * @param userRole
     * @param userName
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<User> getUserListRollPage(@Param("userRole") Integer userRole,
                                   @Param("userName") String userName,
                                   @Param("form") Integer currentPageNo,
                                   @Param("pageSize") Integer pageSize);

    /**
     * 得到符合条件的用户总数
     * @param userRole
     * @param userName
     * @return 符合条件的用户总数
     */
    Integer getUserCount(@Param("userRole") Integer userRole, @Param("userName") String userName);

    /**
     * 添加用户进数据库
     * @param user
     * @return 是否添加成功
     */
    boolean addUser(User user);

    /**
     * 通过id查找用户
     * @param uid
     * @return 查询到的用户
     */
    User getUserById(String uid);

    /**
     * 修改客户信息
     * @param user
     * @return 修改成功与否
     */
    boolean modifyUser(User user);

    /**
     * 查找userCode是否存在
     * @param userCode
     * @return User
     */
    User getUserCodeExist(@Param("userCode") String userCode);
}