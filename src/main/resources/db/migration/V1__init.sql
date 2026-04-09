create table if not exists alert_rule(
    id bigserial primary key,
    symbol varchar(20) not null,
    condition_type varchar(30) not null,
    target_value numeric(19,8) not null,
    is_active boolean not null default true,
    created_at timestamp not null default current_timestamp
)