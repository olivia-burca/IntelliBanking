package com.thesis.BackendApp.service.impl;

import com.thesis.BackendApp.model.Audit;
import com.thesis.BackendApp.repository.AuditRepository;
import com.thesis.BackendApp.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class AuditServiceImpl implements AuditService {
    @Autowired
    private AuditRepository auditRepository;

    @Override
    public Audit addAudit(Audit audit) {
        return auditRepository.save(audit);
    }

    @Override
    public List<Audit> findAllAudit() {
        return auditRepository.findAll();
    }

    @Override
    public List<Audit> findByObjectIdAndObjectType(Long id, String objectType) {
        return auditRepository.findByObjectIdAndObjectType(id, objectType);
    }

    @Override
    public Audit findLastAuditByObjectIdAndObjectType(Long objectId, String objectType) { return auditRepository.findTopByObjectIdAndObjectTypeOrderByIdDesc(objectId, objectType); }

    @Override
    public Audit findByTimestamp(LocalDateTime timestamp) { return auditRepository.findByTimestamp(timestamp); }

}
