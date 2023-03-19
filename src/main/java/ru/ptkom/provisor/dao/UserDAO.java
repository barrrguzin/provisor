package ru.ptkom.provisor.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.repository.UserRepository;

import java.util.stream.StreamSupport;


@Slf4j
@Component
public class UserDAO {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PBXUserDAO pbxUserDAO;


    public User getUserById(Long id) {
        User user = userRepository.findById(id).get();
        if (user == null) {
            log.info("User with ID: " + id + " in not exist (null).");
        } else {
            log.info("User with ID: " + id + "; and username: " + user.getUsername() + "; got from data base.");
        }
        return user;
    }


    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.info("User with username: " + username + " in not exist (null).");
        } else {
            log.info("User with username: " + username + " got from data base.");
        }
        return user;
    }


    public Iterable<User> getAllUsers() {
        Iterable<User> users = userRepository.findAllByOrderByUsername();
        long size = StreamSupport.stream(users.spliterator(), false).count();
        log.info("Got " + size + " users from database.");
        return users;
    }

    public void saveUser(User user){
        if (userRepository.findByUsername(user.getUsername()) == null) {
            userRepository.save(user);
            log.info("User " + user.getUsername() + " saved to data base.");
        } else {
            log.info("Can't save user " + user.getUsername() + " to data base, user already exists.");
        }
    }


    public void updateUser(User user){


        Long id = user.getId();
        String newUsername = user.getUsername();
        PBXUser newNumber = user.getPbxUser();


        if (newNumber == null) {
            if (userRepository.findByUsername(newUsername) == null || userRepository.findByUsername(newUsername).getId() == id) {
                userRepository.save(user);
                log.info("Data of user " + user.getUsername() + " updated.");
            }
        } else {
            Long oldId = pbxUserDAO.getUserByNumber(newNumber.getNumber()).getId();
            if ((userRepository.findByUsername(newUsername) == null || userRepository.findByUsername(newUsername).getId() == id) && (userRepository.findByPbxUserId(oldId) == null || userRepository.findByPbxUserId(oldId).getId() == id)) {
                userRepository.save(user);
                log.info("Data of user " + user.getUsername() + " updated.");
            } else {
                log.info("Can't update data of user " + newUsername + " because new username or phone number belongs to another user.");
            }
        }
    }


    public void deleteUser(User user) {
        if (user.getUsername() != null) {
            User userToDelete = userRepository.findByUsername(user.getUsername());
            userRepository.delete(userToDelete);
            log.info("User with username: " + user.getUsername() + " deleted.");
        } else {
            log.info("Can't delete, username in null.");
        }
    }
}