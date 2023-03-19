package ru.ptkom.provisor.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.repository.PBXUsersRepository;
import ru.ptkom.provisor.repository.UserRepository;

import java.io.IOException;
import java.util.stream.StreamSupport;


@Slf4j
@Component
public class PBXUserDAO {

    @Autowired
    private PBXUsersRepository pbxUsersRepository;
    @Autowired
    private UserRepository userRepository;


    public String[][] findByMacList(String[] macAddresses) {
        int foundUsers = 0;
        String[][] userDataList = new String[macAddresses.length][3];

        for (int i = 0; i < macAddresses.length; i++) {
            PBXUser usr = pbxUsersRepository.findByMacIgnoreCase(macAddresses[i]);
            if (usr != null) {
                userDataList[i][0] = usr.getName();
                userDataList[i][1] = usr.getNumber();
                userDataList[i][2] = usr.getPhoneModel();
                foundUsers++;
            } else {
                userDataList[i][0] = "Пользователь не найден";
                userDataList[i][1] = "-";
                userDataList[i][2] = "-";
            }
        }
        log.info("For " + macAddresses.length + " MAC addresses found " + foundUsers + " workers with phones in data base.");
        return userDataList;
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
            log.info("Phone user " + user.getName() + "saved to data base.");
        } else {
            log.info("MAC address or phone number belongs to another user.");
        }
    }


    public String getMacByNumber(String number){
        PBXUser user = pbxUsersRepository.findByNumber(number);
        if(user == null){
            log.info("Phone user with phone number " + number + " not found.");
            return null;
        } else {
            String mac = user.getMac();
            log.info("Phone user with phone number " + number + " found and has MAC: " + mac);
            return mac;
        }
    }


    public PBXUser getUserByNumber(String number){
        PBXUser user = pbxUsersRepository.findByNumber(number);
        if (user == null) {
            log.info("Phone user with number " + number + " not exists, return null.");
        } else {
            log.info("Phone user with number " + number + " got from data base.");
        }
        return user;
    }


    public String getPhoneModelByNumber(String number){
        PBXUser user = pbxUsersRepository.findByNumber(number);
        if(user == null){
            log.info("Phone user with number " + number + " not found.");
            return null;
        } else {
            String model = user.getPhoneModel();
            log.info("Phone user with number " + number + " has phone " + model + " .");
            return model;
        }
    }


    public PBXUser getUserById(Long id) {
        PBXUser user = null;
        try {
            user = pbxUsersRepository.findById(id).get();
            log.info("Got phone user by ID: " + id + ".");
        } finally {
            return user;
        }
    }


    public void update(PBXUser updatedUser){


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