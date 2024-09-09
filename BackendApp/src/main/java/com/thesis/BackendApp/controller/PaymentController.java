package com.thesis.BackendApp.controller;

import com.thesis.BackendApp.converter.BlockConverter;
import com.thesis.BackendApp.converter.PaymentConverter;
import com.thesis.BackendApp.dtos.*;
import com.thesis.BackendApp.exceptions.BlockchainException;
import com.thesis.BackendApp.exceptions.PaymentException;
import com.thesis.BackendApp.model.Payment;
import com.thesis.BackendApp.model.Block;
import com.thesis.BackendApp.service.BlockchainService;
import com.thesis.BackendApp.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private PaymentConverter paymentConverter;

    @Autowired
    private BlockConverter blockConverter;

    @GetMapping("/payments/{id}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsOfAccount (@PathVariable("id") Long id) {

        List<Payment> payments = paymentService.findPaymentsOfAccount(id);
        PaymentsDto paymentsDto = new PaymentsDto(paymentConverter.convertModelsToDTOs(payments));
        return new ResponseEntity<>(paymentsDto.getPayments(), HttpStatus.OK);

    }

    @GetMapping("/payments/status/{status}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(@PathVariable("status") String status) {
        List<Payment> payments = paymentService.findByStatus(status);
        PaymentsDto paymentsDto = new PaymentsDto(paymentConverter.convertModelsToDTOs(payments));
        return new ResponseEntity<>(paymentsDto.getPayments(), HttpStatus.OK);
    }

    @PostMapping("/payments/add")
    public ResponseEntity addPayment(@RequestParam("userId") Long userId, @RequestParam("accountId") Long accountId, @RequestBody PaymentDTO payment) {
        try {
            Payment newPayment = paymentService.addPayment(userId, paymentConverter.convertDtoToModel(payment), accountId);
            return new ResponseEntity<>(paymentConverter.convertModelToDto(newPayment), HttpStatus.CREATED);
        }
        catch (PaymentException | BlockchainException e) {
            return  ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }

    }


    @PutMapping("/payments/reject")
    public ResponseEntity<PaymentDTO> rejectPayment(@RequestParam("userId") Long userId, @RequestBody PaymentDTO paymentDto) {
        Payment rejectedPayment = paymentService.rejectPayment(userId, paymentConverter.convertDtoToModel(paymentDto));
        return new ResponseEntity<>(paymentConverter.convertModelToDto(rejectedPayment), HttpStatus.OK);

    }


    @GetMapping("/payments/debit-statistics/{status}/{id}")
    public ResponseEntity<PaymentStatisticsDto> getDebitStatistics(@PathVariable("status") String status, @PathVariable("id") Long id) {
        PaymentStatisticsDto paymentStatistics = paymentService.getDebitStatistics(status,id);
        return new ResponseEntity<>(paymentStatistics, HttpStatus.OK);
    }

    @GetMapping("/payments/credit-statistics/{status}/{id}")
    public ResponseEntity<PaymentStatisticsDto> getCreditStatistics(@PathVariable("status") String status, @PathVariable("id") Long id) {
        PaymentStatisticsDto paymentStatistics = paymentService.getCreditStatistics(status,id);
        return new ResponseEntity<>(paymentStatistics, HttpStatus.OK);
    }


    @GetMapping("/blocks")
    public ResponseEntity<List<BlockDTO>> getAllBlocks() {

        List<Block> blocks = blockchainService.findAllBlocks();
        BlocksDTO blocksDto = new BlocksDTO(blockConverter.convertModelsToDTOs(blocks));
        return new ResponseEntity<>(blocksDto.getBlocks(), HttpStatus.OK);

    }

    @GetMapping("/blocks/transactions/{id}")
    public ResponseEntity<List<PaymentDTO>> getTransactionsOfBlock(@PathVariable("id") Long id) {

        List<Payment> payments = paymentService.findPaymentsOfBlock(id);
        PaymentsDto paymentsDto = new PaymentsDto(paymentConverter.convertModelsToDTOs(payments));
        return new ResponseEntity<>(paymentsDto.getPayments(), HttpStatus.OK);

    }


}
