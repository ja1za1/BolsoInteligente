PGDMP         +                {            financialplanning    14.8    14.8 =    1           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            2           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            3           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            4           1262    24577    financialplanning    DATABASE     q   CREATE DATABASE financialplanning WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Portuguese_Brazil.1252';
 !   DROP DATABASE financialplanning;
                dba    false            5           0    0    DATABASE financialplanning    ACL     D   REVOKE CONNECT,TEMPORARY ON DATABASE financialplanning FROM PUBLIC;
                   dba    false    3380            �            1259    32793 	   categoria    TABLE     d   CREATE TABLE public.categoria (
    codigo bigint NOT NULL,
    descricao character varying(255)
);
    DROP TABLE public.categoria;
       public         heap    postgres    false            6           0    0    TABLE categoria    ACL     4   REVOKE ALL ON TABLE public.categoria FROM postgres;
          public          postgres    false    215            �            1259    32792    categoria_codigo_seq    SEQUENCE     }   CREATE SEQUENCE public.categoria_codigo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.categoria_codigo_seq;
       public          postgres    false    215            7           0    0    categoria_codigo_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.categoria_codigo_seq OWNED BY public.categoria.codigo;
          public          postgres    false    214            8           0    0    SEQUENCE categoria_codigo_seq    ACL     B   REVOKE ALL ON SEQUENCE public.categoria_codigo_seq FROM postgres;
          public          postgres    false    214            �            1259    32800    despesa    TABLE     }   CREATE TABLE public.despesa (
    codigo bigint NOT NULL,
    descricao character varying(255),
    cod_categoria integer
);
    DROP TABLE public.despesa;
       public         heap    postgres    false            �            1259    32799    despesa_codigo_seq    SEQUENCE     {   CREATE SEQUENCE public.despesa_codigo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.despesa_codigo_seq;
       public          postgres    false    217            9           0    0    despesa_codigo_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.despesa_codigo_seq OWNED BY public.despesa.codigo;
          public          postgres    false    216            :           0    0    SEQUENCE despesa_codigo_seq    ACL     @   REVOKE ALL ON SEQUENCE public.despesa_codigo_seq FROM postgres;
          public          postgres    false    216            �            1259    32786    forma_pagamento    TABLE     j   CREATE TABLE public.forma_pagamento (
    codigo bigint NOT NULL,
    descricao character varying(255)
);
 #   DROP TABLE public.forma_pagamento;
       public         heap    postgres    false            �            1259    32785    forma_pagamento_codigo_seq    SEQUENCE     �   CREATE SEQUENCE public.forma_pagamento_codigo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.forma_pagamento_codigo_seq;
       public          postgres    false    213            ;           0    0    forma_pagamento_codigo_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.forma_pagamento_codigo_seq OWNED BY public.forma_pagamento.codigo;
          public          postgres    false    212            <           0    0 #   SEQUENCE forma_pagamento_codigo_seq    ACL     H   REVOKE ALL ON SEQUENCE public.forma_pagamento_codigo_seq FROM postgres;
          public          postgres    false    212            �            1259    32837    investimento    TABLE     E  CREATE TABLE public.investimento (
    codigo bigint NOT NULL,
    objetivo character varying(255),
    estrategia character varying(255),
    nome character varying(255),
    valor_investido numeric(10,2),
    posicao numeric(10,2),
    rendimento_bruto numeric(10,2),
    rentabilidade numeric(5,2),
    vencimento date
);
     DROP TABLE public.investimento;
       public         heap    postgres    false            �            1259    32836    investimento_codigo_seq    SEQUENCE     �   CREATE SEQUENCE public.investimento_codigo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.investimento_codigo_seq;
       public          postgres    false    219            =           0    0    investimento_codigo_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.investimento_codigo_seq OWNED BY public.investimento.codigo;
          public          postgres    false    218            >           0    0     SEQUENCE investimento_codigo_seq    ACL     E   REVOKE ALL ON SEQUENCE public.investimento_codigo_seq FROM postgres;
          public          postgres    false    218            �            1259    32868    meta_mensal    TABLE     �   CREATE TABLE public.meta_mensal (
    mes_ano_meta smallint NOT NULL,
    cod_categoria integer NOT NULL,
    valor numeric(10,2),
    percentual numeric(5,2)
);
    DROP TABLE public.meta_mensal;
       public         heap    postgres    false            �            1259    32846 	   orcamento    TABLE     �   CREATE TABLE public.orcamento (
    mes_ano smallint NOT NULL,
    cod_despesa integer NOT NULL,
    data_despesa date,
    data_pagamento date,
    cod_forma_pagamento integer,
    valor numeric(10,2),
    situacao boolean
);
    DROP TABLE public.orcamento;
       public         heap    postgres    false            �            1259    32769    renda    TABLE     `   CREATE TABLE public.renda (
    codigo bigint NOT NULL,
    descricao character varying(255)
);
    DROP TABLE public.renda;
       public         heap    postgres    false            �            1259    32768    renda_codigo_seq    SEQUENCE     y   CREATE SEQUENCE public.renda_codigo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.renda_codigo_seq;
       public          postgres    false    210            ?           0    0    renda_codigo_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.renda_codigo_seq OWNED BY public.renda.codigo;
          public          postgres    false    209            @           0    0    SEQUENCE renda_codigo_seq    ACL     >   REVOKE ALL ON SEQUENCE public.renda_codigo_seq FROM postgres;
          public          postgres    false    209            �            1259    32775    renda_mensal    TABLE     v   CREATE TABLE public.renda_mensal (
    cod_renda integer NOT NULL,
    data date NOT NULL,
    valor numeric(10,2)
);
     DROP TABLE public.renda_mensal;
       public         heap    postgres    false                       2604    32796    categoria codigo    DEFAULT     t   ALTER TABLE ONLY public.categoria ALTER COLUMN codigo SET DEFAULT nextval('public.categoria_codigo_seq'::regclass);
 ?   ALTER TABLE public.categoria ALTER COLUMN codigo DROP DEFAULT;
       public          postgres    false    214    215    215            �           2604    32803    despesa codigo    DEFAULT     p   ALTER TABLE ONLY public.despesa ALTER COLUMN codigo SET DEFAULT nextval('public.despesa_codigo_seq'::regclass);
 =   ALTER TABLE public.despesa ALTER COLUMN codigo DROP DEFAULT;
       public          postgres    false    216    217    217            ~           2604    32789    forma_pagamento codigo    DEFAULT     �   ALTER TABLE ONLY public.forma_pagamento ALTER COLUMN codigo SET DEFAULT nextval('public.forma_pagamento_codigo_seq'::regclass);
 E   ALTER TABLE public.forma_pagamento ALTER COLUMN codigo DROP DEFAULT;
       public          postgres    false    212    213    213            �           2604    32840    investimento codigo    DEFAULT     z   ALTER TABLE ONLY public.investimento ALTER COLUMN codigo SET DEFAULT nextval('public.investimento_codigo_seq'::regclass);
 B   ALTER TABLE public.investimento ALTER COLUMN codigo DROP DEFAULT;
       public          postgres    false    219    218    219            }           2604    32772    renda codigo    DEFAULT     l   ALTER TABLE ONLY public.renda ALTER COLUMN codigo SET DEFAULT nextval('public.renda_codigo_seq'::regclass);
 ;   ALTER TABLE public.renda ALTER COLUMN codigo DROP DEFAULT;
       public          postgres    false    210    209    210            (          0    32793 	   categoria 
   TABLE DATA           6   COPY public.categoria (codigo, descricao) FROM stdin;
    public          postgres    false    215   }C       *          0    32800    despesa 
   TABLE DATA           C   COPY public.despesa (codigo, descricao, cod_categoria) FROM stdin;
    public          postgres    false    217   �C       &          0    32786    forma_pagamento 
   TABLE DATA           <   COPY public.forma_pagamento (codigo, descricao) FROM stdin;
    public          postgres    false    213   �C       ,          0    32837    investimento 
   TABLE DATA           �   COPY public.investimento (codigo, objetivo, estrategia, nome, valor_investido, posicao, rendimento_bruto, rentabilidade, vencimento) FROM stdin;
    public          postgres    false    219   �C       .          0    32868    meta_mensal 
   TABLE DATA           U   COPY public.meta_mensal (mes_ano_meta, cod_categoria, valor, percentual) FROM stdin;
    public          postgres    false    221   �C       -          0    32846 	   orcamento 
   TABLE DATA           }   COPY public.orcamento (mes_ano, cod_despesa, data_despesa, data_pagamento, cod_forma_pagamento, valor, situacao) FROM stdin;
    public          postgres    false    220   D       #          0    32769    renda 
   TABLE DATA           2   COPY public.renda (codigo, descricao) FROM stdin;
    public          postgres    false    210   +D       $          0    32775    renda_mensal 
   TABLE DATA           >   COPY public.renda_mensal (cod_renda, data, valor) FROM stdin;
    public          postgres    false    211   HD       A           0    0    categoria_codigo_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.categoria_codigo_seq', 7, true);
          public          postgres    false    214            B           0    0    despesa_codigo_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.despesa_codigo_seq', 1, false);
          public          postgres    false    216            C           0    0    forma_pagamento_codigo_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.forma_pagamento_codigo_seq', 1, false);
          public          postgres    false    212            D           0    0    investimento_codigo_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.investimento_codigo_seq', 1, false);
          public          postgres    false    218            E           0    0    renda_codigo_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.renda_codigo_seq', 43, true);
          public          postgres    false    209            �           2606    32798    categoria categoria_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (codigo);
 B   ALTER TABLE ONLY public.categoria DROP CONSTRAINT categoria_pkey;
       public            postgres    false    215            �           2606    32805    despesa despesa_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT despesa_pkey PRIMARY KEY (codigo);
 >   ALTER TABLE ONLY public.despesa DROP CONSTRAINT despesa_pkey;
       public            postgres    false    217            �           2606    32791 $   forma_pagamento forma_pagamento_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.forma_pagamento
    ADD CONSTRAINT forma_pagamento_pkey PRIMARY KEY (codigo);
 N   ALTER TABLE ONLY public.forma_pagamento DROP CONSTRAINT forma_pagamento_pkey;
       public            postgres    false    213            �           2606    32844    investimento investimento_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.investimento
    ADD CONSTRAINT investimento_pkey PRIMARY KEY (codigo);
 H   ALTER TABLE ONLY public.investimento DROP CONSTRAINT investimento_pkey;
       public            postgres    false    219            �           2606    32872    meta_mensal meta_mensal_pkey 
   CONSTRAINT     s   ALTER TABLE ONLY public.meta_mensal
    ADD CONSTRAINT meta_mensal_pkey PRIMARY KEY (mes_ano_meta, cod_categoria);
 F   ALTER TABLE ONLY public.meta_mensal DROP CONSTRAINT meta_mensal_pkey;
       public            postgres    false    221    221            �           2606    32850    orcamento orcamento_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT orcamento_pkey PRIMARY KEY (mes_ano, cod_despesa);
 B   ALTER TABLE ONLY public.orcamento DROP CONSTRAINT orcamento_pkey;
       public            postgres    false    220    220            �           2606    32779    renda_mensal renda_mensal_pkey 
   CONSTRAINT     i   ALTER TABLE ONLY public.renda_mensal
    ADD CONSTRAINT renda_mensal_pkey PRIMARY KEY (cod_renda, data);
 H   ALTER TABLE ONLY public.renda_mensal DROP CONSTRAINT renda_mensal_pkey;
       public            postgres    false    211    211            �           2606    32774    renda renda_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.renda
    ADD CONSTRAINT renda_pkey PRIMARY KEY (codigo);
 :   ALTER TABLE ONLY public.renda DROP CONSTRAINT renda_pkey;
       public            postgres    false    210            �           2606    32806 "   despesa despesa_cod_categoria_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT despesa_cod_categoria_fkey FOREIGN KEY (cod_categoria) REFERENCES public.categoria(codigo);
 L   ALTER TABLE ONLY public.despesa DROP CONSTRAINT despesa_cod_categoria_fkey;
       public          postgres    false    215    3209    217            �           2606    32873 *   meta_mensal meta_mensal_cod_categoria_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.meta_mensal
    ADD CONSTRAINT meta_mensal_cod_categoria_fkey FOREIGN KEY (cod_categoria) REFERENCES public.categoria(codigo);
 T   ALTER TABLE ONLY public.meta_mensal DROP CONSTRAINT meta_mensal_cod_categoria_fkey;
       public          postgres    false    221    215    3209            �           2606    32851 $   orcamento orcamento_cod_despesa_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT orcamento_cod_despesa_fkey FOREIGN KEY (cod_despesa) REFERENCES public.despesa(codigo);
 N   ALTER TABLE ONLY public.orcamento DROP CONSTRAINT orcamento_cod_despesa_fkey;
       public          postgres    false    3211    220    217            �           2606    32856 ,   orcamento orcamento_cod_forma_pagamento_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT orcamento_cod_forma_pagamento_fkey FOREIGN KEY (cod_forma_pagamento) REFERENCES public.forma_pagamento(codigo);
 V   ALTER TABLE ONLY public.orcamento DROP CONSTRAINT orcamento_cod_forma_pagamento_fkey;
       public          postgres    false    213    220    3207            �           2606    32780 (   renda_mensal renda_mensal_cod_renda_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.renda_mensal
    ADD CONSTRAINT renda_mensal_cod_renda_fkey FOREIGN KEY (cod_renda) REFERENCES public.renda(codigo);
 R   ALTER TABLE ONLY public.renda_mensal DROP CONSTRAINT renda_mensal_cod_renda_fkey;
       public          postgres    false    210    3203    211            	           826    32845    DEFAULT PRIVILEGES FOR TABLES    DEFAULT ACL     P   ALTER DEFAULT PRIVILEGES FOR ROLE postgres REVOKE ALL ON TABLES  FROM postgres;
                   postgres    false            (      x������ � �      *      x������ � �      &      x������ � �      ,      x������ � �      .      x������ � �      -      x������ � �      #      x������ � �      $      x������ � �     