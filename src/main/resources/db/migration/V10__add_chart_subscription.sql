create table if not exists chart_subscriptions(
    id bigserial primary key,

    user_id bigint not null,

    symbol VARCHAR(30) not null,
    interval VARCHAR(10) not null,

    frequency_minutes bigint not null,
    status varchar(20) not null,
    send_telegram boolean not null default true,
    send_email boolean not null default false,

    created_at timestamptz not null default  CURRENT_TIMESTAMP,
    last_sent_at timestamptz,
    next_send_at timestamptz NOT NULL

);

CREATE INDEX idx_chat_subscriptions_due
on chart_subscriptions(status, next_send_at);