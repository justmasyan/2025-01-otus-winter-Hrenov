package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentServiceImplTest {

    private StudentService studentService;

    private LocalizedIOService ioService;

    @BeforeEach
    void setUp() {
        ioService = mock(LocalizedIOService.class);
        studentService = new StudentServiceImpl(ioService);
    }

    @Test
    void simpleUnitTestDetermineCurrentStudent() {
        Student expectedStudent = new Student("Maksim", "Hrenov");

        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn(expectedStudent.firstName());
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn(expectedStudent.lastName());

        Student actualStudent = studentService.determineCurrentStudent();

        assertThat(actualStudent).isEqualTo(expectedStudent);
    }
}