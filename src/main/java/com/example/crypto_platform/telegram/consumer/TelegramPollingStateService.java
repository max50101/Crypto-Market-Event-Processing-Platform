package com.example.crypto_platform.telegram.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramPollingStateService {
    private static final long STATE_ID=1L;
    private final JdbcTemplate jdbcTemplate;

    public Long getCurrentOffset(){
        Long lastProcessed=jdbcTemplate.query("select last_processed_update_id from telegram_polling_state where id=?",
                rs->rs.next()? rs.getLong(1):null,
                STATE_ID);
        return lastProcessed!=null? lastProcessed+1: null;
    }

    public void saveLastProcessedUpdateId(Long updateId){
        jdbcTemplate.update("insert into telegram_polling_state(id,last_processed_update_id) " +
                "values(?,?) " +
                "on conflict(id) do update " +
                " set last_processed_update_id=excluded.last_processed_update_id",STATE_ID,updateId);
    }

}
