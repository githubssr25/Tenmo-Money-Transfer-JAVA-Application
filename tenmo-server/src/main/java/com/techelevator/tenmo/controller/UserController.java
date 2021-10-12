package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final UserDao dao;

    public UserController(UserDao dao) {
        this.dao = dao;
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> findAll() {
        return dao.findAll();
    }

    @RequestMapping(path = "/username", method = RequestMethod.GET)
    public User findByUsername(@PathVariable String username) {
        return dao.findByUsername(username);
    }


    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    public int findIdByUsername(@PathVariable String username) {
        return dao.findIdByUsername(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public boolean create(@RequestBody @Valid User user, @PathVariable String username, @PathVariable String password) {
        return dao.create(username, password);
    }



    }
