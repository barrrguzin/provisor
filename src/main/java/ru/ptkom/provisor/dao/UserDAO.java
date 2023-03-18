package ru.ptkom.provisor.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.repository.UserRepository;



@Component
public class UserDAO {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PBXUserDAO pbxUserDAO;


    public User getUserById(Long id) {

        User user = userRepository.findById(id).get();
        return user;
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }


    public Iterable<User> getAllUsers() {
        return userRepository.findAllByOrderByUsername();
    }

    public void saveUser(User user){
        if (userRepository.findByUsername(user.getUsername()) == null) {
            userRepository.save(user);
        }
    }


    public void updateUser(User user){


        Long id = user.getId();
        String newUsername = user.getUsername();
        PBXUser newNumber = user.getPbxUser();


        if (newNumber == null) {
            if (userRepository.findByUsername(newUsername) == null || userRepository.findByUsername(newUsername).getId() == id) {
                userRepository.save(user);
            }
        } else {
            Long oldId = pbxUserDAO.getUserByNumber(newNumber.getNumber()).getId();
            if ((userRepository.findByUsername(newUsername) == null || userRepository.findByUsername(newUsername).getId() == id) && (userRepository.findByPbxUserId(oldId) == null || userRepository.findByPbxUserId(oldId).getId() == id)) {
                userRepository.save(user);
            }
        }
    }


    public void deleteUser(User user) {
        if (user.getUsername() != null) {
            User userToDelete = userRepository.findByUsername(user.getUsername());
            userRepository.delete(userToDelete);
        }
    }
}