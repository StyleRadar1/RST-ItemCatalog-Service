INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '17c5aa9a-0a0d-53bd-9229-30be4070cb1f', a.almacen_id, 'Chaqueta denim clásica', 'Chaqueta', 'Azul', 'Almacén Pruebas QA', 'URBANO', 189000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 00:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = 'fc942335-ab30-350a-b6a5-577cf0ca2fb5'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '202e9e28-c141-594d-925b-97db9db82af6', p.id, 'https://picsum.photos/seed/sr-chaqueta-denim/600/800', 1
FROM itemcatalogo.prenda p
WHERE p.id = '17c5aa9a-0a0d-53bd-9229-30be4070cb1f'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT 'bd6c234d-96b4-5914-937a-d56206653bce', a.almacen_id, 'Pantalón de dril beige', 'Pantalón', 'Beige', 'Almacén Pruebas QA', 'CASUAL', 129000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 01:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = 'fc942335-ab30-350a-b6a5-577cf0ca2fb5'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '9da2c3a1-85c2-56ad-bded-2eb7f570a412', p.id, 'https://picsum.photos/seed/sr-pantalon-dril/600/800', 1
FROM itemcatalogo.prenda p
WHERE p.id = 'bd6c234d-96b4-5914-937a-d56206653bce'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '1bbc6439-f7b2-58e7-af18-a111d81daeec', a.almacen_id, 'Suéter tejido gris', 'Suéter', 'Gris', 'Almacén Pruebas QA', 'CASUAL', 139000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 02:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = 'fc942335-ab30-350a-b6a5-577cf0ca2fb5'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '530ad00d-f3d7-541b-a962-2608789a024e', p.id, 'https://picsum.photos/seed/sr-sueter-gris/600/800', 1
FROM itemcatalogo.prenda p
WHERE p.id = '1bbc6439-f7b2-58e7-af18-a111d81daeec'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '7b72c48e-abc3-5dfa-99a7-b3abbcf5cb67', a.almacen_id, 'Falda plisada negra', 'Falda', 'Negro', 'Almacén Pruebas QA', 'FORMAL', 99000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 03:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = 'fc942335-ab30-350a-b6a5-577cf0ca2fb5'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT 'b89b0039-9379-5cb3-b3aa-03122717f20d', p.id, 'https://picsum.photos/seed/sr-falda-plisada/600/800', 1
FROM itemcatalogo.prenda p
WHERE p.id = '7b72c48e-abc3-5dfa-99a7-b3abbcf5cb67'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '415275d6-6825-555e-850d-97a1607f0698', a.almacen_id, 'Tenis blancos urbanos', 'Calzado', 'Blanco', 'Almacén Pruebas QA', 'DEPORTIVO', 219000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 04:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = 'fc942335-ab30-350a-b6a5-577cf0ca2fb5'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '1e21a3a5-0cd2-52c0-8165-76564b538df4', p.id, 'https://picsum.photos/seed/sr-tenis-blancos/600/800', 1
FROM itemcatalogo.prenda p
WHERE p.id = '415275d6-6825-555e-850d-97a1607f0698'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '3455fbf2-3916-5cbd-972e-74d0afac48b3', a.almacen_id, 'Buzo deportivo azul', 'Buzo', 'Azul', 'Almacén Pruebas QA 2', 'DEPORTIVO', 119000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 05:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = '3786bafb-1cde-32c3-a327-8e48202d52a2'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '55d74392-9a5b-521d-b376-6659e56892e6', p.id, 'https://picsum.photos/seed/sr-buzo-azul/600/800', 1
FROM itemcatalogo.prenda p
WHERE p.id = '3455fbf2-3916-5cbd-972e-74d0afac48b3'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '83ba7acc-dab5-561d-9444-1d966c29e6b2', a.almacen_id, 'Camisa de lino blanca', 'Camisa', 'Blanco', 'Almacén Pruebas QA 2', 'FORMAL', 109000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 06:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = '3786bafb-1cde-32c3-a327-8e48202d52a2'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '91ae3c65-c88c-5709-9baa-746b328d2b79', p.id, 'https://picsum.photos/seed/sr-camisa-lino/600/800', 1
FROM itemcatalogo.prenda p
WHERE p.id = '83ba7acc-dab5-561d-9444-1d966c29e6b2'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT 'ca1153c8-a5ea-58d9-b1f5-df7ce9ff551d', a.almacen_id, 'Vestido midi café', 'Vestido', 'Café', 'Almacén Pruebas QA 2', 'VINTAGE', 179000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 07:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = '3786bafb-1cde-32c3-a327-8e48202d52a2'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '6341b72a-0d83-5313-8c6c-0b4da756beb6', p.id, 'https://picsum.photos/seed/sr-vestido-midi/600/800', 1
FROM itemcatalogo.prenda p
WHERE p.id = 'ca1153c8-a5ea-58d9-b1f5-df7ce9ff551d'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT '8713d86f-ef17-5a9f-9836-b91c98a10667', a.almacen_id, 'Saco de paño negro', 'Saco', 'Negro', 'Almacén Pruebas QA 2', 'FORMAL', 249000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 08:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = '3786bafb-1cde-32c3-a327-8e48202d52a2'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '7f914994-6970-5043-b1c3-2b02ae6e58d8', p.id, 'https://picsum.photos/seed/sr-saco-pano/600/800', 1
FROM itemcatalogo.prenda p
WHERE p.id = '8713d86f-ef17-5a9f-9836-b91c98a10667'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.prenda (id, almacen_id, nombre, tipo, color, marca, estilo, precio, estado, disponible,
                                 fecha_publicacion)
SELECT 'f6aebeff-272e-5197-9d0d-4d065f27ef97', a.almacen_id, 'Camiseta básica crudo', 'Camiseta', 'Crudo', 'Almacén Pruebas QA 2', 'CASUAL', 45000, 'APROBADA', true,
       TIMESTAMP '2026-11-25 09:00:00'
FROM itemcatalogo.almacen_resumen a
WHERE a.almacen_id = '3786bafb-1cde-32c3-a327-8e48202d52a2'
ON CONFLICT (id) DO NOTHING;

INSERT INTO itemcatalogo.fotografia_prenda (id, prenda_id, url, orden)
SELECT '3e2e78dd-f3d1-5eae-95fc-297d317a5394', p.id, 'https://picsum.photos/seed/sr-camiseta-crudo/600/800', 1
FROM itemcatalogo.prenda p
WHERE p.id = 'f6aebeff-272e-5197-9d0d-4d065f27ef97'
ON CONFLICT (id) DO NOTHING;
