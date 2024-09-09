package com.thesis.BackendApp.service;

import com.thesis.BackendApp.model.Audit;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditService {

    Audit addAudit(Audit audit);

    List<Audit> findAllAudit();

    List<Audit> findByObjectIdAndObjectType(Long id, String objectType);

    Audit findLastAuditByObjectIdAndObjectType(Long objectId, String objectType);

    Audit findByTimestamp(LocalDateTime timestamp);

}
