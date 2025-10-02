package db.data;

import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.AccountRoleDto;
import ru.otus.project.dto.CourseDto;
import ru.otus.project.dto.HomeworkDto;
import ru.otus.project.dto.HomeworkMessageDto;
import ru.otus.project.dto.HomeworkStatusDto;
import ru.otus.project.dto.LectorDto;
import ru.otus.project.dto.LectureDto;
import ru.otus.project.dto.LectureMaterialDto;
import ru.otus.project.dto.LectureStatusDto;
import ru.otus.project.dto.LectureTopicDto;
import ru.otus.project.dto.StudentDto;
import ru.otus.project.dto.StudyGroupDto;
import ru.otus.project.enums.HomeworkStatusEnum;
import ru.otus.project.enums.LectureStatusEnum;
import ru.otus.project.utils.RandomUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static db.data.DataDictionary.DEFAULT_COURSE_TITLES;
import static db.data.DataDictionary.DEFAULT_FIRST_NAMES;
import static db.data.DataDictionary.DEFAULT_HOMEWORK_DISCUSSION;
import static db.data.DataDictionary.DEFAULT_JOBS;
import static db.data.DataDictionary.DEFAULT_LAST_NAMES;
import static db.data.DataDictionary.DEFAULT_LECTURE_MATERIALS;
import static db.data.DataDictionary.DEFAULT_LECTURE_TOPICS;
import static db.data.DataDictionary.DEFAULT_STUDY_PLACES;
import static db.data.DataDictionary.DEFAULT_USERNAMES;
import static ru.otus.project.utils.RandomUtils.getManyRandomFrom;
import static ru.otus.project.utils.RandomUtils.getOneRandomFrom;
import static ru.otus.project.utils.RandomUtils.nextInt;

public class MigrationDataStorage {

    private final PasswordEncoder passwordEncoder;

    public MigrationDataStorage(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        prepareDomains();
    }

    private void prepareDomains() {
        prepareRoles();
        prepareLectureTopics();
        prepareLectureStatuses();
        prepareHomeworkStatuses();

        prepareCourses();
        prepareAccounts();
        prepareStudents();
        prepareLectors();
        insertCoursesToLectors();

        prepareStudyGroups();
        prepareLectures();
        prepareLectureMaterials();
        prepareHomeworks();
        prepareHomeworkMessages();
    }

    private void prepareLectureTopics() {
        for (int i = 1; i <= 30; i++) {
            LECTURE_TOPICS.add(new LectureTopicDto(i, getOneRandomFrom(DEFAULT_LECTURE_TOPICS)));
        }
    }

    private void prepareRoles() {
        ACCOUNT_ROLES.add(new AccountRoleDto(1, "Студент", "ROLE_STUDENT"));
        ACCOUNT_ROLES.add(new AccountRoleDto(2, "Преподаватель", "ROLE_LECTOR"));
    }

    private void prepareLectureStatuses() {
        Arrays.stream(LectureStatusEnum.values()).forEach(status ->
                LECTURE_STATUSES.add(new LectureStatusDto(status.id, status.text))
        );
    }

    private void prepareHomeworkStatuses() {
        Arrays.stream(HomeworkStatusEnum.values()).forEach(status ->
                HOMEWORK_STATUSES.add(new HomeworkStatusDto(status.id, status.text))
        );
    }

    private void prepareAccounts() {
        ACCOUNTS.add(new AccountDto(1, "STUDENT", passwordEncoder.encode("STUDENT"), ACCOUNT_ROLES.get(0)));
        ACCOUNTS.add(new AccountDto(2, "LECTOR", passwordEncoder.encode("LECTOR"), ACCOUNT_ROLES.get(1)));

        for (int i = 3; i <= DEFAULT_USERNAMES.size() + 2; i++) {
            String username = DEFAULT_USERNAMES.get(i - 3);
            ACCOUNTS.add(new AccountDto(i, username, passwordEncoder.encode(username), getOneRandomFrom(ACCOUNT_ROLES)));
        }
    }

    private void prepareLectors() {
        int lectorId = 1;
        for (AccountDto accountDto : ACCOUNTS) {
            if ("ROLE_LECTOR".equals(accountDto.getAuthority().getAuthority())) {
                LECTORS.add(new LectorDto(lectorId++,
                        getOneRandomFrom(DEFAULT_FIRST_NAMES), getOneRandomFrom(DEFAULT_LAST_NAMES),
                        getOneRandomFrom(DEFAULT_JOBS),
                        accountDto,
                        new ArrayList<>()));
            }
        }
    }

    private void insertCoursesToLectors() {
        COURSES.forEach(courseDto -> getManyRandomFrom(LECTORS)
                .forEach(lector -> lector.getCourses().add(courseDto))
        );
    }

    private void prepareStudents() {
        int studentId = 1;
        for (AccountDto accountDto : ACCOUNTS) {
            if ("ROLE_STUDENT".equals(accountDto.getAuthority().getAuthority())) {
                STUDENTS.add(new StudentDto(studentId++,
                        getOneRandomFrom(DEFAULT_FIRST_NAMES), getOneRandomFrom(DEFAULT_LAST_NAMES),
                        getOneRandomFrom(DEFAULT_STUDY_PLACES), accountDto));
            }
        }
    }

    private void prepareCourses() {
        for (int i = 1; i <= DEFAULT_COURSE_TITLES.size(); i++) {
            String title = DEFAULT_COURSE_TITLES.get(i - 1);
            List<LectureTopicDto> topics = getManyRandomFrom(LECTURE_TOPICS);
            COURSES.add(new CourseDto(i, title, getPriceForCourses(), topics));
        }
    }

    private void prepareStudyGroups() {
        Calendar calendar = Calendar.getInstance();
        prepareCalendar(calendar);
        COURSES.forEach(courseDto -> {
            Date startDate = calendar.getTime();
            String codeGroup = "%s: %s".formatted(courseDto.getTitle(),
                    new SimpleDateFormat("dd-MM-yyyy").format(startDate));

            STUDY_GROUPS.add(new StudyGroupDto(
                    courseDto.getId(),
                    codeGroup,
                    startDate,
                    courseDto,
                    getManyRandomFrom(STUDENTS)
            ));
            calendar.add(Calendar.MONTH, 1);
        });
    }

    private void prepareLectures() {
        int lectureId = 1;

        for (StudyGroupDto groupDto : STUDY_GROUPS) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(groupDto.getStartDate());
            List<LectorDto> lectors = LECTORS.stream()
                    .filter(lector -> lector.getCourses().contains(groupDto.getCourse()))
                    .toList();

            for (LectureTopicDto topic : groupDto.getCourse().getLectureTopics()) {
                LECTURES.add(new LectureDto(
                        lectureId++,
                        calendar.getTime(),
                        getOneRandomFrom(LECTURE_STATUSES),
                        topic,
                        getOneRandomFrom(lectors),
                        groupDto
                ));
                calendar.add(Calendar.DATE, 2);
            }
        }
    }

    private void prepareLectureMaterials() {
        int materialId = 1;
        for (LectureDto lectureDto : LECTURES) {
            List<String> materialUrls = getManyRandomFrom(DEFAULT_LECTURE_MATERIALS);
            for (String url : materialUrls)
                LECTURE_MATERIALS.add(new LectureMaterialDto(materialId++, url, lectureDto));
        }
    }

    private void prepareHomeworks() {
        long homeworkId = 1;
        for (LectureDto lectureDto : LECTURES) {
            List<LectorDto> lectors = LECTORS.stream().filter(lector ->
                    lector.getCourses().contains(lectureDto.getStudyGroup().getCourse())
            ).toList();

            for (StudentDto studentDto : lectureDto.getStudyGroup().getStudents()) {
                LectorDto lectorDto = getOneRandomFrom(lectors);
                HOMEWORKS.add(new HomeworkDto(homeworkId++,
                        nextInt(10) + 1,
                        getOneRandomFrom(HOMEWORK_STATUSES),
                        lectureDto,
                        studentDto,
                        lectorDto));
            }
        }
    }

    private void prepareHomeworkMessages() {
        long messageId = 1;
        Calendar calendar = Calendar.getInstance();
        prepareCalendar(calendar);

        for (HomeworkDto homeworkDto : HOMEWORKS) {
            List<String> strMessages = getOneRandomFrom(DEFAULT_HOMEWORK_DISCUSSION);
            List<HomeworkMessageDto> messages = new ArrayList<>();
            for (int i = 1; i <= strMessages.size(); i++) {
                AccountDto sender;
                String senderDesc;

                if (i % 2 != 0) {
                    StudentDto studentDto = homeworkDto.getStudent();
                    sender = studentDto.getAccount();
                    senderDesc = "%s %s".formatted(studentDto.getFirstName(), studentDto.getLastName());
                } else {
                    LectorDto lectorDto = homeworkDto.getLector();
                    sender = lectorDto.getAccount();
                    senderDesc = "%s %s".formatted(lectorDto.getFirstName(), lectorDto.getLastName());
                }
                calendar.add(Calendar.HOUR, 1);

                messages.add(new HomeworkMessageDto(
                        messageId++,
                        strMessages.get(i - 1),
                        calendar.getTime(),
                        senderDesc,
                        sender,
                        homeworkDto
                ));
            }
            HOMEWORK_MESSAGES.addAll(messages);
        }
    }

    private void prepareCalendar(Calendar calendar) {
        calendar.add(Calendar.DATE, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private BigDecimal getPriceForCourses() {
        return new BigDecimal(1000).multiply(new BigDecimal(RandomUtils.nextInt(100) + 1));
    }

    @Getter
    private static final List<AccountRoleDto> ACCOUNT_ROLES = new ArrayList<>();
    @Getter
    private static final List<AccountDto> ACCOUNTS = new ArrayList<>();
    @Getter
    private static final List<StudentDto> STUDENTS = new ArrayList<>();
    @Getter
    private static final List<LectorDto> LECTORS = new ArrayList<>();
    @Getter
    private static final List<LectureTopicDto> LECTURE_TOPICS = new ArrayList<>();
    @Getter
    private static final List<CourseDto> COURSES = new ArrayList<>();
    @Getter
    private static final List<StudyGroupDto> STUDY_GROUPS = new ArrayList<>();
    @Getter
    private static final List<LectureStatusDto> LECTURE_STATUSES = new ArrayList<>();
    @Getter
    private static final List<LectureMaterialDto> LECTURE_MATERIALS = new ArrayList<>();
    @Getter
    private static final List<LectureDto> LECTURES = new ArrayList<>();
    @Getter
    private static final List<HomeworkDto> HOMEWORKS = new ArrayList<>();
    @Getter
    private static final List<HomeworkMessageDto> HOMEWORK_MESSAGES = new ArrayList<>();
    @Getter
    private static final List<HomeworkStatusDto> HOMEWORK_STATUSES = new ArrayList<>();
}
