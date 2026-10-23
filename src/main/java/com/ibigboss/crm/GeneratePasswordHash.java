package com.ibigboss.crm;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePasswordHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Ike290692";
        String hashed = encoder.encode(rawPassword);
        System.out.println("Hash for 'Ike290692': " + hashed);
        
        boolean matches = encoder.matches(rawPassword, hashed);
        System.out.println("Matches: " + matches);
    }
}
