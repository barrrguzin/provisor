package ru.ptkom.provisor.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.models.Role;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.repository.RoleRepository;
import ru.ptkom.provisor.repository.UserRepository;
import ru.ptkom.provisor.service.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class InitialConfiguration {

    @Autowired
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService){
        this.userService=userService;
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


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


}