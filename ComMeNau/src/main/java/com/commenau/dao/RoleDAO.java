package com.commenau.dao;

import com.commenau.model.Role;

import java.util.Optional;
import com.commenau.connectionPool.ConnectionPool;
public class RoleDAO {
    public Role getRoleByName(String roleName) {
        Optional<Role> role = ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery("SELECT * FROM roles WHERE name = ?")
                        .bind(0, roleName).mapToBean(Role.class).stream().findFirst()

        );
        return role.orElse(null);
    }

    public Role getRoleById(int id) {
        Optional<Role> role = ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery("SELECT * FROM roles WHERE id = ?")
                        .bind(0, id).mapToBean(Role.class).stream().findFirst()

        );
        return role.orElse(null);
    }

}
