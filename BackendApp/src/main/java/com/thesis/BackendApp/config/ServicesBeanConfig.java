package com.thesis.BackendApp.config;




import com.thesis.BackendApp.service.*;
import com.thesis.BackendApp.service.impl.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class ServicesBeanConfig {

    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl();
    }

    @Bean
    public BalanceService balanceService() {
        return new BalanceServiceImpl();
    }

    @Bean
    public PaymentService paymentService() {
        return new PaymentServiceImpl();
    }


    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public UserHistoryService userHistoryService() {
        return new UserHistoryServiceImpl();
    }

    @Bean
    public AuditService auditService() {
        return new AuditServiceImpl();
    }

    @Bean
    public BlockchainService blockchainService() {
        return new BlockchainServiceImpl();
    }


    @Bean
    public AccountHistoryService accountHistoryService() {
        return new AccountHistoryServiceImpl();
    }

    @Bean
    public BalanceHistoryService balanceHistoryService() {
        return new BalanceHistoryServiceImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }




}

