-- ==========================================
-- CTI_PRO -> ENDLESS_TRADE
-- ==========================================

UPDATE channels
SET code             = 'ENDLESS_TRADE',
    name             = 'Endless Trade',
    telegram_chat_id = -1003849044104
WHERE code = 'CTI_PRO';


-- ==========================================
-- ENDLESS TRADE: новые цены
-- ==========================================

UPDATE tariffs t
SET price = CASE t.months
                WHEN 1    THEN 90
                WHEN 3    THEN 208
                WHEN 6    THEN 363
                WHEN 12   THEN 600
                WHEN 1200 THEN 750   -- пожизненная
    END
FROM channels c
WHERE t.channel_id = c.id
  AND c.code = 'ENDLESS_TRADE'
  AND t.months IN (1, 3, 6, 12, 1200);