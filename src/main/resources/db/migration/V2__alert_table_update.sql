ALTER TABLE alert_rule ADD COLUMN user_id BIGINT NOT NULL;

CREATE INDEX idx_alerts_symbol_status
ON alert_rule(symbol,status);