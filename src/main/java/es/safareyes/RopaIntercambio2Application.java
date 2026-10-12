package es.safareyes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "es.safareyes.Interfaces")
@EntityScan(basePackages = "es.safareyes.modelos")
public class RopaIntercambio2Application {

    public static void main(String[] args) {
        SpringApplication.run(RopaIntercambio2Application.class, args);
    }

}


