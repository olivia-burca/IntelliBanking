package com.thesis.BackendApp.controller;

import com.thesis.BackendApp.converter.UserConverter;
import com.thesis.BackendApp.dtos.UserDTO;
import com.thesis.BackendApp.dtos.UsersDto;
import com.thesis.BackendApp.exceptions.EntityNotFoundException;
import com.thesis.BackendApp.exceptions.UsernameExistsException;
import com.thesis.BackendApp.exceptions.WrongPassword;
import com.thesis.BackendApp.model.AppUser;
import com.thesis.BackendApp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserConverter userConverter;
    @Autowired
    private UserService userService;


    @PutMapping("/login")
    public ResponseEntity login2(@RequestBody UserDTO userCredentials) {
        AppUser user = userConverter.convertDtoToModel(userCredentials);
        try {
            Optional<AppUser> loggedUser = this.userService.login2(user.getUsername(), user.getPassword());

            UserDTO userDto = userConverter.convertModelToDto(loggedUser.orElse(null));
            return new ResponseEntity<>(Optional.of(userDto), HttpStatus.OK);

        }
        catch (WrongPassword | EntityNotFoundException e) {
            return  ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }





    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<AppUser> users = userService.findAllUsers();
        UsersDto usersDto = new UsersDto(userConverter.convertModelsToDTOs(users));
        return new ResponseEntity<>(usersDto.getUsers(), HttpStatus.OK);

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById (@PathVariable("id") Long id) {

        AppUser user = userService.findUserById(id);
        UserDTO userDto = userConverter.convertModelToDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername (@PathVariable("username") String username) {

        Optional<AppUser> user = userService.findUserByUsername(username);
        UserDTO userDto = userConverter.convertModelToDto(user.get());
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }

    @PostMapping("/users/add")
    public ResponseEntity addUser(@RequestParam("userId") Long userId, @RequestBody UserDTO user) {
        try {
            AppUser newUser = userService.addUser(userId, userConverter.convertDtoToModel(user));
            return new ResponseEntity<>(userConverter.convertModelToDto(newUser), HttpStatus.CREATED);
        }
        catch (UsernameExistsException e) {
            return  ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }

    }

    @PutMapping("/users/update")
    public ResponseEntity<UserDTO> updateUser(@RequestParam("userId") Long userId, @RequestBody UserDTO user) {

        AppUser updateUser = userService.updateUser(userId, userConverter.convertDtoToModel(user));
        return new ResponseEntity<>(userConverter.convertModelToDto(updateUser), HttpStatus.OK);

    }

    @PutMapping("/users/delete/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {

        AppUser user = userService.deleteUser(id, userId);
        return new ResponseEntity<>(userConverter.convertModelToDto(user), HttpStatus.OK);

    }

    @PutMapping("/users/approve")
    public ResponseEntity<UserDTO> approveUser(@RequestParam("userId") Long userId, @RequestBody UserDTO user) {

        AppUser updateUser = userService.approveUser(userId, userConverter.convertDtoToModel(user));
        return new ResponseEntity<>(userConverter.convertModelToDto(updateUser), HttpStatus.OK);

    }

    @PutMapping("/users/reject")
    public ResponseEntity<UserDTO> rejectUser(@RequestParam("userId") Long userId, @RequestBody UserDTO user) {

        AppUser updateUser = userService.rejectUser(userId, userConverter.convertDtoToModel(user));
        return new ResponseEntity<>(userConverter.convertModelToDto(updateUser), HttpStatus.OK);

    }

    @GetMapping("/users/status/{status}")
    public ResponseEntity<List<UserDTO>> getUsersByStatus(@PathVariable("status") String status) {

        List<AppUser> users = userService.findByStatus(status);
        UsersDto usersDto = new UsersDto(userConverter.convertModelsToDTOs(users));
        return new ResponseEntity<>(usersDto.getUsers(), HttpStatus.OK);

    }

    @PutMapping("users/change-password")
    public ResponseEntity changePassword(@RequestParam("userId") Long userId, @RequestBody String newPassword) {
        userService.changePassword(userId, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

