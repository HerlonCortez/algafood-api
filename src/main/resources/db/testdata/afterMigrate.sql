truncate restaurante_forma_pagamento cascade;
truncate restaurante_usuario_responsavel cascade;
TRUNCATE produto RESTART IDENTITY CASCADE;
TRUNCATE restaurante RESTART IDENTITY CASCADE;
TRUNCATE forma_pagamento RESTART IDENTITY CASCADE;
TRUNCATE cozinha RESTART IDENTITY CASCADE;
TRUNCATE grupo_permissao RESTART IDENTITY CASCADE;
TRUNCATE usuario_grupo RESTART IDENTITY CASCADE;
TRUNCATE usuario RESTART IDENTITY CASCADE;
TRUNCATE grupo RESTART IDENTITY CASCADE;
TRUNCATE permissao RESTART IDENTITY CASCADE;
TRUNCATE cidade RESTART IDENTITY CASCADE;
TRUNCATE estado RESTART IDENTITY CASCADE;
TRUNCATE foto_produto CASCADE;

insert into cozinha (id, nome) values (1, 'Tailandesa') ON CONFLICT (id) DO NOTHING;
insert into cozinha (id, nome) values (2, 'Indiana') ON CONFLICT (id) DO NOTHING;
insert into cozinha (id, nome) values (3, 'Mineira') ON CONFLICT (id) DO NOTHING;
insert into cozinha (id, nome) values (4, 'Cearense') ON CONFLICT (id) DO NOTHING;

insert into estado (id, nome) values (1, 'Minas Gerais') ON CONFLICT (id) DO NOTHING;
insert into estado (id, nome) values (2, 'São Paulo') ON CONFLICT (id) DO NOTHING;
insert into estado (id, nome) values (3, 'Ceará') ON CONFLICT (id) DO NOTHING;

insert into cidade (id, nome, estado_id) values (1, 'Uberlândia', 1) ON CONFLICT (id) DO NOTHING;
insert into cidade (id, nome, estado_id) values (2, 'Belo Horizonte', 1) ON CONFLICT (id) DO NOTHING;
insert into cidade (id, nome, estado_id) values (3, 'São Paulo', 2) ON CONFLICT (id) DO NOTHING;
insert into cidade (id, nome, estado_id) values (4, 'Campinas', 2) ON CONFLICT (id) DO NOTHING;
insert into cidade (id, nome, estado_id) values (5, 'Fortaleza', 3) ON CONFLICT (id) DO NOTHING;

insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao, endereco_cidade_id, endereco_cep, endereco_logradouro, endereco_numero, endereco_bairro) values (1, 'Thai Gourmet', 10, 1, now() AT TIME ZONE 'UTC', now() AT TIME ZONE 'UTC', 1, '38400-999', 'Rua João Pinheiro', '1000', 'Centro') ON CONFLICT (id) DO NOTHING;
insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao) values (2, 'Thai Delivery', 9.50, 1, now() AT TIME ZONE 'UTC', now() AT TIME ZONE 'UTC') ON CONFLICT (id) DO NOTHING;
insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao) values (3, 'Tuk Tuk Comida Indiana', 15, 2, now() AT TIME ZONE 'UTC', now() AT TIME ZONE 'UTC') ON CONFLICT (id) DO NOTHING;
insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao) values (4, 'Java Steakhouse', 12, 3, now() AT TIME ZONE 'UTC', now() AT TIME ZONE 'UTC') ON CONFLICT (id) DO NOTHING;
insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao) values (5, 'Lanchonete do Tio Sam', 11, 4, now() AT TIME ZONE 'UTC', now() AT TIME ZONE 'UTC') ON CONFLICT (id) DO NOTHING;
insert into restaurante (id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao) values (6, 'Bar da Maria', 6, 4, now() AT TIME ZONE 'UTC', now() AT TIME ZONE 'UTC') ON CONFLICT (id) DO NOTHING;

insert into forma_pagamento (id, descricao) values (1, 'Cartão de crédito') ON CONFLICT (id) DO NOTHING;
insert into forma_pagamento (id, descricao) values (2, 'Cartão de débito') ON CONFLICT (id) DO NOTHING;
insert into forma_pagamento (id, descricao) values (3, 'Dinheiro') ON CONFLICT (id) DO NOTHING;

insert into permissao (id, nome, descricao) values (1, 'CONSULTAR_COZINHAS', 'Permite consultar cozinhas') ON CONFLICT (id) DO NOTHING;
insert into permissao (id, nome, descricao) values (2, 'EDITAR_COZINHAS', 'Permite editar cozinhas') ON CONFLICT (id) DO NOTHING;

insert into restaurante_forma_pagamento (restaurante_id, forma_pagamento_id) values (1, 1), (1, 2), (1, 3), (2, 3), (3, 2), (3, 3);

insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('Porco com molho agridoce', 'Deliciosa carne suína ao molho especial', 78.90, false, 1);
insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('Camarão tailandês', '16 camarões grandes ao molho picante', 110, true, 1);

insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('Salada picante com carne grelhada', 'Salada de folhas com cortes finos de carne bovina grelhada e nosso molho especial de pimenta vermelha', 87.20, true, 2);

insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('Garlic Naan', 'Pão tradicional indiano com cobertura de alho', 21, true, 3);
insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('Murg Curry', 'Cubos de frango preparados com molho curry e especiarias', 43, true, 3);

insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('Bife Ancho', 'Corte macio e suculento, com dois dedos de espessura, retirado da parte dianteira do contrafilé', 79, true, 4);
insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('T-Bone', 'Corte muito saboroso, com um osso em formato de T, sendo de um lado o contrafilé e do outro o filé mignon', 89, true, 4);

insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('Sanduíche X-Tudo', 'Sandubão com muito queijo, hamburger bovino, bacon, ovo, salada e maionese', 19, true, 5);

insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('Espetinho de Cupim', 'Acompanha farinha, mandioca e vinagrete', 8, true, 6);

insert into grupo (nome) values ('Gerente'), ('Vendedor'), ('Secretária'), ('Cadastrador');

insert into grupo_permissao (grupo_id, permissao_id) values (1, 1), (1, 2), (2, 1), (2, 2), (3, 1); 

insert into usuario (nome, email, senha, data_cadastro) values
('João da Silva', 'joao.ger@algafood.com', '123', now() AT TIME ZONE 'UTC'),
('Maria Joaquina', 'maria.vnd@algafood.com', '123', now() AT TIME ZONE 'UTC'),
('José Souza', 'jose.aux@algafood.com', '123', now() AT TIME ZONE 'UTC'),
('Sebastião Martins', 'sebastiao.cad@algafood.com', '123', now() AT TIME ZONE 'UTC'),
('Manoel Lima', 'manoel.loja@gmail.com', '123', now() AT TIME ZONE 'UTC');

insert into usuario_grupo (usuario_id, grupo_id) values (1, 1), (1, 2), (2, 2);

insert into restaurante_usuario_responsavel (restaurante_id, usuario_id) values (1, 5), (3, 5);

insert into pedido (id, codigo, restaurante_id, usuario_cliente_id, forma_pagamento_id, endereco_cidade_id, endereco_cep, 
    endereco_logradouro, endereco_numero, endereco_complemento, endereco_bairro,
    status, data_criacao, sub_total, taxa_frete, valor_total)
values (1, 'f9981ca4-5a5e-4da3-af04-933861df3e55', 1, 1, 1, 1, '38400-000', 'Rua Floriano Peixoto', '500', 'Apto 801', 'Brasil',
'CRIADO', now() AT TIME ZONE 'UTC', 298.90, 10, 308.90);

insert into item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, preco_total, observacao)
values (1, 1, 1, 1, 78.9, 78.9, null);

insert into item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, preco_total, observacao)
values (2, 1, 2, 2, 110, 220, 'Menos picante, por favor');


insert into pedido (id, codigo, restaurante_id, usuario_cliente_id, forma_pagamento_id, endereco_cidade_id, endereco_cep, 
        endereco_logradouro, endereco_numero, endereco_complemento, endereco_bairro,
        status, data_criacao, sub_total, taxa_frete, valor_total)
values (2, 'd178b637-a785-4768-a3cb-aa1ce5a8cdab', 4, 1, 2, 1, '38400-111', 'Rua Acre', '300', 'Casa 2', 'Centro',
'CRIADO', now() AT TIME ZONE 'UTC', 79, 0, 79);

insert into item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, preco_total, observacao)
values (3, 2, 6, 1, 79, 79, 'Ao ponto');

insert into item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, preco_total, observacao)
values (4, 1, 7, 2, 110, 220, 'Menos picante, por favor');

insert into pedido (id, codigo, restaurante_id, usuario_cliente_id, forma_pagamento_id, endereco_cidade_id, endereco_cep, 
                    endereco_logradouro, endereco_numero, endereco_complemento, endereco_bairro,
	                status, data_criacao, data_confirmacao, data_entrega, sub_total, taxa_frete, valor_total)
values (3, 'b5741512-8fbc-47fa-9ac1-b530354fc0ff', 1, 1, 1, 1, '38400-222', 'Rua Natal', '200', null, 'Brasil',
        'ENTREGUE', '2024-09-18 21:10:00', '2024-09-18 21:10:45', '2024-09-18 21:55:44', 110, 10, 120);

insert into item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, preco_total, observacao)
values (5, 3, 2, 1, 110, 110, null);

insert into pedido (id, codigo, restaurante_id, usuario_cliente_id, forma_pagamento_id, endereco_cidade_id, endereco_cep, 
                    endereco_logradouro, endereco_numero, endereco_complemento, endereco_bairro,
	                status, data_criacao, data_confirmacao, data_entrega, sub_total, taxa_frete, valor_total)
values (4, '5c621c9a-ba61-4454-8631-8aabefe58dc2', 1, 2, 1, 1, '38400-800', 'Rua Fortaleza', '900', 'Apto 504', 'Centro',
        'ENTREGUE', '2024-09-18 19:10:00', '2024-09-18 19:10:45', '2024-09-18 19:55:44', 174.4, 5, 179.4);

insert into item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, preco_total, observacao)
values (6, 4, 3, 2, 87.2, 174.4, null);

insert into pedido (id, codigo, restaurante_id, usuario_cliente_id, forma_pagamento_id, endereco_cidade_id, endereco_cep, 
                    endereco_logradouro, endereco_numero, endereco_complemento, endereco_bairro,
	                status, data_criacao, data_confirmacao, data_entrega, sub_total, taxa_frete, valor_total)
values (5, '8d774bcf-b238-42f3-aef1-5fb388754d63', 1, 3, 2, 1, '38400-200', 'Rua 10', '930', 'Casa 20', 'Martins',
        'ENTREGUE', '2024-09-17 19:10:00', '2024-09-17 19:10:45', '2024-09-17 19:55:44', 87.2, 10, 97.2);

insert into item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, preco_total, observacao)
values (7, 5, 3, 1, 87.2, 87.2, null);

SELECT setval('public.cidade_id_seq', (SELECT MAX(id) FROM cidade));
SELECT setval('public.cozinha_id_seq', (SELECT MAX(id) FROM cozinha));
SELECT setval('public.estado_id_seq', (SELECT MAX(id) FROM estado));
SELECT setval('public.restaurante_id_seq', (SELECT MAX(id) FROM restaurante));
SELECT setval('public.grupo_id_seq', (SELECT MAX(id) FROM grupo));
SELECT setval('public.forma_pagamento_id_seq', (SELECT MAX(id) FROM forma_pagamento));
SELECT setval('public.pedido_id_seq', (SELECT MAX(id) FROM pedido));
SELECT setval('public.item_pedido_id_seq', (SELECT MAX(id) FROM item_pedido));
SELECT setval('public.permissao_id_seq', (SELECT MAX(id) FROM permissao));
SELECT setval('public.produto_id_seq', (SELECT MAX(id) FROM produto));
SELECT setval('public.usuario_id_seq', (SELECT MAX(id) FROM usuario));

