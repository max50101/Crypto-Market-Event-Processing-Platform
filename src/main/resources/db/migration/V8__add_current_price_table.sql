create table if not exists current_prices(
symbol varchar(60) primary key,
price bigint not null,
updated_at timestamptz not null
)