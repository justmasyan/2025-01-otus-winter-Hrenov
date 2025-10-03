package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.AccountConverter;
import ru.otus.project.converter.LectorConverter;
import ru.otus.project.domain.Lector;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.LectorDto;
import ru.otus.project.exception.LectorNotFoundException;
import ru.otus.project.exception.NoZeroIdException;
import ru.otus.project.repository.AccountRoleRepository;
import ru.otus.project.repository.LectorRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectorServiceImpl implements LectorService{

    private final AccountServiceImpl accountService;

    private final LectorRepository lectorRepository;

    private final LectorConverter lectorConverter;

    private final AccountConverter accountConverter;

    @Override
    @Transactional(readOnly = true)
    public LectorDto findByAccountId(long accountId) {
        return lectorRepository.findByAccountId(accountId).map(lectorConverter::entityToDto)
                .orElseThrow(() -> new LectorNotFoundException("Лектор не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LectorDto> findAllByCourseId(long courseId) {
        return lectorRepository.findAllByCoursesId(courseId).stream()
                .map(lectorConverter::entityToBaseInfoDto).toList();
    }

    @Override
    @Transactional
    public LectorDto insert(LectorDto lectorDto) {
        if (lectorDto.getId() != 0) {
            throw new NoZeroIdException("Id должен равняться нулю");
        }

        AccountDto account = accountService.insert(lectorDto.getAccount());
        Lector lector = lectorRepository.save(new Lector(
                lectorDto.getId(),
                lectorDto.getFirstName(),
                lectorDto.getLastName(),
                lectorDto.getJobTitle(),
                accountConverter.dtoToEntity(account),
                new ArrayList<>()
        ));
        return lectorConverter.entityToDto(lector);
    }

    @Override
    @Transactional
    public LectorDto update(LectorDto lectorDto) {
        Lector lector = lectorRepository.findByAccountId(lectorDto.getAccount().getId())
                .orElseThrow(() -> new LectorNotFoundException("Преподаватель не найден"));

        AccountDto account = accountService.update(lectorDto.getAccount());
        Lector newLector = lectorRepository.save(new Lector(
                lector.getId(),
                lectorDto.getFirstName(),
                lectorDto.getLastName(),
                lectorDto.getJobTitle(),
                accountConverter.dtoToEntity(account),
                new ArrayList<>())
        );
        return lectorConverter.entityToDto(newLector);
    }
}
