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
values (1, 'Comment_1'), (1, 'Comment_4'),
       (2, 'Comment_2'), (2, 'Comment_5'),
       (3, 'Comment_3'), (3, 'Comment_6');

insert into users(login, password)
values ('user','$2a$10$/Df3OzUvDbndggyFNSF74OfnMjbewfq4K882/w/Z8YoX35LvfPSF2')