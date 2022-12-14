package com.kidmain.kopilochka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * /// ERROR ///
 * class org.springframework.orm.jpa.EntityManagerHolder cannot be cast to class org.springframework.orm.hibernate5.SessionHolder
 * /// SOLUTION ///
 * (exclude = HibernateJpaAutoConfiguration.class)
 */

@SpringBootApplication
public class KopilochkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(KopilochkaApplication.class, args);
    }
}