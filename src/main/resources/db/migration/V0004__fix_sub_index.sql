ALTER TABLE subscriptions DROP CONSTRAINT uq_subscription_user_channel;

CREATE UNIQUE INDEX uq_subscription_user_channel_active
    ON subscriptions (user_id, channel_id)
    WHERE status = 'ACTIVE';