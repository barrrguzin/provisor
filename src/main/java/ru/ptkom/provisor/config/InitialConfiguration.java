package ru.ptkom.provisor.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.models.Role;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.repository.RoleRepository;
import ru.ptkom.provisor.repository.UserRepository;
import ru.ptkom.provisor.service.UdpSocketService;
import ru.ptkom.provisor.service.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class InitialConfiguration {

    private UserService userService;
    @Autowired
    public void setUserService(UserService userService){
        this.userService=userService;
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UdpSocketService udpSocketService;

    @PostConstruct
    public void setDefaultUser() {

        String username = "barguzin";
        try {
            userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // user does not exist, create a new user and save to the database
            User user = new User();
            user.setUsername(username);
            user.setPassword("$2a$12$qEldoo3nfwRXQyngvsfgk.c7pRanXW7nvYBkW9iOs0sdyCnnlY2l6");
            Collection<Role> roles = new ArrayList<Role>();
            Role role = new Role();
            role.setName("SUPERADMIN");
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        }

        System.out.println("System user set");
    }


    @PostConstruct
    public void setDefaultRoles(){


        if (roleRepository.findByName("ROLE_SUPERADMIN") == null) {
            System.out.println(roleRepository.findByName("ROLE_SUPERADMIN"));
            Role sa = new Role("ROLE_SUPERADMIN");
            roleRepository.save(sa);
        }


        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            Role a = new Role("ROLE_ADMIN");
            roleRepository.save(a);
        }


        if (roleRepository.findByName("ROLE_USER") == null) {
            Role u = new Role("ROLE_USER");
            roleRepository.save(u);
        }


        System.out.println("System roles set");
    }


    @PostConstruct
    public void sipSocket() {
        //udpSocketService.startUdpSocket(5060);
    }
}
