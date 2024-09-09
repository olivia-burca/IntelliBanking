package com.thesis.BackendApp.service;

import com.thesis.BackendApp.model.UserHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface UserHistoryService {

    UserHistory addUserHistory(UserHistory userHistory);

    List<UserHistory> findAllUserHistory();

    List<UserHistory> findByUserId(Long id);

    UserHistory findLastUserHistoryByUserId(Long userId);

    UserHistory findByTimestamp(LocalDateTime timestamp);

}
