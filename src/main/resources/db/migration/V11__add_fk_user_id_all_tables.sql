DELETE FROM alert_rule;
alter table alert_rule
ADD CONSTRAINT  fk_alert_user
FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE cascade ;

alter table chart_subscriptions
    ADD CONSTRAINT  fk_alert_user
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE cascade ;

alter table telegram_email_verification_tokens
    ADD CONSTRAINT  fk_alert_user
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE cascade ;


CREATE INDEX  idx_alert_user_id on alert_rule(user_id);
create index idx_chart_subscription_user_id on chart_subscriptions(user_id);
create index idx_telegram_email_verification_token on telegram_email_verification_tokens(user_id);