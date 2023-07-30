package ru.ptkom.provisor.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.exception.PBXUserNotFoundException;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.repository.PBXUsersRepository;
import ru.ptkom.provisor.repository.UserRepository;

import java.util.Optional;
import java.util.stream.StreamSupport;


@Slf4j
@Component
public class PBXUserDAO {


    private final PBXUsersRepository pbxUsersRepository;
    private final UserRepository userRepository;

    public PBXUserDAO(PBXUsersRepository pbxUsersRepository, UserRepository userRepository) {
        this.pbxUsersRepository = pbxUsersRepository;
        this.userRepository = userRepository;
    }


    public PBXUser findByMac(String macAddresses) {
        PBXUser usr = pbxUsersRepository.findByMacIgnoreCase(macAddresses);
        if (usr != null && macAddresses != "" && macAddresses != null) {
            return usr;
        } else {
            PBXUser userWithMacNotFound = new PBXUser("Пользователь не найден","-","-","-");
            return userWithMacNotFound;
        }
    }


    public Iterable<PBXUser> getAllUsers(){
        Iterable<PBXUser> users = pbxUsersRepository.findAllByOrderByNumberAsc();
        long size = StreamSupport.stream(users.spliterator(), false).count();
        log.info("Got " + size + " workers with phones from database.");
        return users;
    }

    public void saveUser(PBXUser user){

        PBXUser checkNumber = pbxUsersRepository.findByNumber(user.getNumber());
        PBXUser checkMac = pbxUsersRepository.findByMacIgnoreCase(user.getMac());


        if (checkNumber == null && checkMac == null) {
            pbxUsersRepository.save(user);
            log.info("Phone user " + user.getName() + " saved to data base");
        } else {
            log.info("MAC address or phone number belongs to another user");
        }
    }


    public String getMacByNumber(String number) throws PBXUserNotFoundException {
        PBXUser user = pbxUsersRepository.findByNumber(number);
        if(user == null){
            log.warn("Phone user with phone number " + number + " not found.");
            throw new PBXUserNotFoundException("Phone user with phone number " + number + " not found.");
        } else {
            String mac = user.getMac();
            log.info("Phone user with phone number " + number + " found and has MAC: " + mac);
            return mac;
        }
    }


    public PBXUser getUserByNumber(String number) throws PBXUserNotFoundException {
        PBXUser user = pbxUsersRepository.findByNumber(number);
        if (user == null) {
            log.warn("Phone user with number " + number + " not exists.");
            throw new PBXUserNotFoundException("Phone user with number " + number + " not exists.");
        } else {
            log.info("Phone user with number " + number + " got from data base.");
        }
        return user;
    }


    public String getPhoneModelByNumber(String number) throws PBXUserNotFoundException {
        PBXUser user = pbxUsersRepository.findByNumber(number);
        if(user == null){
            log.warn("Phone user with number " + number + " not found.");
            throw new PBXUserNotFoundException("Phone user with number " + number + " not found.");
        } else {
            String model = user.getPhoneModel();
            log.info("Phone user with number " + number + " has phone " + model + " .");
            return model;
        }
    }


    public PBXUser getUserById(Long id) throws PBXUserNotFoundException {
        Optional<PBXUser> user = pbxUsersRepository.findById(id);
        if (user.isPresent()) {
            log.info("Got phone user by ID: " + id);
            return user.get();
        } else {
            log.warn("Phone user with ID " + id + " not found.");
            throw new PBXUserNotFoundException("Phone user with ID " + id + " not found.");
        }
    }


    public void update(PBXUser updatedUser) throws PBXUserNotFoundException {


        Long id = updatedUser.getId();


        PBXUser userToBeUpdated = getUserById(id);
        userToBeUpdated.setName(updatedUser.getName());
        userToBeUpdated.setMac(updatedUser.getMac());
        userToBeUpdated.setNumber(updatedUser.getNumber());
        userToBeUpdated.setPhoneModel(updatedUser.getPhoneModel());


        PBXUser checkMac = pbxUsersRepository.findByMacIgnoreCase(userToBeUpdated.getMac());
        PBXUser checkPhone = pbxUsersRepository.findByNumber(userToBeUpdated.getNumber());


        if ((checkMac == null || checkMac.getId() == id) && (checkPhone == null || checkPhone.getId() == id)) {
            pbxUsersRepository.save(userToBeUpdated);
            log.info("Phone user with name " + updatedUser.getName() + " updated in data base.");
        } else {
            log.info("Can't update data of phone user with name " + updatedUser.getName() + ", MAC address or phone number belongs to another user.");
        }
    }


    public void delete(Long id){

        User connectedUser = userRepository.findByPbxUserId(id);
        if (connectedUser != null){
            connectedUser.setPbxUser(null);
            userRepository.save(connectedUser);
            log.info("Field PBX user in connected user(" + connectedUser.getUsername() + ") is cleared.");
        }
        pbxUsersRepository.deleteById(id);
        log.info("Phone user with ID: " + id + " deleted from data base.");
    }


}