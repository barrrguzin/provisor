package ru.ptkom.provisor.repository;


import org.springframework.data.repository.CrudRepository;


import org.springframework.stereotype.Repository;
import ru.ptkom.provisor.models.User;


@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);
    User findByPbxUserId(Long pbxUserId);
    Iterable<User> findAllByOrderByUsername();
}
