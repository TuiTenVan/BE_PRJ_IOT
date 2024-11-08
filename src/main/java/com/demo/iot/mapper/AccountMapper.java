package com.demo.iot.mapper;

import com.demo.iot.dto.request.AccountRequest;
import com.demo.iot.dto.response.AccountResponse;
import com.demo.iot.entity.Account;
import com.demo.iot.entity.Role;
import com.demo.iot.repository.IRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountMapper {
    ModelMapper modelMapper;
    IRoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public AccountResponse toAccountResponse(Account account) {
        AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
        accountResponse.setRole(account.getRole().getName());
        return accountResponse;
    }

    public Account toAccount(AccountRequest accountRequest) {
        Account account = modelMapper.map(accountRequest, Account.class);
        Optional<Role> role = roleRepository.findByName(accountRequest.getRole());
        role.ifPresent(account::setRole);
        account.setStatus(1);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return account;
    }
}
