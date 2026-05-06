package com.example.crypto_platform.market.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceTradeMessage {
    private String stream;
    private TradeData data;

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public TradeData getData() {
        return data;
    }

    public void setData(TradeData tradeData) {
        this.data = tradeData;
    }

    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TradeData{
        private String e;
        private String s;
        private String p;
        private Long T;

        public String getE() {
            return e;
        }

        public void setE(String e) {
            this.e = e;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public Long getT() {
            return T;
        }

        public void setT(Long t) {
            T = t;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }
    }
 }
