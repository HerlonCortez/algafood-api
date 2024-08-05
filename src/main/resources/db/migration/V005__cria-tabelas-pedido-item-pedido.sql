create table pedido (
	id serial8 not null primary key,
	descricao text not null,
	data_criacao timestamptz not null,
	data_confirmacao timestamptz not null,
	data_cancelamento timestamptz not null,
	data_entrega timestamptz not null,
	sub_total decimal(10,2) not null,
	taxa_frete decimal(10,2) not null,
	valor_total decimal(10,2) not null,
	forma_pagamento_id bigint not null,
	restaurante_id bigint not null,
	usuario_cliente_id bigint not null,
	
	endereco_cidade_id bigint not null,
	endereco_cep varchar(9) not null,
	endereco_logradouro varchar(100) not null,
	endereco_numero varchar(20) not null,
	endereco_complemento varchar(60) null,
	endereco_bairro varchar(60) not null,
  
    status varchar(10) not null
);

ALTER TABLE public.pedido ADD CONSTRAINT fk_pedido_restaurante FOREIGN KEY (restaurante_id) REFERENCES public.restaurante(id);
ALTER TABLE public.pedido ADD CONSTRAINT fk_pedido_usuario_cliente FOREIGN KEY (usuario_cliente_id) REFERENCES public.usuario(id);
ALTER TABLE public.pedido ADD CONSTRAINT fk_pedido_forma_pagamento FOREIGN KEY (forma_pagamento_id) REFERENCES public.forma_pagamento(id);

CREATE table item_pedido(
	id serial8 not null primary key,
	descricao text not null,
	quantidade smallint not null,
	preco_unitario decimal(10,2) not null,
	preco_total decimal(10,2) not null,
	observacao varchar(255) null,
	pedido_id bigint not null,
	produto_id bigint not NULL,
	
	CONSTRAINT uk_item_pedido_produto UNIQUE (pedido_id, produto_id)
);

ALTER TABLE public.item_pedido ADD CONSTRAINT fk_item_pedido_pedido FOREIGN KEY (pedido_id) REFERENCES public.pedido(id);
ALTER TABLE public.item_pedido ADD CONSTRAINT fk_item_pedido_produto FOREIGN KEY (produto_id) REFERENCES public.produto(id);