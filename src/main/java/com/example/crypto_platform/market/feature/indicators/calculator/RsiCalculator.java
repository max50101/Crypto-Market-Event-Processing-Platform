package com.example.crypto_platform.market.feature.indicators.calculator;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@Component
public class RsiCalculator {
    private static final MathContext MC=new MathContext(20, RoundingMode.HALF_UP);
    public BigDecimal calculateRsi(List<BigDecimal> closes,int period){
        if(closes==null||closes.size()<period+1){
            throw  new IllegalArgumentException("Need at leaset period + 1 closes");
        }

        BigDecimal averageGain=BigDecimal.ZERO;
        BigDecimal averageLoss=BigDecimal.ZERO;

        for(int i=1;i<=period;i++){
            BigDecimal change=closes.get(i).subtract(closes.get(i-1));
            if(change.compareTo(BigDecimal.ZERO)>0){
                averageGain=averageGain.add(change);
            }else if(change.compareTo(BigDecimal.ZERO)<0){
                averageLoss=averageLoss.add(change.abs());
            }
        }

        averageGain=averageGain.divide(BigDecimal.valueOf(period),MC);
        averageLoss=averageLoss.divide(BigDecimal.valueOf(period),MC);

        for(int i=period+1;i<closes.size();i++){
            BigDecimal currentGain=BigDecimal.ZERO;
            BigDecimal currentLoss=BigDecimal.ZERO;
            BigDecimal change=closes.get(i).subtract(closes.get(i-1));
            currentGain=change.compareTo(BigDecimal.ZERO)>0?change:BigDecimal.ZERO;
            currentLoss=change.compareTo(BigDecimal.ZERO)<0?change.abs():BigDecimal.ZERO;
            averageGain=averageGain.
                    multiply(BigDecimal.valueOf(period-1)).
                    add(currentGain).
                    divide(BigDecimal.valueOf(period),MC);

            averageLoss=averageLoss.
                    multiply(BigDecimal.valueOf(period-1)).
                    add(currentLoss).
                    divide(BigDecimal.valueOf(period),MC);
            }
        if(averageLoss.compareTo(BigDecimal.ZERO)==0){
            return BigDecimal.valueOf(100).setScale(2,RoundingMode.HALF_UP);
        }
        BigDecimal rs=averageGain.divide(averageLoss,MC);
        return BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(100).divide(BigDecimal.ONE.add(rs),MC)).setScale(2,RoundingMode.HALF_UP);

        }


    }

