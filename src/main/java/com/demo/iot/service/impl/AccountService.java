package com.demo.iot.service.impl;


import com.demo.iot.dto.request.AccountRequest;
import com.demo.iot.dto.response.AccountResponse;
import com.demo.iot.entity.Account;
import com.demo.iot.entity.Role;
import com.demo.iot.exception.AlreadyExitException;
import com.demo.iot.exception.NotFoundException;
import com.demo.iot.mapper.AccountMapper;
import com.demo.iot.repository.IAccountRepository;
import com.demo.iot.repository.IRoleRepository;
import com.demo.iot.service.IAccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService implements IAccountService {
    IAccountRepository accountRepository;
    AccountMapper accountMapper;
    PasswordEncoder passwordEncoder;
    IRoleRepository roleRepository;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        if(accountRepository.findByUsername(accountRequest.getUsername()).isPresent()) {
            throw new AlreadyExitException("Account already exists");
        }
        if(accountRepository.findByEmail(accountRequest.getUsername()).isPresent()) {
            throw new AlreadyExitException("Account already exists");
        }
        if (accountRepository.findByPhone(accountRequest.getUsername()).isPresent()) {
            throw new AlreadyExitException("Account already exists");
        }
        Account account = accountMapper.toAccount(accountRequest);
        accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public Page<AccountResponse> findAccount(String username, Pageable pageable) {
        if (username == null || username.isEmpty()) {
            return accountRepository.findAllWithStatus(pageable).map(accountMapper::toAccountResponse);
        } else {
            return accountRepository.findAccount(username, pageable)
                    .map(accountMapper::toAccountResponse);
        }
    }

    @Override
    public void deleteAccount(List<Integer> ids) {
        List<Account> accounts = accountRepository.findAllById(ids);
        for(Account account : accounts) {
            account.setStatus(0);
            accountRepository.save(account);
        }
    }

    @Override
    public AccountResponse updateAccount(Integer id, AccountRequest accountRequest) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found")
        );
        accountRepository.findByUsername(accountRequest.getUsername()).ifPresent(existingAccount -> {
            if (!existingAccount.getId().equals(id)) {
                throw new AlreadyExitException("Username already exists");
            }
        });
        accountRepository.findByEmail(accountRequest.getEmail()).ifPresent(existingAccount -> {
            if (!existingAccount.getId().equals(id)) {
                throw new AlreadyExitException("Email already exists");
            }
        });
        accountRepository.findByPhone(accountRequest.getPhone()).ifPresent(existingAccount -> {
            if (!existingAccount.getId().equals(id)) {
                throw new AlreadyExitException("Phone already exists");
            }
        });
        account.setUsername(accountRequest.getUsername());
        account.setFullName(accountRequest.getFullName());
        account.setEmail(accountRequest.getEmail());
        account.setPhone(accountRequest.getPhone());
        account.setAddress(accountRequest.getAddress());
        account.setGender(accountRequest.getGender());
        Optional<Role> role = roleRepository.findByName(accountRequest.getRole());
        if (role.isEmpty()) {
            throw new NotFoundException("Role not found");
        }
        if (accountRequest.getPassword() != null && !accountRequest.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(accountRequest.getPassword());
            account.setPassword(encodedPassword);
        }
        account.setRole(role.get());
        account.setAvatar(accountRequest.getAvatar());
        account.setDateOfBirth(accountRequest.getDateOfBirth());
        accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }


    @Override
    public AccountResponse getAccount(Integer id) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found")
        );
        return accountMapper.toAccountResponse(account);
    }
}
