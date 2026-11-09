package edu.dosw.styleradar.itemcatalogo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ItemCatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemCatalogServiceApplication.class, args);
    }
}
