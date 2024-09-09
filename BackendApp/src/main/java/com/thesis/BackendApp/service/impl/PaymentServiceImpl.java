package com.thesis.BackendApp.service.impl;

import com.thesis.BackendApp.dtos.PaymentStatisticsDto;
import com.thesis.BackendApp.exceptions.BlockchainException;
import com.thesis.BackendApp.exceptions.EntityNotFoundException;
import com.thesis.BackendApp.exceptions.PaymentException;
import com.thesis.BackendApp.model.Block;
import com.thesis.BackendApp.repository.PaymentRepository;
import com.thesis.BackendApp.model.*;
import com.thesis.BackendApp.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlockchainService blockchainService;

    @Override
    public  List<Payment> findByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    /**
     * Calculates the SHA-256 hash of a Payment object after incrementing the sequence number.
     *
     * @param payment The Payment object to hash
     * @return The calculated hash as a string
     */
    @Override
    public String calculatePaymentHash(Payment payment) {
        payment.setSequence(payment.getSequence()+1); // we increase the sequence to avoid 2 identical transactions having the same hash
        // Generate SHA-256 hash of payment attributes concatenated together as a string
        return BlockchainUtils.generateSHA256(
                BlockchainUtils.getStringFromKey(payment.getSender()) +
                        BlockchainUtils.getStringFromKey(payment.getRecipient()) +
                        payment.getAmount().toString() + payment.getSequence()
        );
    }

    /**
     * Generates an ECDSA signature for a Payment object using the private key of a credit account.
     *
     * @param payment The Payment object to sign
     * @param creditAccount The credit account to use for signing
     */
    @Override
    public void generateSignature(Payment payment, Account creditAccount) {
        String data = BlockchainUtils.getStringFromKey(payment.getSender()) + BlockchainUtils.getStringFromKey(payment.getRecipient()) + payment.getAmount().toString()	;
        payment.setSignature(BlockchainUtils.generateECDSASignature(creditAccount.getPrivateKey(), data));
    }
    /**
     * Verifies the ECDSA signature of a Payment object.
     *
     * @param payment The Payment object to verify
     * @return True if the signature is valid, false otherwise
     */
    @Override
    public boolean verifiySignature(Payment payment) {
        String data = BlockchainUtils.getStringFromKey(payment.getSender()) + BlockchainUtils.getStringFromKey(payment.getRecipient()) + payment.getAmount().toString()	;
        return BlockchainUtils.verifyECDSASignature(payment.getSender(), data, payment.getSignature());
    }



    @Override
    public Payment addPayment(Long userId, Payment payment, Long accountId) {
        LocalDateTime timestamp = LocalDateTime.now();
        payment.setTimestamp(timestamp);
        AppUser userWhoModified = userService.findUserById(userId);

        Account creditAccount = accountService.findAccountById(accountId);
        Account debitAccount = accountService.findAccountById(payment.getDebitAccountId());
        payment.setSender(creditAccount.getPublicKey());
        payment.setRecipient(debitAccount.getPublicKey());
        payment.setTransactionHash(this.calculatePaymentHash(payment));
        this.generateSignature(payment, creditAccount);
        payment.setCreditAccount(creditAccount);
        payment.setOperation("TRANSACTION");
        Balance balance = balanceService.findLastBalanceOfAccount(creditAccount.getId());
        boolean enoughFunds = balance.getAvailableDebit().subtract(balance.getAvailableCredit()).compareTo(payment.getAmount()) >= 0;

        Balance oldBalanceCreditAcc = balanceService.findLastBalanceOfAccount(accountId);
        Balance newBalanceCreditAcc = new Balance();
        newBalanceCreditAcc.setPendingDebit(oldBalanceCreditAcc.getPendingDebit());
        newBalanceCreditAcc.setPendingCredit(oldBalanceCreditAcc.getPendingCredit().add(payment.getAmount()));
        newBalanceCreditAcc.setAvailableCredit(oldBalanceCreditAcc.getAvailableCredit().add(payment.getAmount()));
        newBalanceCreditAcc.setAvailableDebit(oldBalanceCreditAcc.getAvailableDebit() );
        newBalanceCreditAcc.setAccount(creditAccount);
        balanceService.addBalance(userId, newBalanceCreditAcc);

        Balance oldBalanceDebitAcc = balanceService.findLastBalanceOfAccount(payment.getDebitAccountId());
        Balance newBalanceDebitAcc = new Balance();
        newBalanceDebitAcc.setPendingDebit(oldBalanceDebitAcc.getPendingDebit().add(payment.getAmount()) );
        newBalanceDebitAcc.setPendingCredit(oldBalanceDebitAcc.getPendingCredit());
        newBalanceDebitAcc.setAvailableCredit(oldBalanceDebitAcc.getAvailableCredit());
        newBalanceDebitAcc.setAvailableDebit(oldBalanceDebitAcc.getAvailableDebit());
        newBalanceDebitAcc.setAccount(debitAccount);
        balanceService.addBalance(userId, newBalanceDebitAcc);
        paymentRepository.save(payment);


        if(this.verifiySignature(payment)) {
            if(enoughFunds) {
                if(!Objects.equals(creditAccount.getStatus(), "APPROVE")) {
                    if(!Objects.equals(debitAccount.getStatus(), "APPROVE")) {
                        if (!Objects.equals(creditAccount.getAccountStatus(), "BLOCKED CREDIT")) {
                            if (!Objects.equals(debitAccount.getAccountStatus(), "BLOCKED DEBIT")) {
                                if (!Objects.equals(debitAccount.getAccountStatus(), "BLOCKED")) {
                                    if (!Objects.equals(creditAccount.getAccountStatus(), "BLOCKED")) {
                                        if (!Objects.equals(creditAccount.getAccountStatus(), "CLOSED")) {
                                            if (!Objects.equals(debitAccount.getAccountStatus(), "CLOSED")) {
                                                if (!Objects.equals(debitAccount.getStatus(), "REMOVED")) {
                                                    if (!Objects.equals(debitAccount.getStatus(), "REJECTED")) {
                                                        if (!Objects.equals(creditAccount.getStatus(), "REJECTED")) {
                                                            if (!Objects.equals(creditAccount.getStatus(), "REMOVED")) {

                                                                Block newBlock;
                                                                if (this.blockchainService.getLastBlock() == null) {
                                                                    newBlock = new Block(payment.toString(), "0");
                                                                } else {
                                                                    newBlock = new Block(payment.toString(), this.blockchainService.getLastBlock().getHash());
                                                                }
                                                                payment.setBlock(newBlock);
                                                                this.blockchainService.addBlock(newBlock);
                                                                this.blockchainService.initializeBlockchain();
                                                                try {
                                                                    this.blockchainService.getBlockchain().isBlockchainValid();
                                                                    payment.setStatus("COMPLETED");
                                                                    payment.setNextstatus("-");

                                                                    Balance oldBalanceCreditAcc2 = balanceService.findLastBalanceOfAccount(payment.getCreditAccount().getId());
                                                                    Balance newBalanceCreditAcc2 = new Balance();
                                                                    newBalanceCreditAcc2.setPendingDebit(oldBalanceCreditAcc2.getPendingDebit());
                                                                    newBalanceCreditAcc2.setPendingCredit(oldBalanceCreditAcc2.getPendingCredit().subtract(payment.getAmount()));
                                                                    newBalanceCreditAcc2.setAvailableCredit(oldBalanceCreditAcc2.getAvailableCredit());
                                                                    newBalanceCreditAcc2.setAvailableDebit(oldBalanceCreditAcc2.getAvailableDebit());
                                                                    newBalanceCreditAcc2.setAccount(payment.getCreditAccount());
                                                                    balanceService.addBalance(userId, newBalanceCreditAcc2);

                                                                    Balance oldBalanceDebitAcc2 = balanceService.findLastBalanceOfAccount(payment.getDebitAccountId());
                                                                    Balance newBalanceDebitAcc2 = new Balance();
                                                                    newBalanceDebitAcc2.setPendingDebit(oldBalanceDebitAcc2.getPendingDebit().subtract(payment.getAmount()));
                                                                    newBalanceDebitAcc2.setPendingCredit(oldBalanceDebitAcc2.getPendingCredit());
                                                                    newBalanceDebitAcc2.setAvailableCredit(oldBalanceDebitAcc2.getAvailableCredit());
                                                                    newBalanceDebitAcc2.setAvailableDebit(oldBalanceDebitAcc2.getAvailableDebit().add(payment.getAmount()));
                                                                    newBalanceDebitAcc2.setAccount(accountService.findAccountById(payment.getDebitAccountId()));
                                                                    balanceService.addBalance(userId, newBalanceDebitAcc2);

                                                                    Payment addedPayment = paymentRepository.save(payment);
                                                                    Audit audit = new Audit(userId, userWhoModified.getUsername(), addedPayment.getId(), "PAYMENT", "ADD", timestamp);
                                                                    auditService.addAudit(audit);
                                                                    return addedPayment;

                                                                } catch (BlockchainException e) {
                                                                    this.rejectPayment(userId, payment);
                                                                    this.blockchainService.removeBlock(newBlock);
                                                                    this.blockchainService.initializeBlockchain();
                                                                    throw (new PaymentException("There has been an error validating the blockchain. Please contact Help Desk. Transaction was rejected."));

                                                                }

                                                            } else {
                                                                this.rejectPayment(userId, payment);
                                                                throw (new PaymentException("Credit account was removed! Transaction rejected."));
                                                            }

                                                        } else {
                                                            this.rejectPayment(userId, payment);
                                                            throw (new PaymentException("Credit account was rejected! Transaction rejected."));
                                                        }

                                                    } else {
                                                        this.rejectPayment(userId, payment);
                                                        throw (new PaymentException("Debit account was rejected! Payment rejected."));
                                                    }

                                                } else {
                                                    this.rejectPayment(userId, payment);
                                                    throw (new PaymentException("Debit account was removed! Transaction was rejected."));
                                                }

                                            } else {
                                                this.rejectPayment(userId, payment);
                                                throw (new PaymentException("Debit account is closed! Transaction was rejected."));
                                            }

                                        } else {
                                            this.rejectPayment(userId, payment);
                                            throw (new PaymentException("Credit account is closed! Transaction was rejected."));
                                        }

                                    } else {
                                        this.rejectPayment(userId, payment);
                                        throw (new PaymentException("Credit account is blocked! Transaction was rejected."));
                                    }

                                } else {
                                    this.rejectPayment(userId, payment);
                                    throw (new PaymentException("Debit account is blocked! Transaction was rejected."));
                                }

                            } else {
                                this.rejectPayment(userId, payment);
                                throw (new PaymentException("Debit account is debit blocked! Transaction was rejected."));
                            }

                        } else {
                            this.rejectPayment(userId, payment);
                            throw (new PaymentException("Credit account is credit blocked! Transaction was rejected."));
                        }
                    } else {
                        this.rejectPayment(userId, payment);
                        throw (new PaymentException("Debit account is in approval state! Transaction was rejected."));
                    }
                } else {
                    this.rejectPayment(userId, payment);
                    throw (new PaymentException("Credit account is in approval state! Transaction was rejected."));
                }
            }
            else {
                this.rejectPayment(userId, payment);
                throw(new PaymentException("Not enough funds in the credit account! Transaction was rejected."));
            }

        } else {
            this.rejectPayment(userId, payment);
            throw(new PaymentException("There has been an error generating the Transaction Signature. Please contact Help Desk. Transaction was rejected."));
        }



    }

    @Override
    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment with id " + id + " was not found"));
    }


    @Override
    public Payment rejectPayment(Long userId, Payment paymentToApprove) {
        LocalDateTime timestamp = LocalDateTime.now();
        Payment payment = this.findById(paymentToApprove.getId());
        AppUser userWhoModified = userService.findUserById(userId);
        Audit audit = new Audit(userId, userWhoModified.getUsername(), payment.getId(), "PAYMENT", "REJECT", timestamp);
        auditService.addAudit(audit);
        payment.setStatus("REJECTED");
        payment.setNextstatus("-");

        Balance oldBalanceCreditAcc = balanceService.findLastBalanceOfAccount(payment.getCreditAccount().getId());
        Balance newBalanceCreditAcc = new Balance();
        newBalanceCreditAcc.setPendingDebit(oldBalanceCreditAcc.getPendingDebit());
        newBalanceCreditAcc.setPendingCredit(oldBalanceCreditAcc.getPendingCredit().subtract(payment.getAmount()));
        newBalanceCreditAcc.setAvailableCredit(oldBalanceCreditAcc.getAvailableCredit().subtract(payment.getAmount()));
        newBalanceCreditAcc.setAvailableDebit(oldBalanceCreditAcc.getAvailableDebit() );
        newBalanceCreditAcc.setAccount(payment.getCreditAccount());
        balanceService.addBalance(userId, newBalanceCreditAcc);

        Balance oldBalanceDebitAcc = balanceService.findLastBalanceOfAccount(payment.getDebitAccountId());
        Balance newBalanceDebitAcc = new Balance();
        newBalanceDebitAcc.setPendingDebit(oldBalanceDebitAcc.getPendingDebit().subtract(payment.getAmount()) );
        newBalanceDebitAcc.setPendingCredit(oldBalanceDebitAcc.getPendingCredit());
        newBalanceDebitAcc.setAvailableCredit(oldBalanceDebitAcc.getAvailableCredit());
        newBalanceDebitAcc.setAvailableDebit(oldBalanceDebitAcc.getAvailableDebit());
        newBalanceDebitAcc.setAccount(accountService.findAccountById(payment.getDebitAccountId()));
        balanceService.addBalance(userId, newBalanceDebitAcc);


        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> findPaymentsOfAccount(Long accountId) {
        List<Payment> payments = paymentRepository.findByCreditAccountIdOrDebitAccountIdOrderById(accountId, accountId);
        return payments;

    }

    @Override
    public List<Payment> findPaymentsOfBlock(Long blockId) {
        List<Payment> payments = paymentRepository.findByBlockId(blockId);
        return payments;

    }

    @Override
    public PaymentStatisticsDto getDebitStatistics(String status, Long accountId) {
        return paymentRepository.getDebitStatistics(status, accountId);
    }

    @Override
    public PaymentStatisticsDto getCreditStatistics(String status, Long accountId) {
        return paymentRepository.getCreditStatistics(status, accountId);
    }


}