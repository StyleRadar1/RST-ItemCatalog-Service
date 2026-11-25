UPDATE itemcatalogo.fotografia_prenda
SET url = REPLACE(TRIM(url), ' ', '%20')
WHERE url <> REPLACE(TRIM(url), ' ', '%20');
