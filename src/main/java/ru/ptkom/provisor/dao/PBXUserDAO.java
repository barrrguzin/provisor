package ru.ptkom.provisor.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.repository.PBXUsersRepository;
import ru.ptkom.provisor.repository.UserRepository;


@Component
public class PBXUserDAO {

    @Autowired
    private PBXUsersRepository pbxUsersRepository;
    @Autowired
    private UserRepository userRepository;


    public String[][] findByMacList(String[] macAddresses) {

        String[][] userDataList = new String[macAddresses.length][3];

        for (int i = 0; i < macAddresses.length; i++) {
            PBXUser usr = pbxUsersRepository.findByMacIgnoreCase(macAddresses[i]);
            if (usr != null) {
                userDataList[i][0] = usr.getName();
                userDataList[i][1] = usr.getNumber();
                userDataList[i][2] = usr.getPhoneModel();
            } else {
                userDataList[i][0] = "Пользователь не найден";
                userDataList[i][1] = "-";
                userDataList[i][2] = "-";
            }
        }
        return userDataList;
    }


    public Iterable<PBXUser> getAllUsers(){
        return pbxUsersRepository.findAllByOrderByNumberAsc();
    }

    public void saveUser(PBXUser user){
        pbxUsersRepository.save(user);
    }


    public String getMacByNumber(String number){
        PBXUser user = pbxUsersRepository.findByNumber(number);
        if(user == null){
            return null;
        }
        return user.getMac();
    }


    public PBXUser getUserByNumber(String number){
        PBXUser user = pbxUsersRepository.findByNumber(number);
        return user;
    }


    public String getPhoneModelByNumber(String number){
        PBXUser user = pbxUsersRepository.findByNumber(number);
        if(user == null){
            return null;
        }
        return user.getPhoneModel();
    }


    public PBXUser getUserById(Long id) {
        PBXUser user = null;
        try {
            user = pbxUsersRepository.findById(id).get();
        } finally {
            return user;
        }
    }


    public void update(PBXUser updatedUser){
        Long id = updatedUser.getId();
        PBXUser userToBeUpdated = getUserById(id);
        pbxUsersRepository.findById(id);
        userToBeUpdated.setName(updatedUser.getName());
        userToBeUpdated.setMac(updatedUser.getMac());
        userToBeUpdated.setNumber(updatedUser.getNumber());
        userToBeUpdated.setPhoneModel(updatedUser.getPhoneModel());
        pbxUsersRepository.save(userToBeUpdated);
    }


    public void delete(Long id){

        User connectedUser = userRepository.findByPbxUserId(id);
        if (connectedUser != null){
            connectedUser.setPbxUser(null);
            userRepository.save(connectedUser);
        }
        pbxUsersRepository.deleteById(id);
    }


}
