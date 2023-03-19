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
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.models.Role;
import ru.ptkom.provisor.repository.RoleRepository;
import ru.ptkom.provisor.repository.UserRepository;
import ru.ptkom.provisor.models.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserDAO userDAO;

    private PasswordEncoder passwordEncoder;
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder=passwordEncoder;
    }

    @Autowired
    private PBXUserDAO pbxUserDAO;

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }


    public void updateUser(User user, String number, String[] roles){
        if (roles != null) {
            List<Role> list = new ArrayList<Role>(roles.length);
            for (int i = 0; i < roles.length; i++) {
                Role role = roleRepository.findByName(roles[i]);
                list.add(role);
            }
            user.setRoles(list);
        }


        User oldUser = userDAO.getUserByUsername(user.getUsername());

        if (user.getPassword().equals("")){
            user.setPassword(oldUser.getPassword());
        }
        else {
            String newPass = user.getPassword();
            user.setPassword(passwordEncoder.encode(newPass));
        }
        PBXUser pbxUser = pbxUserDAO.getUserByNumber(number);
        user.setPbxUser(pbxUser);

        Long id = oldUser.getId();
        user.setId(id);

        log.debug("User " + user.getUsername() + " send to update method.");
        userDAO.updateUser(user);
    }
    public void updateUser(User user, String[] roles){
        if (roles != null) {
            List<Role> list = new ArrayList<Role>(roles.length);
            for (int i = 0; i < roles.length; i++) {
                Role role = roleRepository.findByName(roles[i]);
                list.add(role);
            }
            user.setRoles(list);
        }

        User oldUser = userDAO.getUserByUsername(user.getUsername());

        if (user.getPassword().equals("")){
            user.setPassword(oldUser.getPassword());
        }
        else {
            String newPass = user.getPassword();
            user.setPassword(passwordEncoder.encode(newPass));
        }

        Long id = oldUser.getId();
        user.setId(id);

        user.setPbxUser(null);

        log.debug("User " + user.getUsername() + " send to update method.");
        userDAO.updateUser(user);
    }


    public void addUser(User user, String[] roles) {

        List<Role> list = new ArrayList<Role>(roles.length);
        for (int i = 0; i < roles.length; i++){
            Role role = roleRepository.findByName(roles[i]);
            list.add(role);
        }

        user.setRoles(list);

        String number = user.getPbxUser().getNumber();
        String password = user.getPassword();

        user.setPbxUser(pbxUserDAO.getUserByNumber(number));
        user.setPassword(passwordEncoder.encode(password));
        log.debug("Password to " + user.getUsername() + " encoded.");
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