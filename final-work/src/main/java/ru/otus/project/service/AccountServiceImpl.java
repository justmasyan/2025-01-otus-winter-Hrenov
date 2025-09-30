package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.AccountConverter;
import ru.otus.project.domain.Account;
import ru.otus.project.domain.AccountRole;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.repository.AccountRepository;
import ru.otus.project.repository.AccountRoleRepository;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final AccountConverter accountConverter;

    private final AccountRepository accountRepository;

    private final AccountRoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public AccountDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username).map(accountConverter::entityToDto)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Transactional
    public AccountDto insert(AccountDto accountDto) {
        if (accountDto.getId() != 0) {
            throw new RuntimeException("При добавлении аккаунта id == 0");
        }

        return save(accountDto);
    }

    private AccountDto save(AccountDto accountDto) {
        AccountRole role = roleRepository.findById(accountDto.getAuthority().getId())
                .orElseThrow(() -> new RuntimeException("Нет такой роли"));

        Account account = accountRepository.save(new Account(
                accountDto.getId(),
                accountDto.getUsername(),
                passwordEncoder.encode(accountDto.getPassword()),
                role
        ));

        return accountConverter.entityToDto(account);
    }
}
