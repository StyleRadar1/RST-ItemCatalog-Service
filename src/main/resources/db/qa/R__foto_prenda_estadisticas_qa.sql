UPDATE itemcatalogo.fotografia_prenda
SET url = regexp_replace(url, 'JUANNICOLASA(%20| )Frontend-Style-Radar', 'JUANNICOLASA/Frontend-Style-Radar', 'g')
WHERE url ~ 'JUANNICOLASA(%20| )Frontend-Style-Radar';
