INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '17c5aa9a-0a0d-53bd-9229-30be4070cb1f', a.almacen_id, 'Chaqueta de cuero negra', 'Chaqueta', 'Negro', 'Almacén Pruebas QA', 'URBANO', 189000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 00:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = 'fc942335-ab30-350a-b6a5-577cf0ca2fb5'
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, tipo = EXCLUDED.tipo, color = EXCLUDED.color,
                               estilo = EXCLUDED.estilo;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '202e9e28-c141-594d-925b-97db9db82af6', p.id, 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600&q=80', 1
FROM itemcatalogo.prenda p
WHERE p.id = '17c5aa9a-0a0d-53bd-9229-30be4070cb1f'
ON CONFLICT (id) DO UPDATE SET url = EXCLUDED.url;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT 'bd6c234d-96b4-5914-937a-d56206653bce', a.almacen_id, 'Pantalón de dril beige', 'Pantalón', 'Beige', 'Almacén Pruebas QA', 'CASUAL', 129000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 01:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = 'fc942335-ab30-350a-b6a5-577cf0ca2fb5'
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, tipo = EXCLUDED.tipo, color = EXCLUDED.color,
                               estilo = EXCLUDED.estilo;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '9da2c3a1-85c2-56ad-bded-2eb7f570a412', p.id, 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=600&q=80', 1
FROM itemcatalogo.prenda p
WHERE p.id = 'bd6c234d-96b4-5914-937a-d56206653bce'
ON CONFLICT (id) DO UPDATE SET url = EXCLUDED.url;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '1bbc6439-f7b2-58e7-af18-a111d81daeec', a.almacen_id, 'Suéter tejido crudo', 'Suéter', 'Crudo', 'Almacén Pruebas QA', 'VINTAGE', 139000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 02:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = 'fc942335-ab30-350a-b6a5-577cf0ca2fb5'
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, tipo = EXCLUDED.tipo, color = EXCLUDED.color,
                               estilo = EXCLUDED.estilo;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '530ad00d-f3d7-541b-a962-2608789a024e', p.id, 'https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=600&q=80', 1
FROM itemcatalogo.prenda p
WHERE p.id = '1bbc6439-f7b2-58e7-af18-a111d81daeec'
ON CONFLICT (id) DO UPDATE SET url = EXCLUDED.url;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '7b72c48e-abc3-5dfa-99a7-b3abbcf5cb67', a.almacen_id, 'Falda plisada negra', 'Falda', 'Negro', 'Almacén Pruebas QA', 'FORMAL', 99000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 03:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = 'fc942335-ab30-350a-b6a5-577cf0ca2fb5'
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, tipo = EXCLUDED.tipo, color = EXCLUDED.color,
                               estilo = EXCLUDED.estilo;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT 'b89b0039-9379-5cb3-b3aa-03122717f20d', p.id, 'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=600&q=80', 1
FROM itemcatalogo.prenda p
WHERE p.id = '7b72c48e-abc3-5dfa-99a7-b3abbcf5cb67'
ON CONFLICT (id) DO UPDATE SET url = EXCLUDED.url;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '415275d6-6825-555e-850d-97a1607f0698', a.almacen_id, 'Tenis de lona café', 'Calzado', 'Café', 'Almacén Pruebas QA', 'URBANO', 219000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 04:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = 'fc942335-ab30-350a-b6a5-577cf0ca2fb5'
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, tipo = EXCLUDED.tipo, color = EXCLUDED.color,
                               estilo = EXCLUDED.estilo;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '1e21a3a5-0cd2-52c0-8165-76564b538df4', p.id, 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=600&q=80', 1
FROM itemcatalogo.prenda p
WHERE p.id = '415275d6-6825-555e-850d-97a1607f0698'
ON CONFLICT (id) DO UPDATE SET url = EXCLUDED.url;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '3455fbf2-3916-5cbd-972e-74d0afac48b3', a.almacen_id, 'Buzo con capota gris', 'Buzo', 'Gris', 'Almacén Pruebas QA 2', 'DEPORTIVO', 119000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 05:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = '3786bafb-1cde-32c3-a327-8e48202d52a2'
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, tipo = EXCLUDED.tipo, color = EXCLUDED.color,
                               estilo = EXCLUDED.estilo;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '55d74392-9a5b-521d-b376-6659e56892e6', p.id, 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=600&q=80', 1
FROM itemcatalogo.prenda p
WHERE p.id = '3455fbf2-3916-5cbd-972e-74d0afac48b3'
ON CONFLICT (id) DO UPDATE SET url = EXCLUDED.url;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '83ba7acc-dab5-561d-9444-1d966c29e6b2', a.almacen_id, 'Camisa de chambray azul', 'Camisa', 'Azul', 'Almacén Pruebas QA 2', 'CASUAL', 109000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 06:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = '3786bafb-1cde-32c3-a327-8e48202d52a2'
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, tipo = EXCLUDED.tipo, color = EXCLUDED.color,
                               estilo = EXCLUDED.estilo;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '91ae3c65-c88c-5709-9baa-746b328d2b79', p.id, 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&q=80', 1
FROM itemcatalogo.prenda p
WHERE p.id = '83ba7acc-dab5-561d-9444-1d966c29e6b2'
ON CONFLICT (id) DO UPDATE SET url = EXCLUDED.url;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT 'ca1153c8-a5ea-58d9-b1f5-df7ce9ff551d', a.almacen_id, 'Vestido corto blanco', 'Vestido', 'Blanco', 'Almacén Pruebas QA 2', 'CASUAL', 179000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 07:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = '3786bafb-1cde-32c3-a327-8e48202d52a2'
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, tipo = EXCLUDED.tipo, color = EXCLUDED.color,
                               estilo = EXCLUDED.estilo;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '6341b72a-0d83-5313-8c6c-0b4da756beb6', p.id, 'https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?w=600&q=80', 1
FROM itemcatalogo.prenda p
WHERE p.id = 'ca1153c8-a5ea-58d9-b1f5-df7ce9ff551d'
ON CONFLICT (id) DO UPDATE SET url = EXCLUDED.url;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '8713d86f-ef17-5a9f-9836-b91c98a10667', a.almacen_id, 'Saco de paño azul', 'Saco', 'Azul', 'Almacén Pruebas QA 2', 'FORMAL', 249000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 08:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = '3786bafb-1cde-32c3-a327-8e48202d52a2'
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, tipo = EXCLUDED.tipo, color = EXCLUDED.color,
                               estilo = EXCLUDED.estilo;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '7f914994-6970-5043-b1c3-2b02ae6e58d8', p.id, 'https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=600&q=80', 1
FROM itemcatalogo.prenda p
WHERE p.id = '8713d86f-ef17-5a9f-9836-b91c98a10667'
ON CONFLICT (id) DO UPDATE SET url = EXCLUDED.url;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT 'f6aebeff-272e-5197-9d0d-4d065f27ef97', a.almacen_id, 'Camiseta estampada cruda', 'Camiseta', 'Crudo', 'Almacén Pruebas QA 2', 'URBANO', 45000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 09:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = '3786bafb-1cde-32c3-a327-8e48202d52a2'
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, tipo = EXCLUDED.tipo, color = EXCLUDED.color,
                               estilo = EXCLUDED.estilo;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '3e2e78dd-f3d1-5eae-95fc-297d317a5394', p.id, 'https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=600&q=80', 1
FROM itemcatalogo.prenda p
WHERE p.id = 'f6aebeff-272e-5197-9d0d-4d065f27ef97'
ON CONFLICT (id) DO UPDATE SET url = EXCLUDED.url;
