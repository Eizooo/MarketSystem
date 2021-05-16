package com.ssm.service.impl;

import com.ssm.dao.RoleMapper;
import com.ssm.model.Role;
import com.ssm.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Eizooo
 * @date 2021/4/21 10:43
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoles() {
        return roleMapper.getRoles();
    }
}
