package com.revature.revworkforce;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RevWorkForceApplication {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the port number:");
        int port = scan.nextInt();

        Map<String, Object> serverConfig = new HashMap<>();
        serverConfig.put("server.port", port);
        serverConfig.put("spring.application.name", "RevWorkForce");
        serverConfig.put("spring.profiles.active", "development");

        SpringApplication app = new SpringApplication(RevWorkForceApplication.class);
        app.setDefaultProperties(serverConfig);
        app.run(args);

        System.out.println("RevWorkforce Application Started Successfully on port " + port);
    }
}