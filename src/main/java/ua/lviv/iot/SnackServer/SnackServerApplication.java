package ua.lviv.iot.SnackServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ua.lviv.iot.SnackServer.controller",
        "ua.lviv.iot.SnackServer.service", "ua.lviv.iot.SnackServer.datastorage",
        "ua.lviv.iot.SnackServer.help"})
public class SnackServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnackServerApplication.class, args);
    }

}
