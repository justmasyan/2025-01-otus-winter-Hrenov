create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

create table commentaries (
    id bigserial,
    text varchar(255),
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);

create table users (
    id bigserial,
    login varchar(255) UNIQUE,
    password varchar(255),
    role int NOT NULL,
    primary key (id)
);

create table acl_sid (
    id bigserial,
    principal tinyint NOT NULL,
    sid varchar(100) NOT NULL,
    primary key (id)
);

CREATE table acl_class (
  id bigserial,
  class varchar(255) NOT NULL,
  primary key (id)
);

create table acl_object_identity (
    id bigserial,
    object_id_class bigint NOT NULL,
    object_id_identity bigint NOT NULL,
    parent_object bigint DEFAULT NULL,
    owner_sid bigint DEFAULT NULL,
    entries_inheriting tinyint NOT NULL,
    primary key (id),
    foreign key (object_id_class)  references acl_class (id),
    foreign key (parent_object)  references acl_object_identity (id),
    foreign key (owner_sid)  references acl_sid (id)
);

create table acl_entry (
    id bigserial,
    acl_object_identity bigint NOT NULL,
    ace_order int NOT NULL,
    sid bigint NOT NULL,
    mask int NOT NULL,
    granting tinyint NOT NULL,
    audit_success tinyint NOT NULL,
    audit_failure tinyint NOT NULL,
    primary key (id),
    foreign key (acl_object_identity)  references acl_object_identity (id),
    foreign key (sid)  references acl_sid (id)
);