create schema if not exists catalogue;
create table if not exists catalogue.t_product
(
    id        serial        not null primary key,
    c_title   varchar(50)   not null,
    c_details varchar(1000) not null
);

insert into catalogue.t_product (id, c_title, c_details)
values (1, 'Товар №1', 'Описание товар №1'),
       (2, 'Шоколадка', 'Черный шоколад'),
       (3, 'Клавиатура', 'Клавиатура механическая'),
       (4, 'Мышка', 'Описание мышки');