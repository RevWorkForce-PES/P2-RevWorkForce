package com.revature.revworkforce;

import java.util.HashMap;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RevWorkForceApplication {

    public static void main(String[] args) {
    	HashMap serverConf=new HashMap();
		Scanner scan=new Scanner(System.in);
		System.out.println("Enter the port #");
		int port=scan.nextInt();
		serverConf.put("server.port", port);
		serverConf.put("spring.application.name", "ErrorHanding");
		serverConf.put("Spring.application.profile", "Development");
		
		//SpringApplication.run(ErrorHandingApplication.class, args);
		SpringApplication sprapp=new SpringApplication(RevWorkForceApplication.class);
		sprapp.setDefaultProperties(serverConf);
		sprapp.run(args);
		
        SpringApplication.run(RevWorkForceApplication.class, args);
        System.out.println("RevWorkforce Application Started Successfully!");
    }
}