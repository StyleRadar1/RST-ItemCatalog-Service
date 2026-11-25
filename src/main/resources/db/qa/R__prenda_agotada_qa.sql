INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT 'c3bc58c3-e0e2-43cd-a61a-1279a3894fe7', a.almacen_id, 'Camiseta agotada QA notificaciones', 'Camiseta', 'Gris',
       'Luna Store', 'CASUAL', 50000, 'APROBADA', false, TIMESTAMP '2026-11-25 00:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = 'fc942335-ab30-350a-b6a5-577cf0ca2fb5'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '7fe9b5a0-257c-4f65-8fd2-6781022ff31d', p.id, 'https://picsum.photos/seed/agotada/600', 1
FROM itemcatalogo.prenda p
WHERE p.id = 'c3bc58c3-e0e2-43cd-a61a-1279a3894fe7'
ON CONFLICT (id) DO NOTHING;
