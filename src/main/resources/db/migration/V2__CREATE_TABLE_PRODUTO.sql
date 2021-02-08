CREATE TABLE produto
(
    id                   INTEGER AUTO_INCREMENT,
    plano                VARCHAR(255) NOT NULL,
    subplano             VARCHAR(255) NOT NULL,
    data_inicio_vigencia DATE NOT NULL,
    data_fim_vigencia    DATE,
    empresa_id           INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (empresa_id) REFERENCES empresa(id)
) ENGINE=InnoDB AUTO_INCREMENT=237832;