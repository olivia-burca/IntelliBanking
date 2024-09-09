package com.thesis.BackendApp.service.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.thesis.BackendApp.exceptions.EntityNotFoundException;
import com.thesis.BackendApp.exceptions.UsernameExistsException;
import com.thesis.BackendApp.exceptions.WrongPassword;
import com.thesis.BackendApp.model.AppUser;
import com.thesis.BackendApp.model.Audit;
import com.thesis.BackendApp.model.UserHistory;
import com.thesis.BackendApp.repository.UserRepository;
import com.thesis.BackendApp.service.AuditService;
import com.thesis.BackendApp.service.UserHistoryService;
import com.thesis.BackendApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryService userHistoryService;

    @Autowired
    private AuditService auditService;


    @Override
    public Optional<AppUser> login2(String username, String password) {
        Optional<AppUser> user = this.findUserByUsername(username);
        if(user.isPresent()) {
            if(Objects.equals(user.get().getUsername(), "admin1") || Objects.equals(user.get().getUsername(), "admin2"))
            {
                if(Objects.equals(user.get().getPassword(), "1234")) {
                    return user;
                }
                else
                    throw (new WrongPassword("Wrong password!"));
            }
            else {
                if (BCrypt.verifyer().verify(password.toCharArray(), user.get().getPassword()).verified)
                    return user;
                else
                    throw (new WrongPassword("Wrong password!"));
            }

        }
        else throw(new EntityNotFoundException("Username does not exist!"));

    }


    @Override
    @Transactional
    public AppUser addUser(Long userId, AppUser user) {
        if(this.findUserByUsername(user.getUsername()).isPresent()) {
            throw(new UsernameExistsException("Username already exists! Cannot add user."));
        }
        else {

            user.setStatus("APPROVE");
            user.setNextstatus("ACTIVE");
            String bcryptHashString = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
            user.setPassword(bcryptHashString);
            AppUser userWhoModified = this.findUserById(userId);
            AppUser newUser = userRepository.save(user);
            LocalDateTime timestamp = LocalDateTime.now();

            UserHistory userHistory = new UserHistory(newUser.getId(), newUser.getUsername(), newUser.getFullname(), newUser.getAddress(), newUser.getPassword(), newUser.getEmail(), newUser.getStatus(), newUser.getNextstatus(), timestamp);
            Audit audit = new Audit(userId, userWhoModified.getUsername(), newUser.getId(), "USER", "ADD", timestamp);
            auditService.addAudit(audit);
            UserHistory uH = userHistoryService.addUserHistory(userHistory);
            userHistoryService.addUserHistory(uH);

            return newUser;
        }
    }

    @Override
    public List<AppUser> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public AppUser updateUser(Long userId, AppUser user) {
        LocalDateTime timestamp = LocalDateTime.now();
        AppUser userWhoModified = this.findUserById(userId);
        AppUser oldUser = this.findUserById(user.getId());
        UserHistory userHistory = new UserHistory(oldUser.getId(), oldUser.getUsername(), oldUser.getFullname(), oldUser.getAddress(),oldUser.getPassword(), oldUser.getEmail(), oldUser.getStatus(), oldUser.getNextstatus(), timestamp);
        userHistoryService.addUserHistory(userHistory);
        Audit audit = new Audit(userId, userWhoModified.getUsername(), user.getId(), "USER", "UPDATE", timestamp);
        auditService.addAudit(audit);
        user.setStatus("APPROVE");
        user.setNextstatus("ACTIVE");

        return userRepository.save(user);
    }

    @Override
    public AppUser findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " was not found"));
    }

    @Override
    @Transactional
    public AppUser deleteUser(Long id, Long userId) {
        AppUser user = this.findUserById(id);
        AppUser userWhoModified = this.findUserById(userId);
        LocalDateTime timestamp = LocalDateTime.now();
        UserHistory userHistory = new UserHistory(user.getId(), user.getUsername(),user.getFullname(), user.getAddress(), user.getPassword(), user.getEmail(), user.getStatus(), user.getNextstatus(), timestamp);
        userHistoryService.addUserHistory(userHistory);
        Audit audit = new Audit(userId,userWhoModified.getUsername(), user.getId(), "USER", "DELETE", timestamp);
        auditService.addAudit(audit);
        user.setStatus("APPROVE");
        user.setNextstatus("REMOVED");
        return userRepository.save(user);
    }
    @Override
    @Transactional
    public AppUser approveUser(Long userId, AppUser user) {
        LocalDateTime timestamp = LocalDateTime.now();
        AppUser userWhoModified = this.findUserById(userId);
        Audit audit = new Audit(userId, userWhoModified.getUsername(), user.getId(), "USER", "APPROVE", timestamp);
        auditService.addAudit(audit);
        UserHistory userHistory = new UserHistory(user.getId(), user.getUsername(), user.getFullname(), user.getAddress(), user.getPassword(), user.getEmail(), user.getStatus(), user.getNextstatus(), timestamp);
        userHistoryService.addUserHistory(userHistory);
        if(Objects.equals(user.getNextstatus(), "ACTIVE"))
        {
            user.setStatus("ACTIVE");
            user.setNextstatus("-");
        }
        else {
            user.setStatus("REMOVED");
            user.setNextstatus("-");
        }
        return userRepository.save(user);
    }
    @Override
    @Transactional
    public AppUser rejectUser(Long userId, AppUser user) {
        AppUser copyUser = user;

        if(Objects.equals(user.getNextstatus(), "REMOVED")) {
            user.setStatus("ACTIVE");
            user.setNextstatus("-");
        }
        else {
            Audit lastAudit = auditService.findLastAuditByObjectIdAndObjectType(user.getId(), "USER");
            if(Objects.equals(lastAudit.getOperation(), "UPDATE")) {
                UserHistory history = userHistoryService.findLastUserHistoryByUserId(user.getId());
                user.setEmail(history.getEmail());
                user.setPassword(history.getPassword());
                user.setFullname(history.getFullname());
                user.setAddress(history.getAddress());
                user.setStatus("ACTIVE");
                user.setNextstatus("-");
            }
            else {
                user.setStatus("REJECTED");
                user.setNextstatus("-");
            }
        }
        LocalDateTime timestamp = LocalDateTime.now();
        UserHistory userHistory = new UserHistory(copyUser.getId(), copyUser.getUsername(),user.getFullname(), user.getAddress(), copyUser.getPassword(), copyUser.getEmail(), copyUser.getStatus(), copyUser.getNextstatus(), timestamp);
        userHistoryService.addUserHistory(userHistory);
        AppUser userWhoModified = this.findUserById(userId);
        Audit audit = new Audit(userId, userWhoModified.getUsername(), copyUser.getId(), "USER", "REJECT", timestamp);
        auditService.addAudit(audit);

        return  userRepository.save(user);
    }

    @Override
    public List<AppUser> findByStatus(String status){
        return userRepository.findByStatus(status);
    }

    @Override
    public Optional<AppUser> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        LocalDateTime timestamp = LocalDateTime.now();
        AppUser user = findUserById(userId);
        UserHistory userHistory = new UserHistory(user.getId(), user.getUsername(), user.getFullname(), user.getAddress(),user.getPassword(), user.getEmail(), user.getStatus(), user.getNextstatus(), timestamp);
        userHistoryService.addUserHistory(userHistory);
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
        user.setPassword(bcryptHashString);
        Audit audit = new Audit(userId, user.getUsername(), user.getId(), "USER", "CHANGE PASSWORD", timestamp);
        auditService.addAudit(audit);
        userRepository.save(user);
    }

}
