package com.thesis.BackendApp.service.impl;

import com.thesis.BackendApp.model.UserHistory;
import com.thesis.BackendApp.repository.UserHistoryRepository;
import com.thesis.BackendApp.service.UserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class UserHistoryServiceImpl implements UserHistoryService {
    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Override
    public UserHistory addUserHistory(UserHistory userHistory) {
        return userHistoryRepository.save(userHistory);
    }

    @Override
    public List<UserHistory> findAllUserHistory() {
        return userHistoryRepository.findAll();
    }

    @Override
    public List<UserHistory> findByUserId(Long id) {
        return userHistoryRepository.findByUserId(id);
    }

    @Override
    public UserHistory findLastUserHistoryByUserId(Long userId) { return userHistoryRepository.findTopByUserIdOrderByIdDesc(userId); }

    @Override
    public UserHistory findByTimestamp(LocalDateTime timestamp) { return userHistoryRepository.findByTimestamp(timestamp); }

}
