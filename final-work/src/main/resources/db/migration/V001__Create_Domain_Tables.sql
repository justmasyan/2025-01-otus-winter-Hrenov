CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255),
    authority VARCHAR(255) UNIQUE
);

CREATE TABLE homework_statuses (
    id BIGSERIAL PRIMARY KEY,
    text VARCHAR(255) UNIQUE
);

CREATE TABLE lecture_statuses (
    id BIGSERIAL PRIMARY KEY,
    text VARCHAR(255) UNIQUE
);

CREATE TABLE lecture_topics (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) UNIQUE
);

CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role_id BIGINT REFERENCES roles(id)
);

CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    study_place VARCHAR(256),
    account_id BIGINT REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE TABLE lectors (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    job_title VARCHAR(256),
    account_id BIGINT REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    price NUMERIC
);

CREATE TABLE study_groups (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255),
    start_date DATETIME,
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE lectures (
    id BIGSERIAL PRIMARY KEY,
    url VARCHAR(255),
    lecture_time DATETIME,
    status_id tinyint REFERENCES lecture_statuses(id),
    topic_id BIGINT REFERENCES lecture_topics(id) ON DELETE CASCADE,
    lector_id BIGINT REFERENCES lectors(id),
    group_id BIGINT REFERENCES study_groups(id) ON DELETE CASCADE
);

CREATE TABLE lecture_materials (
    id BIGSERIAL PRIMARY KEY,
    url VARCHAR(255),
    lecture_id BIGINT REFERENCES lectures(id) ON DELETE CASCADE
);

CREATE TABLE homeworks (
    id BIGSERIAL PRIMARY KEY,
    mark tinyint,
    status_id tinyint REFERENCES homework_statuses(id),
    lecture_id BIGINT REFERENCES lectures(id) ON DELETE CASCADE,
    lector_id BIGINT REFERENCES lectors(id),
    student_id BIGINT REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    send_time DATETIME,
    text VARCHAR(256),
    sender_desc VARCHAR(256),
    sender_id BIGINT REFERENCES accounts(id),
    homework_id BIGINT REFERENCES homeworks(id) ON DELETE CASCADE
);