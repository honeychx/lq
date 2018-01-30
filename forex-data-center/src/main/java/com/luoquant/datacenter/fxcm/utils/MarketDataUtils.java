package com.luoquant.datacenter.fxcm.utils;

import com.fxcm.fix.pretrade.MarketDataSnapshot;

import java.text.SimpleDateFormat;

public class MarketDataUtils {
    private static final String sep = "\t";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static String formatToCsvLine(MarketDataSnapshot snapshot){
        StringBuilder sb = new StringBuilder();
        sb.append(sdf.format(snapshot.getDate().toDate())).append(sep)
        .append(snapshot.getAskOpen()).append(sep)
        .append(snapshot.getAskHigh()).append(sep)
        .append(snapshot.getAskLow()).append(sep)
        .append(snapshot.getAskClose()).append(sep)

        .append(snapshot.getBidOpen()).append(sep)
        .append(snapshot.getBidHigh()).append(sep)
        .append(snapshot.getBidLow()).append(sep)
        .append(snapshot.getBidClose()).append(sep);


        return sb.toString();

    }
}
