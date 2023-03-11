package ru.ptkom.provisor.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.repository.PBXUsersRepository;
import ru.ptkom.provisor.repository.UserRepository;



@Component
public class UserDAO {

    @Autowired
    private UserRepository userRepository;


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
        userRepository.save(user);
    }

    public void updateUser(User user){userRepository.save(user);}

    public void deleteUser(User user) {
        if (user.getUsername() != null) {
            User userToDelete = userRepository.findByUsername(user.getUsername());
            userRepository.delete(userToDelete);
        }
    }

}