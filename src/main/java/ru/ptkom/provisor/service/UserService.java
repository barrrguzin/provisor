package ru.ptkom.provisor.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ptkom.provisor.dao.PBXUserDAO;
import ru.ptkom.provisor.dao.UserDAO;
import ru.ptkom.provisor.exception.PBXUserNotFoundException;
import ru.ptkom.provisor.models.Role;
import ru.ptkom.provisor.repository.RoleRepository;
import ru.ptkom.provisor.models.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements UserDetailsService {


    private final RoleRepository roleRepository;
    private final UserDAO userDAO;
    private final PBXUserDAO pbxUserDAO;


    private PasswordEncoder passwordEncoder;


    public UserService(RoleRepository roleRepository, UserDAO userDAO, PBXUserDAO pbxUserDAO) {
        this.roleRepository = roleRepository;
        this.userDAO = userDAO;
        this.pbxUserDAO = pbxUserDAO;
    }


    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder=passwordEncoder;
    }


    public User findByUsername(String username){
    return userDAO.getUserByUsername(username);
}


    public void updateUser(User newUserData) {
        User user = userDAO.getUserById(newUserData.getId());
        processUsernameUpdate(user, newUserData);
        processPasswordUpdate(user, newUserData);
        processRoleUpdate(user, newUserData);
        processNumberUpdate(user, newUserData);
        log.debug("User " + user.getUsername() + " send to update method.");
        userDAO.updateUser(user);
    }


    private void processRoleUpdate(User currentUserData, User newUserData) {
        Collection<Role> roles = newUserData.getRoles();
        if (roles.size() != 0) {
            currentUserData.setRoles(roles);
            repairRoleList(currentUserData);
        }
    }


    private void processUsernameUpdate(User currentUserData, User newUserData) {
        String newUsername = newUserData.getUsername();
        if (newUsername != "" && newUsername != currentUserData.getUsername()) {
            currentUserData.setUsername(newUsername);
        }
    }


    private void processPasswordUpdate(User currentUserData, User newUserData) {
        String newPassword = newUserData.getPassword();
        if (newPassword != "" || newPassword != null) {
            currentUserData.setPassword(newPassword);
            encodePassword(currentUserData);
        }
    }


    private void processNumberUpdate(User currentUserData, User newUserData) {
        String newNumber = newUserData.getPbxUser().getNumber();
        if (currentUserData.getPbxUser() == null) {
            if (newNumber == "") {
                currentUserData.setPbxUser(null);
            } else {
                currentUserData.setPbxUser(pbxUserDAO.getUserByNumber(newNumber));
            }
        } else {
            if (newNumber != currentUserData.getPbxUser().getNumber()) {
                if (newNumber == "") {
                    currentUserData.setPbxUser(null);
                } else {
                    currentUserData.setPbxUser(pbxUserDAO.getUserByNumber(newNumber));
                }
            }
        }
    }


    public void addUser(User user) {
        repairRoleList(user);
        repairPBXUser(user);
        encodePassword(user);
        saveUser(user);
    }


    private void repairRoleList(User user) {
        Collection<Role> roles = user.getRoles();
        List<Role> list = new ArrayList<Role>(roles.size());
        for (Role role : roles) {
            list.add(roleRepository.findByName(role.getName()));
        }
        user.setRoles(list);
    }


    private void repairPBXUser(User user) {
        try {
            String number = user.getPbxUser().getNumber();
            user.setPbxUser(pbxUserDAO.getUserByNumber(number));
        } catch (PBXUserNotFoundException e) {
            user.setPbxUser(null);
            log.info("There is no phone number to new user " + user.getUsername());
        }
    }


    private void encodePassword(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        log.debug("Password to " + user.getUsername() + " encoded.");
    }


    private void saveUser(User user) {
        log.debug("User " + user.getUsername() + " send to add method.");
        userDAO.saveUser(user);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
            mapRolesToAuthorities(user.getRoles()));
    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());

    }


}