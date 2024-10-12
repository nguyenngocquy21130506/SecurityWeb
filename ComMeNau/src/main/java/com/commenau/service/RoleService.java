package com.commenau.service;

import com.commenau.dao.RoleDAO;
import com.commenau.model.Role;

import javax.inject.Inject;

public class RoleService {
    @Inject
    private RoleDAO roleDao;

    public Role getById(int id) {
        return roleDao.getRoleById(id);
    }
}
