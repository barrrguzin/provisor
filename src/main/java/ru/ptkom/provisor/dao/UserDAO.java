package ru.ptkom.provisor.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.exception.PBXUserNotFoundException;
import ru.ptkom.provisor.exception.UserNotFoundException;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.repository.UserRepository;

import java.util.Optional;
import java.util.stream.StreamSupport;


@Slf4j
@Component
public class UserDAO {


    private final UserRepository userRepository;
    private final PBXUserDAO pbxUserDAO;

    public UserDAO(UserRepository userRepository, PBXUserDAO pbxUserDAO) {
        this.userRepository = userRepository;
        this.pbxUserDAO = pbxUserDAO;
    }


    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existedUser = user.get();
            log.info("User with ID: " + id + "; and username: " + existedUser.getUsername() + "; got from data base.");
            return existedUser;

        } else {
            log.warn("User with ID: " + id + " in not exist (null).");
            throw new UserNotFoundException("Phone user with ID " + id + " not found.");
        }
    }



    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("User with username: " + username + " in not exist.");
            throw new UserNotFoundException("User with username: " + username + " in not exist.");
        } else {
            log.info("User with username: " + username + " got from data base.");
            return user;
        }
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


    public void updateUser(User user) {
        if (user.getPbxUser() == null) {
            updateIfUserHasNoPhoneNumber(user);
        } else {
            updateIfUserHavePhoneNumber(user);
        }
    }


    private void updateIfUserHasNoPhoneNumber(User user) {
        String newUsername = user.getUsername();
        if (userRepository.findByUsername(newUsername) == null || userRepository.findByUsername(newUsername).getId() == user.getId()) {
            userRepository.save(user);
            log.info("Data of user " + user.getUsername() + " updated.");
        }
    }


    private void updateIfUserHavePhoneNumber(User user) {
        Long id = user.getId();
        String newUsername = user.getUsername();
        PBXUser newNumber = user.getPbxUser();
        try {
            Long oldId = pbxUserDAO.getUserByNumber(newNumber.getNumber()).getId();
            if ((userRepository.findByUsername(newUsername) == null || userRepository.findByUsername(newUsername).getId() == id) && (userRepository.findByPbxUserId(oldId) == null || userRepository.findByPbxUserId(oldId).getId() == id)) {
                userRepository.save(user);
                log.info("Data of user " + user.getUsername() + " updated.");
            } else {
                log.info("Can't update data of user " + newUsername + " because new username or phone number belongs to another user.");
            }
        } catch (PBXUserNotFoundException e) {
            log.error("Cant get ID of user who should to be updated: " + e.getMessage());
        }
    }


    public void deleteUser(Long id) {
        if (id != null) {
            Optional<User> userToDelete = userRepository.findById(id);
            if (userToDelete.isPresent()) {
                userRepository.delete(userToDelete.get());
                log.info("User with username: " + userToDelete.get().getUsername() + " deleted.");
            } else {
                log.info("There is no user with id: " + id);
            }
        } else {
            log.info("Can't delete, id in null.");
        }
    }
}