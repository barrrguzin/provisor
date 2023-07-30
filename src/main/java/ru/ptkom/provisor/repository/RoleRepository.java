package ru.ptkom.provisor.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ptkom.provisor.models.Role;


import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);

    List<Role> findAll();

}
