package com.thesis.BackendApp.service;

import com.thesis.BackendApp.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserService {
    AppUser addUser(Long userId, AppUser user);

    List<AppUser> findAllUsers();

    AppUser updateUser(Long userId, AppUser user);

    AppUser findUserById(Long id);

    AppUser deleteUser(Long id, Long userId);

    List<AppUser> findByStatus(String status);

    AppUser approveUser(Long userId, AppUser user);

    AppUser rejectUser(Long userId, AppUser id);

    Optional<AppUser> findUserByUsername(String username);

    Optional<AppUser> login2(String username, String password);

    void changePassword(Long userId, String newPassword);

}
