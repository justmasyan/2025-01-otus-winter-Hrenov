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