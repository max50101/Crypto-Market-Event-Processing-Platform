package com.example.crypto_platform.market.feature.chart;

import com.example.crypto_platform.market.dto.BinanceKline;
import org.knowm.xchart.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
@Service
public class XChartGenerator {
    public byte[] generatePriceChart(List<BinanceKline> candles, String symbol, String interval) {
        BinanceKline lastCandle=candles.get(candles.size()-1);
        OHLCChart chart = new OHLCChartBuilder()
                .width(1000)
                .height(600)
                .title(symbol + " " + interval + "Price")
                .xAxisTitle("Time")
                .yAxisTitle("Price")
                .build();
        List<Date> kData = candles.stream()
                .map(kline -> new Date(kline.openTime()))
                .toList();
        List<Double> open = candles.stream()
                .map(kline -> kline.open().doubleValue())
                .toList();
        List<Double> close = candles.stream()
                .map(kline -> kline.close().doubleValue())
                .toList();
        List<Double> high = candles.stream()
                .map(kline -> kline.high().doubleValue())
                .toList();
        List<Double> low = candles.stream()
                .map(kline -> kline.low().doubleValue())
                .toList();
        chart.getStyler().setDefaultSeriesRenderStyle(OHLCSeries.OHLCSeriesRenderStyle.Candle);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setXAxisLabelRotation(45);
        chart.getStyler().setDatePattern(resolveDatePattern(interval));
        OHLCSeries series=chart.addSeries(symbol,kData,open,high,low,close);
        series.setDownColor(Color.RED);
        series.setUpColor(Color.GREEN);

        double lastPrice=lastCandle.close().doubleValue();
        DecimalFormat priceFormat=new DecimalFormat("#,##0.00");
        String lastPriceText=priceFormat.format(lastPrice);
        chart.addAnnotation(new AnnotationLine(
                lastPrice,false,false
        ));
        chart.addAnnotation(new AnnotationText(
                "Last"+lastPriceText,
                candles.get(0).openTime(),
                lastPrice*1.01,
                false
        ));
        return toPngBytes(chart);
    }

    private byte[] toPngBytes(OHLCChart chart){
        try(ByteArrayOutputStream outputStream=new ByteArrayOutputStream()){
            BitmapEncoder.saveBitmap(chart,outputStream, BitmapEncoder.BitmapFormat.PNG);
            return outputStream.toByteArray();
        }catch (IOException e){
            throw  new IllegalStateException("Failed to generate price chart");
        }
    }

    private String resolveDatePattern(String interval) {
        return switch (interval) {
            case "1m", "3m", "5m", "15m", "30m" -> "HH:mm";
            case "1h", "2h", "4h", "6h", "8h", "12h" -> "MM-dd HH:mm";
            default -> "yyyy-MM-dd";
        };
    }
}
