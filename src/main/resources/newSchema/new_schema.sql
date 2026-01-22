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
    name varchar,
    quantitiy numeric,
    type mouvement_type,
    unity uniti_type,
    creation_dateTime timestamp,
    id_ingredient int,
    CONSTRAINT fk_stock foreign key (id_ingredient) references ingredient(id)
);