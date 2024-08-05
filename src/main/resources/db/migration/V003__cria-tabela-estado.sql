CREATE TABLE estado(
	id serial8 NOT NULL PRIMARY KEY,
	nome varchar(80) NOT NULL
);

insert into estado (nome) select distinct nome_estado from cidade;

alter table cidade add column estado_id bigint;

update cidade set estado_id = (select e.id from estado e where e.nome = cidade.nome_estado);

alter table cidade ALTER column estado_id SET NOT null;

alter table cidade add constraint fk_cidade_estado
foreign key (estado_id) references estado (id);

alter table cidade drop column nome_estado;

alter table cidade RENAME column nome_cidade TO nome;