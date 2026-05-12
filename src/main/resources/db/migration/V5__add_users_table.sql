create table if not exists users(
id bigserial primary key,
telegram_user_id bigint not null unique,
telegram_chat_id bigint not null,
telegram_user_name varchar(40) not null,
user_role varchar(40) not null,
email varchar(60),
email_verified boolean not null default false,
conversation_state varchar(40) default 'IDLE'
);

