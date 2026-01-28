Alter table ingredient drop constraint if not exists fk_dish;
Alter table ingredient drop column if not exists id_dish;

create type uniti_type as enum ('FCS','KG','L');

create table dishIngredient(
 id serial primary key,
 id_dish int not null,
 id_ingredient int not null,
 quantity_required numeric not null,
 uniti uniti_type,
 constraint fk_dish foreign key (id_dish) references dish(id),
constraint fk_ingredient foreign key(id_ingredient) references ingredient(id));

update dish set price =3500.00 where id=1
update dish set price =12000.00 where id=2
update dish set price =8000.00 where id=4

insert into dishIngredient (id,id_dish,id_ingredient,quantity_required,uniti) values(1,1,1,0.20,'KG'),
                                        (2,1,2,0.15,'KG'),
                                        (3,2,3,1.00,'KG'),
                                        (4,4,4,0.30,'KG'),
                                        (5,4,5,0.20,'KG');

create type mouvement_type as enum('IN','OUT');

Create table stockMouvement(
    id serial primary key,
    quantitiy numeric,
    type mouvement_type,
    unity uniti_type,
    creation_dateTime timestamp,
    id_ingredient int,
    CONSTRAINT fk_stock foreign key (id_ingredient) references ingredient(id)
);

create table orders(
    id serial primary key ,
    reference varchar(150) not null ,
    create_datetime TIMESTAMP not null
);

create table dishOrder(
    id serial primary key ,
    id_order int,
    id_dish int,
    CONSTRAINT fk_order foreign key (id_order) references orders(id),
    CONSTRAINT fk_dish foreign key (id_dish) references dish(id)
)

insert into stockMouvement(id,quantitiy,type,unity,creation_dateTime,id_ingredient) values
(1, 5.0, 'IN', 'KG', '2024-01-05 08:00',1),
(2, 0.2, 'OUT', 'KG', '2024-01-06 12:00',1),
(3, 4.0, 'IN', 'KG', '2024-01-05 08:00',2),
(4, 0.15, 'OUT', 'KG', '2024-01-06 12:00',2),
(5, 10.0, 'IN', 'KG', '2024-01-04 09:00',3),
(6, 1.0, 'OUT', 'KG', '2024-01-06 13:00',3),
(7, 3.0, 'IN', 'KG', '2024-01-05 10:00',4),
(8, 0.3, 'OUT', 'KG', '2024-01-06 14:00',4),
(9, 2.5, 'IN', 'KG', '2024-01-05 10:00',5),
(10, 0.2, 'OUT', 'KG', '2024-01-06 14:00',5);

ALTER TABLE dishOrder add column quantity int not null;