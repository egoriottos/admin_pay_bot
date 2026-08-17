ALTER TABLE subscriptions DROP CONSTRAINT IF EXISTS uq_subscription_user_channel;

CREATE UNIQUE INDEX IF NOT EXISTS uq_subscription_user_channel_active
    ON subscriptions (user_id, channel_id)
    WHERE status = 'ACTIVE';