package ru.ptkom.provisor.dao;

import org.springframework.stereotype.Component;
import ru.ptkom.provisor.models.Role;
import ru.ptkom.provisor.repository.RoleRepository;

import java.util.List;

@Component
public class RoleDAO{

    private final RoleRepository roleRepository;

    public RoleDAO(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public List<Role> getListOfRoles() {
        return roleRepository.findAll();
    }
}
