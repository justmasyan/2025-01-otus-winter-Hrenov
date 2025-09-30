package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.AccountRoleDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountRoleController {

    private final AccountRoleServiceImpl roleService;

    @GetMapping("api/roles")
    public List<AccountRoleDto> findAll(){
        return roleService.findAll();
    }
}
