package com.luoquant.datacenter.fxcm.utils;

import com.fxcm.fix.pretrade.MarketDataSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String formatDateToYYMMDD(Date date){
        return sdfYYMMDD.format(date);
    }
}
