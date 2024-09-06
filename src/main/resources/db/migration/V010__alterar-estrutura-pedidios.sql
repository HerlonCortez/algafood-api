ALTER TABLE public.pedido DROP COLUMN descricao;
ALTER TABLE public.pedido ALTER COLUMN data_entrega DROP NOT NULL;
ALTER TABLE public.pedido ALTER COLUMN data_cancelamento DROP NOT NULL;
ALTER TABLE public.pedido ALTER COLUMN data_confirmacao DROP NOT NULL;