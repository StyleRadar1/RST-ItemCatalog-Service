CREATE TABLE itemcatalogo.almacen_resumen (
    almacen_id UUID PRIMARY KEY,
    nombre     VARCHAR(120)  NOT NULL,
    reputacion NUMERIC(3, 2) NOT NULL,
    activo     BOOLEAN       NOT NULL
);

CREATE TABLE itemcatalogo.prenda (
    id                UUID PRIMARY KEY,
    almacen_id        UUID           NOT NULL REFERENCES itemcatalogo.almacen_resumen (almacen_id),
    nombre            VARCHAR(120)   NOT NULL,
    tipo              VARCHAR(60)    NOT NULL,
    color             VARCHAR(40),
    marca             VARCHAR(60),
    estilo            VARCHAR(60),
    precio            NUMERIC(12, 2) NOT NULL,
    estado            VARCHAR(20)    NOT NULL,
    disponible        BOOLEAN        NOT NULL,
    fecha_publicacion TIMESTAMP      NOT NULL
);

CREATE TABLE itemcatalogo.fotografia_prenda (
    id        UUID PRIMARY KEY,
    prenda_id UUID         NOT NULL REFERENCES itemcatalogo.prenda (id),
    url       VARCHAR(500) NOT NULL,
    orden     INTEGER      NOT NULL
);

CREATE TABLE itemcatalogo.moderacion_publicacion (
    id           UUID PRIMARY KEY,
    prenda_id    UUID         NOT NULL REFERENCES itemcatalogo.prenda (id),
    decision     VARCHAR(20)  NOT NULL,
    motivo       VARCHAR(300),
    moderador_id UUID         NOT NULL,
    fecha        TIMESTAMP    NOT NULL
);
