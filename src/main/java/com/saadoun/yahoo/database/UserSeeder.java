package com.saadoun.yahoo.database;


import com.saadoun.yahoo.model.entity.User;
import com.saadoun.yahoo.repository.UserRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSeeder implements CommandLineRunner {

    @Autowired
    UserRepositoryInterface userRepositoryInterface;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // prevent duplicate seeding
        if (userRepositoryInterface.count() > 0) {
            return;
        }

        List<User> users = new ArrayList<>();

        users.add(User.builder()
                .first_name("Mostafa")
                .last_name("Saadoun")
                .username("Mostafa_sadoon")
                .email("sadoonmosta6@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build());

        users.add(User.builder()
                .first_name("Ahmed")
                .last_name("Saadoun")
                .username("Ahmed_sadoon")
                .email("sadoonAhmed6@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build());

        users.add(User.builder()
                .first_name("Mohamed")
                .last_name("Saadoun")
                .username("Mohamed_sadoon")
                .email("sadoonMohamed6@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build());


        users.add(User.builder()
                .first_name("Hamza")
                .last_name("Saadoun")
                .username("Hamza_sadoon")
                .email("sadoonHamza6@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build());


        users.add(User.builder()
                .first_name("Nada")
                .last_name("Saadoun")
                .username("Nada_sadoon")
                .email("sadoonNada6@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build());

        userRepositoryInterface.saveAll(users);

        System.out.println("✅ Seeded 10 users");
    }
}
