-- ==========================================
-- Пожизненная подписка: months 0 -> 1200 (100 лет)
-- чтобы now.plusMonths(tariff.getMonths()) не давал дату в прошлом
-- ==========================================

UPDATE tariffs t
SET months = 1200
FROM channels c
WHERE t.channel_id = c.id
  AND c.code IN ('ELITE_TREND', 'TRADE_BE', 'CTI_PRO')
  AND t.months = 0
  AND t.price = 600;