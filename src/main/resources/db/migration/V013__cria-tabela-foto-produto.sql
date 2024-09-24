CREATE TABLE public.foto_produto (
	produto_id int8 NOT NULL,
	nome_arquivo varchar(150) NOT NULL,
	descricao varchar(150),
	content_type varchar(80) NOT NULL,
	tamanho int8 NOT NULL,
	CONSTRAINT produto_id_pkey PRIMARY KEY (produto_id)
);
ALTER TABLE public.foto_produto ADD CONSTRAINT fk_foto_produto_produto FOREIGN KEY (produto_id) REFERENCES public.produto(id);
