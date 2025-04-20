package com.demo.iot.config;

import com.demo.iot.entity.Account;
import com.demo.iot.entity.Role;
import com.demo.iot.repository.IAccountRepository;
import com.demo.iot.repository.IRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CreatedAccountConfig {
    PasswordEncoder passwordEncoder;
    IRoleRepository roleRepository;

    @Bean
    ApplicationRunner createAccount(IAccountRepository accountRepository) {
        return args -> {
            if(accountRepository.findByUsername("hanhnv").isEmpty()) {
                Optional<Role> roleOptional = roleRepository.findByName("admin");
                Role role = roleOptional.orElseGet(() -> roleRepository.save(Role.builder().name("admin").status(1).build()));
                Account account = Account.builder()
                        .username("hanhnv")
                        .password(passwordEncoder.encode("123456"))
                        .role(role)
                        .status(1)
                        .build();
                accountRepository.save(account);
                log.info("Created account successfully: {}", account.getUsername());
            }
        };
    }
}
