alter table payments
    add column chat_id BIGINT DEFAULT 0;

alter table payments
    add column message_id INTEGER DEFAULT 0;
CREATE INDEX idx_payments_chat_id ON payments(chat_id);
CREATE INDEX idx_payments_message_id ON payments(message_id);