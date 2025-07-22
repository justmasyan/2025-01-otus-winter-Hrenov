insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);
       
insert into commentaries(book_id, text)
values (1, 'Comment_1'), (1, 'Comment_2'),
       (2, 'Comment_3'), (2, 'Comment_4'),
       (3, 'Comment_5'), (3, 'Comment_6');

insert into users(login, password, role)
values ('user','$2a$10$NOZCt5QWt3jxrSYZIK31YuEP43IoypN57OUuYsJAPLJn/AptaiSGi', 2),
       ('manager','$2a$10$qoJaPyjEKVWc31j.1ltli.HWSu3677F1DJoO4G6bMXuAEAR1T8UGy', 1),
       ('admin','$2a$10$rWQmDyUPS2r.fHzl24S8dOLYq7uLO.142u7/.THZ.BlQfC6rl83s6', 0);

insert into acl_class(class)
values ('ru.otus.hw.dto.AuthorDto'), ('ru.otus.hw.dto.GenreDto'),
       ('ru.otus.hw.dto.CommentaryDto'), ('ru.otus.hw.dto.BookDto');

insert into acl_sid(principal, sid)
values (0, 'ROLE_USER'), (0, 'ROLE_MANAGER'), (0, 'ROLE_ADMIN');

insert into acl_object_identity(object_id_class, object_id_identity,parent_object,owner_sid,entries_inheriting)
values (1, 1, NULL, 3, 0), (1, 2, NULL, 3, 0), (1, 3, NULL, 3, 0), --Авторы
    (2, 1, NULL, 3, 0), (2, 2, NULL, 3, 0), (2, 3, NULL, 3, 0),
    (2, 4, NULL, 3, 0), (2, 5, NULL, 3, 0), (2, 6, NULL, 3, 0), --Жанры
    (3, 1, NULL, 3, 0), (3, 2, NULL, 3, 0), (3, 3, NULL, 3, 0),
    (3, 4, NULL, 3, 0), (3, 5, NULL, 3, 0), (3, 6, NULL, 3, 0), --Комментарии
    (4, 1, NULL, 3, 0), (4, 2, NULL, 3, 0), (4, 3, NULL, 3, 0); --Книги

insert into acl_entry(acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
values
(1, 1, 1, 1, 1, 1, 1),
(2, 1, 1, 1, 1, 1, 1),
(3, 1, 1, 1, 1, 1, 1), --всем авторам проставили право на чтение для ROLE_USER
(4, 1, 1, 1, 1, 1, 1),
(5, 1, 1, 1, 1, 1, 1),
(6, 1, 1, 1, 1, 1, 1),
(7, 1, 1, 1, 1, 1, 1),
(8, 1, 1, 1, 1, 1, 1),
(9, 1, 1, 1, 1, 1, 1), --всем жанрам проставили право на чтение для ROLE_USER
(10, 1, 1, 1, 1, 1, 1),
(11, 1, 1, 1, 1, 1, 1),
(12, 1, 1, 1, 1, 1, 1),
(13, 1, 1, 1, 1, 1, 1),
(14, 1, 1, 1, 1, 1, 1),
(15, 1, 1, 1, 1, 1, 1), --всем комментариям проставили право на чтение для ROLE_USER
(16, 1, 1, 1, 1, 1, 1),
(17, 1, 1, 1, 1, 1, 1),
(18, 1, 1, 1, 1, 1, 1), --всем книгам проставили право на чтение для ROLE_USER

(10, 1, 2, 2, 1, 1, 1),
(11, 1, 2, 2, 1, 1, 1),
(12, 1, 2, 2, 1, 1, 1),
(13, 1, 2, 2, 1, 1, 1),
(14, 1, 2, 2, 1, 1, 1),
(15, 1, 2, 2, 1, 1, 1), --всем комментариям проставили право на изменение для ROLE_MANAGER
(16, 1, 2, 2, 1, 1, 1),
(17, 1, 2, 2, 1, 1, 1),
(18, 1, 2, 2, 1, 1, 1), --всем книгам проставили право на изменение для ROLE_MANAGER

(10, 1, 3, 8, 1, 1, 1),
(11, 1, 3, 8, 1, 1, 1),
(12, 1, 3, 8, 1, 1, 1),
(13, 1, 3, 8, 1, 1, 1),
(14, 1, 3, 8, 1, 1, 1),
(15, 1, 3, 8, 1, 1, 1), --всем комментариям проставили право на удаление для ROLE_ADMIN
(16, 1, 3, 8, 1, 1, 1),
(17, 1, 3, 8, 1, 1, 1),
(18, 1, 3, 8, 1, 1, 1); --всем книгам проставили право на удаление для ROLE_ADMIN

