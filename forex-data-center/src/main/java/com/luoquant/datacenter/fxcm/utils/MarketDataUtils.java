package com.luoquant.datacenter.fxcm.utils;

import com.fxcm.fix.pretrade.MarketDataSnapshot;
import com.luoquant.datacenter.fxcm.CandleStick;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MarketDataUtils {
    private static final String sep = "\t";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat sdfYYMMDD = new SimpleDateFormat("yyyyMMdd");
    public static String formatToCsvLine(MarketDataSnapshot snapshot){
        StringBuilder sb = new StringBuilder();

        sb.append(sdf.format(snapshot.getOpenTimestamp().toDate())).append(sep)
        .append(sdf.format(snapshot.getCloseTimestamp().toDate())).append(sep)
        .append(snapshot.getAskOpen()).append(sep)
        .append(snapshot.getAskHigh()).append(sep)
        .append(snapshot.getAskLow()).append(sep)
        .append(snapshot.getAskClose()).append(sep)

        .append(snapshot.getBidOpen()).append(sep)
        .append(snapshot.getBidHigh()).append(sep)
        .append(snapshot.getBidLow()).append(sep)
        .append(snapshot.getBidClose()).append(sep)

        .append(snapshot.getTickVolume());
        return sb.toString();
    }

    public static List<CandleStick> readCandleFromCsv(List<String> csvLines){
        List<CandleStick> candleStickList = new ArrayList<>();
        for (String csvLine : csvLines){
            CandleStick candleStick = new CandleStick();
            String []fields = csvLine.split(sep);
            try {
                candleStick.setStartDate(sdf.parse(fields[0]));
                candleStick.setEndDate(((sdf.parse(fields[1]))));

                candleStick.setOpenAsk(Double.valueOf(fields[2]));
                candleStick.setHighAsk(Double.valueOf(fields[3]));
                candleStick.setLowAsk(Double.valueOf(fields[4]));
                candleStick.setCloseAsk(Double.valueOf(fields[5]));

                 candleStick.setOpenBid(Double.valueOf(fields[6]));
                candleStick.setHighBid(Double.valueOf(fields[7]));
                candleStick.setLowBid(Double.valueOf(fields[8]));
                candleStick.setCloseBid(Double.valueOf(fields[9]));

                candleStick.setVolume(Double.valueOf(fields[10]));

                candleStickList.add(candleStick);

            }catch (ParseException e){
                e.printStackTrace();
            }
        }
        return candleStickList;
    }

    public static String formatDateToYYMMDD(Date date){
        return sdfYYMMDD.format(date);
    }
}
