package com.thesis.BackendApp.controller;

import com.thesis.BackendApp.model.Audit;
import com.thesis.BackendApp.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping("/audit/{objectType}/{objectid}")
    public ResponseEntity<List<Audit>> getAuditByObjectId (@PathVariable("objectType") String objectType, @PathVariable("objectid") Long objectId) {
        List<Audit> audit = auditService.findByObjectIdAndObjectType(objectId, objectType);
        return new ResponseEntity<>(audit, HttpStatus.OK);
    }

    @GetMapping("/audit/last/{objectType}/{objectid}")
    public ResponseEntity<Audit> getLastAuditByObjectId (@PathVariable("objectType") String objectType, @PathVariable("objectid") Long objectId) {
        Audit audit = auditService.findLastAuditByObjectIdAndObjectType(objectId, objectType);
        return new ResponseEntity<>(audit, HttpStatus.OK);
    }

    @GetMapping("/audit/timestamp/{timestamp}")
    public ResponseEntity<Audit> getAuditByTimestamp (@PathVariable("timestamp") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        Audit audit = auditService.findByTimestamp(timestamp);
        return new ResponseEntity<>(audit, HttpStatus.OK);
    }

}
