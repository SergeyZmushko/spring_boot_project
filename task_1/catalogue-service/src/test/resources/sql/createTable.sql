create schema if not exists catalogue;
create table if not exists catalogue.t_product
(
    id        serial        not null primary key,
    c_title   varchar(50)   not null,
    c_details varchar(1000) not null
);