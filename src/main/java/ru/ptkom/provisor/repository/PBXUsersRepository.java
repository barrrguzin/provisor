package ru.ptkom.provisor.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ptkom.provisor.models.PBXUser;

public interface PBXUsersRepository extends CrudRepository<PBXUser, Long> {


    PBXUser findByNumber(String number);
    PBXUser findByMacIgnoreCase(String number);
    Iterable<PBXUser> findAllByOrderByNumberAsc();
    Iterable<PBXUser> findAllByOrderByNumberDesc();
}
