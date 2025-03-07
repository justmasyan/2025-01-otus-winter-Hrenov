package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentServiceImplTest {

    private StudentService studentService;

    private IOService ioService;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        studentService = new StudentServiceImpl(ioService);
    }

    @Test
    void simpleUnitTestDetermineCurrentStudent() {
        Student expectedStudent = new Student("Maksim", "Hrenov");

        when(ioService.readStringWithPrompt("Please input your first name")).thenReturn(expectedStudent.firstName());
        when(ioService.readStringWithPrompt("Please input your last name")).thenReturn(expectedStudent.lastName());

        Student actualStudent = studentService.determineCurrentStudent();

        assertThat(actualStudent).isEqualTo(expectedStudent);
    }
}