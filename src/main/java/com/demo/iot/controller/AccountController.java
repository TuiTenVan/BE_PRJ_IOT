package com.demo.iot.controller;

import com.demo.iot.dto.request.AccountRequest;
import com.demo.iot.dto.response.AccountResponse;
import com.demo.iot.dto.response.ApiResponse;
import com.demo.iot.service.IAccountService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    IAccountService accountService;

    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_ACCOUNT')")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        AccountResponse accountResponse = accountService.createAccount(accountRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(accountResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ALL_ACCOUNTS')")
    public ResponseEntity<?> getAllAccount(@Valid @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                           @RequestParam(value = "username", required = false) String username) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AccountResponse> accountResponses = accountService.findAccount(username, pageable);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(accountResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_ACCOUNT')")
    public ResponseEntity<?> updateAccount(@Valid @PathVariable Integer id,
                                           @RequestBody AccountRequest accountRequest) {
        AccountResponse accountResponse = accountService.updateAccount(id, accountRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(accountResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ACCOUNT')")
    public ResponseEntity<?> getAccount(@PathVariable Integer id) {
        AccountResponse accountResponse = accountService.getAccount(id);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(accountResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_ACCOUNTS')")
    public ResponseEntity<?> deleteAccount(@PathVariable List<Integer> ids) {
        accountService.deleteAccount(ids);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}