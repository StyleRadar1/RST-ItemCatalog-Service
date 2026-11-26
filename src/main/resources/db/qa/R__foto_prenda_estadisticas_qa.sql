UPDATE itemcatalogo.fotografia_prenda
SET url = REPLACE(REPLACE(url, 'JUANNICOLASA%20Frontend-Style-Radar', 'JUANNICOLASA/Frontend-Style-Radar'),
                  'JUANNICOLASA Frontend-Style-Radar', 'JUANNICOLASA/Frontend-Style-Radar')
WHERE strpos(url, 'JUANNICOLASA%20Frontend-Style-Radar') > 0
   OR strpos(url, 'JUANNICOLASA Frontend-Style-Radar') > 0;
