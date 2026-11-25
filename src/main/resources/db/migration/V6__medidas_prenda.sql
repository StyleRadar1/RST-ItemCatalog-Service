CREATE TABLE IF NOT EXISTS itemcatalogo.medida_prenda (
    id          UUID PRIMARY KEY,
    prenda_id   UUID             NOT NULL REFERENCES itemcatalogo.prenda (id),
    talla       VARCHAR(10)      NOT NULL,
    min_pecho   DOUBLE PRECISION,
    max_pecho   DOUBLE PRECISION,
    min_cintura DOUBLE PRECISION,
    max_cintura DOUBLE PRECISION,
    min_cadera  DOUBLE PRECISION,
    max_cadera  DOUBLE PRECISION,
    largo       DOUBLE PRECISION,
    CONSTRAINT uk_medida_prenda_talla UNIQUE (prenda_id, talla)
);
