package org.moonshot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackageClasses = { CommonRoot.class, AuthRoot.class, DomainRoot.class, ExternalRoot.class }
)
public class MoonshotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoonshotApplication.class, args);
    }

}
