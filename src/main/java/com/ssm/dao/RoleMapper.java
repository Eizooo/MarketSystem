package com.ssm.dao;

import com.ssm.model.Role;

import java.util.List;

/**
 * @author Eizooo
 * @date 2021/4/21 9:28
 */
public interface RoleMapper {
    /**
     * 拿到表中所有的角色数据
     * @return 角色的集合
     */
    List<Role> getRoles();
}
