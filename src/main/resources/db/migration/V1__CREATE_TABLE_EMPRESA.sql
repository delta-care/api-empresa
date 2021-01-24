CREATE TABLE empresa
(
    id                 INTEGER NOT NULL auto_increment,
    cnpj               VARCHAR(255) NOT NULL,
    nome               VARCHAR(255) NOT NULL,
    coberturas         INTEGER NOT NULL,
    produtos           INTEGER NOT NULL,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    PRIMARY KEY (id)
);