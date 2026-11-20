UPDATE itemcatalogo.prenda SET tipo = 'Saco' WHERE tipo = 'Blazer';

UPDATE itemcatalogo.prenda p
SET marca = a.nombre
FROM itemcatalogo.almacen_resumen a
WHERE p.almacen_id = a.almacen_id;
