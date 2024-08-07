create table forma_pagamento (
	id serial8 not null primary key,
	descricao varchar(60) not null
);

create table grupo (
	id serial8 not null primary key,
	nome varchar(60) not null
);

create table grupo_permissao (
	grupo_id bigint not null,
	permissao_id bigint not null
);

create table permissao (
	id serial8 not null primary key,
	descricao varchar(60) not null,
	nome varchar(100) not null
);

create table produto (
	id serial8 not null primary key,
	restaurante_id bigint not null,
	nome varchar(80) not null,
	descricao text not null,
	preco decimal(10,2) not null,
	ativo boolean not null
);

create table restaurante (
	id serial8 not null primary key,
	cozinha_id bigint not null,
	nome varchar(80) not null,
	taxa_frete decimal(10,2) not null,
	data_atualizacao timestamptz not null,
	data_cadastro timestamptz not null,
	
	endereco_cidade_id bigint,
	endereco_cep varchar(9),
	endereco_logradouro varchar(100),
	endereco_numero varchar(20),
	endereco_complemento varchar(60),
	endereco_bairro varchar(60)
);

create table restaurante_forma_pagamento (
	restaurante_id bigint not null,
	forma_pagamento_id bigint not null
);

create table usuario (
	id serial8 not null primary key,
	nome varchar(80) not null,
	email varchar(255) not null,
	senha varchar(255) not null,
	data_cadastro timestamptz not null
);

create table usuario_grupo (
	usuario_id bigint not null,
	grupo_id bigint not null
);




alter table grupo_permissao add constraint fk_grupo_permissao_permissao
foreign key (permissao_id) references permissao (id);

alter table grupo_permissao add constraint fk_grupo_permissao_grupo
foreign key (grupo_id) references grupo (id);

alter table produto add constraint fk_produto_restaurante
foreign key (restaurante_id) references restaurante (id);

alter table restaurante add constraint fk_restaurante_cozinha
foreign key (cozinha_id) references cozinha (id);

alter table restaurante add constraint fk_restaurante_cidade
foreign key (endereco_cidade_id) references cidade (id);

alter table restaurante_forma_pagamento add constraint fk_rest_forma_pagto_forma_pagto
foreign key (forma_pagamento_id) references forma_pagamento (id);

alter table restaurante_forma_pagamento add constraint fk_rest_forma_pagto_restaurante
foreign key (restaurante_id) references restaurante (id);

alter table usuario_grupo add constraint fk_usuario_grupo_grupo
foreign key (grupo_id) references grupo (id);

alter table usuario_grupo add constraint fk_usuario_grupo_usuario
foreign key (usuario_id) references usuario (id);