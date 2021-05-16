package com.ssm.dao;

import com.ssm.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eizooo
 * @date 2021/4/1 10:15
 */
public interface UserMapper {
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
     * 获取表中对应的用户(用于登陆)
     * @param user 从客户端login.jsp中获得的对象
     * @return 从数据库中获得的user 可能为null
     */
    User getUser(User user);

    /**
     * 往smbms_user表中添加用户
     * @param user
     * @return 影响的行数
     */
    int addUser(User user);

    /**
     * 根据id来修改smbms_user表中用户部分字段,过滤空值选项
     * @param user
     * @return 影响行数
     */
    int modifyFilter(User user);

    /**
     * 修改smbms_user表中用户数据
     * @param user
     * @return 影响行数
     */
    int modify(User user);

    /**
     * 根据id修改smbms_user表中用户密码
     * @param pwd
     * @param id
     * @return 影响行数
     */
    int modifyPassword(@Param("userPassword") String pwd, @Param("id") Integer id);

    /**
     * 通过id删除smbms_user中的用户对象
     * @param id
     * @return 影响行数
     */
    int deleteUserById(@Param("id") Integer id);

    /**
     * 通过id查找用户
     * @param id
     * @return
     */
    User getUserById(@Param("id") Integer id);

    /**
     * 通过roleId查找目标用户的roleName
     * @param roleId
     * @return 结果集
     */
    List<User> getUserRoleNameByRoleId(@Param("id") Integer roleId);

    /**
     * 通用用户id查找目标用户的住房信息
     * @param userId
     * @return 结果集
     */
    List<User> getUserAddressNameById(@Param("id") Integer userId);

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
     * @return
     */
    Integer getUserCount(@Param("userRole") Integer userRole, @Param("userName") String userName);

    /**
     * 查找userCode是否存在
     * @param userCode
     * @return User
     */
    User getUserCodeExist(@Param("userCode") String userCode);
}
