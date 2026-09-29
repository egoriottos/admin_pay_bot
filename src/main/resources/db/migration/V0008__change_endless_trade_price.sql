update tariffs t
set price = 90
from channels c
where t.channel_id = c.id
and c.code = 'ENDLESS_TRADE'
and t.months = 1;

UPDATE tariffs t
SET price = 150
FROM channels c
WHERE t.channel_id = c.id
  AND c.code = 'ENDLESS_TRADE'
  AND t.months = 3;

UPDATE tariffs t
SET price = 250
FROM channels c
WHERE t.channel_id = c.id
  AND c.code = 'ENDLESS_TRADE'
  AND t.months = 6;

UPDATE tariffs t
SET price = 320
FROM channels c
WHERE t.channel_id = c.id
  AND c.code = 'ENDLESS_TRADE'
  AND t.months = 12;

UPDATE tariffs t
SET price = 450
FROM channels c
WHERE t.channel_id = c.id
  AND c.code = 'ENDLESS_TRADE'
  AND t.months = 1200;
