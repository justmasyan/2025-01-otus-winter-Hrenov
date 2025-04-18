package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.domain.Student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = StudentServiceImpl.class)
class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @MockitoBean
    private LocalizedIOService ioService;

    @Test
    void simpleUnitTestDetermineCurrentStudent() {
        Student expectedStudent = new Student("Maksim", "Hrenov");

        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn(expectedStudent.firstName());
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn(expectedStudent.lastName());

        Student actualStudent = studentService.determineCurrentStudent();

        assertThat(actualStudent).isEqualTo(expectedStudent);
    }
}