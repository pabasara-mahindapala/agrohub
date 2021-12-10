package lk.agrohub.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgroHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgroHubApplication.class, args);
    }

}
