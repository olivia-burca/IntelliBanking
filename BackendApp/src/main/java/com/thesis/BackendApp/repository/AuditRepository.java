package com.thesis.BackendApp.repository;

import com.thesis.BackendApp.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditRepository extends JpaRepository<Audit, Long> {
    List<Audit> findByObjectId(Long objectId);

    @Query(nativeQuery = true, value = "SELECT * FROM audit a WHERE a.object_id = ?1 AND a.object_type = ?2 ORDER BY a.id DESC LIMIT 1")
    Audit findTopByObjectIdAndObjectTypeOrderByIdDesc(Long objectId, String objectType);

    List<Audit> findByObjectIdAndObjectType(Long id, String objectType);

    Audit findByTimestamp(LocalDateTime timestamp);
}
