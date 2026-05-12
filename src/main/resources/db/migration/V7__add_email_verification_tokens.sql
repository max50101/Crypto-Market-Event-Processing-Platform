create table if not exists telegram_email_verification_tokens(
   id bigserial primary key,
   user_id bigint not null ,
   token varchar(40) not null unique,
   used boolean not null default false,
   expires_at timestamptz not null
)