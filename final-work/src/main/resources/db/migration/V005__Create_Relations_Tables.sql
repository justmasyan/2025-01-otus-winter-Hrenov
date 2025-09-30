CREATE TABLE courses_topics (
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    topic_id BIGINT REFERENCES lecture_topics(id) ON DELETE CASCADE,
    PRIMARY KEY (course_id, topic_id)
);

CREATE TABLE courses_lectors (
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    lector_id BIGINT REFERENCES lectors(id) ON DELETE CASCADE,
    PRIMARY KEY (course_id, lector_id)
);

CREATE TABLE groups_students (
    group_id BIGINT REFERENCES study_groups(id) ON DELETE CASCADE,
    student_id BIGINT REFERENCES students(id) ON DELETE CASCADE,
    PRIMARY KEY (group_id, student_id)
);

CREATE TABLE homeworks_messages (
    homework_id BIGINT REFERENCES homeworks(id) ON DELETE CASCADE,
    message_id BIGINT REFERENCES messages(id) ON DELETE CASCADE,
    PRIMARY KEY (homework_id, message_id)
);

CREATE TABLE lectures_materials (
    lecture_id BIGINT REFERENCES lectures(id) ON DELETE CASCADE,
    material_id BIGINT REFERENCES lecture_materials(id) ON DELETE CASCADE,
    PRIMARY KEY (lecture_id, material_id)
);