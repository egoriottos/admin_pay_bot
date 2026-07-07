-- ==========================================
-- CHANNELS
-- ==========================================

INSERT INTO channels (id, code, name, telegram_chat_id)
VALUES
    (nextval('channels_seq'), 'ELITE_TREND', 'Elite Trend', '-1003851740846'),
    (nextval('channels_seq'), 'CTI_PRO', 'CTI Pro', '-1003848210143'),
    (nextval('channels_seq'), 'TRADE_BE', 'Trade B&E', '-1003962895871');

-- ==========================================
-- ELITE TREND
-- ==========================================

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 1, 70
FROM channels
WHERE code = 'ELITE_TREND';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 3, 160
FROM channels
WHERE code = 'ELITE_TREND';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 6, 284
FROM channels
WHERE code = 'ELITE_TREND';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 12, 520
FROM channels
WHERE code = 'ELITE_TREND';

-- ==========================================
-- TRADE B&E
-- ==========================================

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 1, 50
FROM channels
WHERE code = 'TRADE_BE';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 3, 115
FROM channels
WHERE code = 'TRADE_BE';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 6, 205
FROM channels
WHERE code = 'TRADE_BE';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 12, 380
FROM channels
WHERE code = 'TRADE_BE';

-- ==========================================
-- CTI PRO
-- ==========================================

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 1, 80
FROM channels
WHERE code = 'CTI_PRO';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 3, 180
FROM channels
WHERE code = 'CTI_PRO';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 6, 324
FROM channels
WHERE code = 'CTI_PRO';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 12, 594
FROM channels
WHERE code = 'CTI_PRO';