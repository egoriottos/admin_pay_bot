-- ==========================================
-- PRESSURE TRADE
-- ==========================================

INSERT INTO channels (id, code, name, telegram_chat_id)
VALUES (nextval('channels_seq'), 'PRESSURE_TRADE', 'Pressure trade', -1004496339730);

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 1, 67 FROM channels WHERE code = 'PRESSURE_TRADE';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 3, 151 FROM channels WHERE code = 'PRESSURE_TRADE';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 6, 320 FROM channels WHERE code = 'PRESSURE_TRADE';

INSERT INTO tariffs (id, channel_id, months, price)
SELECT nextval('tariffs_seq'), id, 12, 550 FROM channels WHERE code = 'PRESSURE_TRADE';