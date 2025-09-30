package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.LectorConverter;
import ru.otus.project.domain.Account;
import ru.otus.project.domain.Lector;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.LectorDto;
import ru.otus.project.exception.LectorNotFoundException;
import ru.otus.project.exception.NotZeroIdWhenInsertException;
import ru.otus.project.repository.LectorRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectorServiceImpl {

    private final AccountServiceImpl accountService;

    private final LectorRepository lectorRepository;

    private final LectorConverter lectorConverter;

    @Transactional(readOnly = true)
    public LectorDto findByAccountId(long accountId) {
        return lectorRepository.findByAccountId(accountId).map(lectorConverter::entityToDto)
                .orElseThrow(() -> new LectorNotFoundException("Лектор не найден"));
    }

    @Transactional(readOnly = true)
    public List<LectorDto> findAllByCourseId(long courseId) {
        return lectorRepository.findAllByCoursesId(courseId).stream()
                .map(lectorConverter::entityToBaseInfoDto).toList();
    }

    @Transactional
    public LectorDto insert(LectorDto lectorDto) {
        if (lectorDto.getId() != 0 || lectorDto.getAccount().getId() != 0) {
            throw new NotZeroIdWhenInsertException("Id должен равняться нулю");
        }
        return save(lectorDto);
    }

    private LectorDto save(LectorDto lectorDto) {
        AccountDto account = accountService.insert(lectorDto.getAccount());
        Lector lector = lectorRepository.save(new Lector(
                lectorDto.getId(),
                lectorDto.getFirstName(),
                lectorDto.getLastName(),
                lectorDto.getJobTitle(),
                new Account(account.getId()),
                new ArrayList<>()
        ));
        return lectorConverter.entityToDto(lector);
    }
}
