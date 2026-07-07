-- ==========================================
-- USERS
-- ==========================================

CREATE SEQUENCE users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE users (
                       id BIGINT NOT NULL,

                       telegram_id BIGINT NOT NULL UNIQUE,
                       username VARCHAR(255),
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),

                       language VARCHAR(20) NOT NULL,

                       created_at TIMESTAMP NOT NULL DEFAULT now(),
                       last_activity TIMESTAMP,

                       CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE INDEX idx_users_telegram_id
    ON users (telegram_id);



-- ==========================================
-- CHANNELS
-- ==========================================

CREATE SEQUENCE channels_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE channels (
                          id BIGINT NOT NULL,

                          code VARCHAR(100) NOT NULL UNIQUE,
                          name VARCHAR(255) NOT NULL,

                          telegram_chat_id BIGINT NOT NULL UNIQUE,

                          CONSTRAINT pk_channels PRIMARY KEY (id)
);



-- ==========================================
-- TARIFFS
-- ==========================================

CREATE SEQUENCE tariffs_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE tariffs (
                         id BIGINT NOT NULL,

                         channel_id BIGINT NOT NULL,

                         months INTEGER NOT NULL,

                         price NUMERIC(19,2) NOT NULL,

                         CONSTRAINT pk_tariffs PRIMARY KEY (id),

                         CONSTRAINT fk_tariff_channel
                             FOREIGN KEY (channel_id)
                                 REFERENCES channels(id)
                                 ON DELETE CASCADE
);

CREATE INDEX idx_tariffs_channel_id
    ON tariffs(channel_id);



-- ==========================================
-- SUBSCRIPTIONS
-- ==========================================

CREATE SEQUENCE subscriptions_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE subscriptions (
                               id BIGINT NOT NULL,

                               user_id BIGINT NOT NULL,
                               channel_id BIGINT NOT NULL,
                               tariff_id BIGINT NOT NULL,

                               status VARCHAR(30) NOT NULL,

                               start_date TIMESTAMP NOT NULL,
                               expire_date TIMESTAMP NOT NULL,

                               reminder_sent BOOLEAN NOT NULL DEFAULT FALSE,

                               CONSTRAINT pk_subscriptions PRIMARY KEY (id),

                               CONSTRAINT fk_subscription_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_subscription_channel
                                   FOREIGN KEY (channel_id)
                                       REFERENCES channels(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_subscription_tariff
                                   FOREIGN KEY (tariff_id)
                                       REFERENCES tariffs(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT uq_subscription_user_channel
                                   UNIQUE (user_id, channel_id)
);

CREATE INDEX idx_subscriptions_user
    ON subscriptions(user_id);

CREATE INDEX idx_subscriptions_channel
    ON subscriptions(channel_id);

CREATE INDEX idx_subscriptions_expire
    ON subscriptions(expire_date);



-- ==========================================
-- PAYMENTS
-- ==========================================

CREATE SEQUENCE payments_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE payments (
                          id BIGINT NOT NULL,

                          invoice_id VARCHAR(255) NOT NULL UNIQUE,
                          order_id VARCHAR(255),

                          user_id BIGINT NOT NULL,
                          subscription_id BIGINT,

                          amount NUMERIC(19,2) NOT NULL,

                          status VARCHAR(30) NOT NULL,

                          created_at TIMESTAMP NOT NULL DEFAULT now(),
                          paid_at TIMESTAMP,

                          CONSTRAINT pk_payments PRIMARY KEY (id),

                          CONSTRAINT fk_payment_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_payment_subscription
                              FOREIGN KEY (subscription_id)
                                  REFERENCES subscriptions(id)
                                  ON DELETE SET NULL
);

CREATE INDEX idx_payments_user
    ON payments(user_id);

CREATE INDEX idx_payments_subscription
    ON payments(subscription_id);

CREATE INDEX idx_payments_status
    ON payments(status);