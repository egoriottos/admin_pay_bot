UPDATE tariffs t
SET price = 52
FROM channels c
WHERE t.channel_id = c.id
  AND c.code = 'ELITE_TREND'
  AND t.months = 1;

UPDATE tariffs t
SET price = 120
FROM channels c
WHERE t.channel_id = c.id
  AND c.code = 'ELITE_TREND'
  AND t.months = 3;

UPDATE tariffs t
SET price = 210
FROM channels c
WHERE t.channel_id = c.id
  AND c.code = 'ELITE_TREND'
  AND t.months = 6;

UPDATE tariffs t
SET price = 387
FROM channels c
WHERE t.channel_id = c.id
  AND c.code = 'ELITE_TREND'
  AND t.months = 12;


-- ==========================================
-- Пожизненная подписка (months = 0, $600) — для всех индикаторов
-- ==========================================

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 0, 600
FROM channels
WHERE code = 'ELITE_TREND';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 0, 600
FROM channels
WHERE code = 'TRADE_BE';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 0, 600
FROM channels
WHERE code = 'CTI_PRO';