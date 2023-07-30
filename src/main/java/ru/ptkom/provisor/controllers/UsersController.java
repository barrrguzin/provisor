package ru.ptkom.provisor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.dao.UserDAO;
import ru.ptkom.provisor.exception.UserNotFoundException;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.service.RequestService;
import ru.ptkom.provisor.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Slf4j
@RestController
public class UsersController {


    private final UserDAO userDAO;
    private final UserService userService;
    private final RequestService requestService;


    public UsersController(UserDAO userDAO, UserService userService, RequestService requestService) {
        this.userDAO = userDAO;
        this.userService = userService;
        this.requestService = requestService;
    }


    @GetMapping(value = "/users", produces="application/json")
    public ResponseEntity<Iterable<User>> usersShow(Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        Iterable<User> users = userDAO.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping(value = "/users/{id}", produces="application/json")
    public ResponseEntity<User> showUserParameters(@PathVariable("id") Long id, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        try {
            User user = userDAO.getUserById(id);
            user.setPassword("");
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("/users/edit")
    public ResponseEntity updateUsersData(@RequestBody User user, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send PATCH request to page: " + request.getRequestURI());
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity deleteUsersData(@PathVariable("id") Long id, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send DELETE request to page: " + request.getRequestURI());
        userDAO.deleteUser(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/users/add")
    public ResponseEntity addUser(@RequestBody User user, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send POST request to page: " + request.getRequestURI());
        userService.addUser(user);
        return ResponseEntity.ok().build();
    }


}