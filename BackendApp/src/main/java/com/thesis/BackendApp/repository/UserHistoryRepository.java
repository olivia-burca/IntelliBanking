package com.thesis.BackendApp.repository;

import com.thesis.BackendApp.model.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    List<UserHistory> findByUserId(Long userId);

    UserHistory findTopByUserIdOrderByIdDesc(Long userId);

    UserHistory findByTimestamp(LocalDateTime timestamp);

}
