--Script para criação das tabelas no Banco de Dados

CREATE TABLE Usuario (
    codigo SERIAL NOT NULL,
    nome VARCHAR(50),
    login VARCHAR(12),
    senha VARCHAR(12),
    CONSTRAINT pk_Usuario PRIMARY KEY (codigo)
);

CREATE TABLE Caixa (
    codigo SERIAL NOT NULL,
    valor_caixa DECIMAL(10, 2),
    CONSTRAINT pk_Caixa PRIMARY KEY (codigo)
);

CREATE TABLE Produto (
    ID_Produto SERIAL NOT NULL,
    nome VARCHAR(20),
    preco DECIMAL(10, 2),
    tamanho VARCHAR(4),
    cor VARCHAR(20),
    descricao VARCHAR(50),
    estacao SMALLINT,
    CONSTRAINT pk_Produto PRIMARY KEY (ID_Produto)
);

CREATE TABLE Compra (
    codigo SERIAL NOT NULL,
    data DATE,
    valor_total DECIMAL(10, 2),
    CONSTRAINT pk_Compra PRIMARY KEY (codigo)
);

CREATE TABLE Registro_Compra (
    COD_Compra INT NOT NULL,
    ID_Produto INT NOT NULL,
    valor_unitario DECIMAL(10, 2),
    observacao VARCHAR(50),
    CONSTRAINT pk_Registro_compra PRIMARY KEY (COD_Compra, ID_Produto),
    CONSTRAINT fk_Registro_compra_Compra FOREIGN KEY (COD_Compra) REFERENCES Compra (codigo),
    CONSTRAINT fk_Registro_compra_Produto FOREIGN KEY (ID_Produto) REFERENCES Produto (ID_Produto)
);

CREATE TABLE Cliente (
    ID_Cliente SERIAL NOT NULL,
    nome VARCHAR(50),
    contato VARCHAR(50),
    endereco VARCHAR(50),
    CONSTRAINT pk_Cliente PRIMARY KEY (ID_Cliente)
);

CREATE TABLE Venda (
    codigo SERIAL NOT NULL,
    data DATE,
    valor_total DECIMAL(10, 2),
    forma_pagamento VARCHAR(8),
    ID_Cliente INT,
    CONSTRAINT pk_Venda PRIMARY KEY (codigo),
    CONSTRAINT fk_Venda_Cliente FOREIGN KEY (ID_Cliente) REFERENCES Cliente (ID_Cliente)
);

CREATE TABLE Registro_Venda (
    COD_Venda INT NOT NULL,
    ID_Produto INT NOT NULL,
    quantidade INT,
    valor_unitario DECIMAL(10, 2),
    observacao VARCHAR(50),
    CONSTRAINT pk_Registro_venda PRIMARY KEY (COD_Venda, ID_Produto),
    CONSTRAINT fk_Registro_Venda_Venda FOREIGN KEY (COD_Venda) REFERENCES Venda (codigo),
    CONSTRAINT fk_Registro_Venda_Produto FOREIGN KEY (ID_Produto) REFERENCES Produto (ID_Produto)
);

CREATE TABLE Estoque (
    ID_Produto INT NOT NULL,
    quantidade INT DEFAULT 0,
    CONSTRAINT pk_Estoque PRIMARY KEY (ID_Produto),
    CONSTRAINT fk_Estoque_Produto FOREIGN KEY (ID_Produto) REFERENCES Produto(ID_Produto)
);

CREATE TABLE IF NOT EXISTS caixa
(
    codigo integer NOT NULL DEFAULT nextval('caixa_codigo_seq'::regclass),
    valor_caixa numeric(10,2),
    valor_inicial numeric(10,2),
    status boolean DEFAULT true,
    CONSTRAINT pk_caixa PRIMARY KEY (codigo)
)
