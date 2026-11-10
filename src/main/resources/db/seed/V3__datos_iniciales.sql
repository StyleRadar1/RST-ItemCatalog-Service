-- Datos de ejemplo para probar en QA; los almacenes usan los mismos ids que ms-geolocalizacion
INSERT INTO itemcatalogo.almacen_resumen (almacen_id, nombre, reputacion, activo) VALUES
    ('a1111111-1111-1111-1111-111111111111', 'Sastrería Antonia', 4.80, TRUE),
    ('a2222222-2222-2222-2222-222222222222', 'Calle 85 Store', 4.20, TRUE),
    ('a3333333-3333-3333-3333-333333333333', 'Nora Vintage', 3.90, TRUE);

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible, fecha_publicacion) VALUES
    ('b1111111-1111-1111-1111-111111111111', 'a1111111-1111-1111-1111-111111111111', 'Blazer de lino', 'Blazer', 'Beige', 'Antonia', 'Formal', 280000, 'APROBADA', TRUE, NOW()),
    ('b2222222-2222-2222-2222-222222222222', 'a2222222-2222-2222-2222-222222222222', 'Chaqueta denim oversize', 'Chaqueta', 'Azul', 'Levis', 'Urbano', 190000, 'APROBADA', TRUE, NOW()),
    ('b3333333-3333-3333-3333-333333333333', 'a2222222-2222-2222-2222-222222222222', 'Hoodie gráfico', 'Buzo', 'Negro', 'Calle 85', 'Urbano', 120000, 'APROBADA', TRUE, NOW() - INTERVAL '10 days'),
    ('b4444444-4444-4444-4444-444444444444', 'a3333333-3333-3333-3333-333333333333', 'Chaqueta de cuero 90s', 'Chaqueta', 'Café', 'Sin marca', 'Vintage', 150000, 'PENDIENTE', TRUE, NOW());

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden) VALUES
    ('c1111111-1111-1111-1111-111111111111', 'b1111111-1111-1111-1111-111111111111', 'https://picsum.photos/seed/blazer/600', 1),
    ('c2222222-2222-2222-2222-222222222222', 'b2222222-2222-2222-2222-222222222222', 'https://picsum.photos/seed/denim/600', 1),
    ('c3333333-3333-3333-3333-333333333333', 'b3333333-3333-3333-3333-333333333333', 'https://picsum.photos/seed/hoodie/600', 1),
    ('c4444444-4444-4444-4444-444444444444', 'b4444444-4444-4444-4444-444444444444', 'https://picsum.photos/seed/cuero/600', 1);
