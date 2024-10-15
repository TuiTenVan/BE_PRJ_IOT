package com.defty.iot.security;

import com.defty.iot.entity.Account;
import com.defty.iot.entity.Permission;
import com.defty.iot.repository.IPermissionRepository;
import com.defty.iot.service.impl.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequiredPermission {
    AccountService accountService;
    IPermissionRepository permissionRepository;

    public boolean checkPermission(String permissionCheck){
        Optional<Account> account = accountService.getCurrentAccount();
        if(account.isPresent()){
            Set<Permission> permissions = permissionRepository.findPermissionsByRoleId(account.get().getRole().getId());
            for(Permission permission : permissions){
                if (permission.getName().equals(permissionCheck)){
                    return true;
                }
            }
        }
        return false;
    }
}
