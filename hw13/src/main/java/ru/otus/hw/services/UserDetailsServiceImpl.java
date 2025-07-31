package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.UserRoleConverter;
import ru.otus.hw.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserRoleConverter userRoleConverter;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var dbUser = userRepository.findByLogin(username);
        return dbUser.map(user -> new User(user.getLogin(), user.getPassword(),
                userRoleConverter.convertToAuthorities(user.getRole())
        )).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
}
