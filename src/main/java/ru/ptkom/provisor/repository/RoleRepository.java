package ru.ptkom.provisor.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ptkom.provisor.models.Role;
import ru.ptkom.provisor.models.User;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);

}
