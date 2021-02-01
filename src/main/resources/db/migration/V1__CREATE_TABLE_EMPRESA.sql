CREATE TABLE empresa
(
    id                 INTEGER AUTO_INCREMENT,
    cnpj               VARCHAR(255) NOT NULL,
    nome               VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL,
    logradouro         VARCHAR(255) NOT NULL,
    bairro             VARCHAR(255) NOT NULL,
    uf                 VARCHAR(255) NOT NULL,
    cep                VARCHAR(255) NOT NULL,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (cnpj)
) ENGINE=InnoDB AUTO_INCREMENT=637832;