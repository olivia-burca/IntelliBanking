package com.thesis.BackendApp.config;

import com.thesis.BackendApp.model.JPAEntitiesPackageIndicator;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan(basePackageClasses = {JPAEntitiesPackageIndicator.class
})
@EnableJpaRepositories(basePackages = "com.example.HelloWorld.repository",
        basePackageClasses = {JPAEntitiesPackageIndicator.class
        })
public class EntitiesConfig {
}
