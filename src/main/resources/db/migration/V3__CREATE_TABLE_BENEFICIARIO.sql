CREATE TABLE beneficiario
(
    id                   INTEGER AUTO_INCREMENT,
    plano                VARCHAR(255) NOT NULL,
    subplano             VARCHAR(255) NOT NULL,
    cpf                  VARCHAR(255) NOT NULL,
    nome                 VARCHAR(255) NOT NULL,
    empresa_id           INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (empresa_id) REFERENCES empresa(id)
) ENGINE=InnoDB AUTO_INCREMENT=337832;