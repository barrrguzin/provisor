package ru.ptkom.provisor.repository;


import org.springframework.data.repository.CrudRepository;


import org.springframework.stereotype.Repository;
import ru.ptkom.provisor.models.User;


@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);

//    @Modifying
//    @Transactional
//    @Query(value = "select * from users where pbx_user_id = ':pbx_user_id'", nativeQuery = true)
//    List<User> findByPBXUSerId(@Param("pbx_user_id") Long pbx_user_id);
    User findByPbxUserId(Long pbxUserId);

    Iterable<User> findAllByOrderByUsername();
}
