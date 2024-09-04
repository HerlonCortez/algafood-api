create table restaurante_usuario_responsavel (
  restaurante_id int8 not null,
  usuario_id int8 not NULL,
  CONSTRAINT restaurante_usuario_responsavel_pkey PRIMARY KEY (restaurante_id, usuario_id)
);

alter table restaurante_usuario_responsavel add constraint fk_restaurante_usuario_restaurante
foreign key (restaurante_id) references restaurante (id);

alter table restaurante_usuario_responsavel add constraint fk_restaurante_usuario_usuario
foreign key (usuario_id) references usuario (id);
