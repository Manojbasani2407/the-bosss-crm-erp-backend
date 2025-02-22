package com.crm.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrmErpApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrmErpApplication.class, args);
    }
}


//import java.util.Base64;
//import java.security.SecureRandom;
//
//public class GenerateSecretKey {
//    public static void main(String[] args) {
//        byte[] key = new byte[32]; // 256-bit key
//        new SecureRandom().nextBytes(key);
//        String base64Key = Base64.getEncoder().encodeToString(key);
//        System.out.println("🔑 Base64 Secret Key: " + base64Key);
//    }
//}
