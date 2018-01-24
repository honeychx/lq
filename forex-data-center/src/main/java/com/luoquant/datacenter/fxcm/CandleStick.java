package com.luoquant.datacenter.fxcm;

/**
 * Created by luoqing on 1/23/18.
 */
public class CandleStick {
    private String date;
    private double openAsk;
    private double highAsk;
    private double lowAsk;
    private double closeAsk;

    private double openBid;
    private double highBid;
    private double lowBid;
    private double closeBid;

    private boolean isAsk = true;


    private double open;
    private double high;
    private double low;
    private double close;
    private double rsi;


    public CandleStick(String date, double openBid, double highBid, double lowBid, double closeBid, double closeAsk) {
        this.date = date;
        this.openBid = openBid;
        this.highBid = highBid;
        this.lowBid = lowBid;
        this.closeBid = closeBid;
        this.closeAsk = closeAsk;
    }

    public CandleStick(String date, double openAsk, double highAsk, double lowAsk, double closeAsk, double openBid, double highBid, double lowBid, double closeBid, boolean isAsk) {
        this.date = date;
        this.openAsk = openAsk;
        this.highAsk = highAsk;
        this.lowAsk = lowAsk;
        this.closeAsk = closeAsk;
        this.openBid = openBid;
        this.highBid = highBid;
        this.lowBid = lowBid;
        this.closeBid = closeBid;
        this.isAsk = isAsk;
    }

    public boolean isAsk() {
        return isAsk;
    }

    public void setAsk(boolean ask) {
        isAsk = ask;
    }

    public double getOpen() {
        if (isAsk)
            return openAsk;
        return openBid;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        if (isAsk)
            return highAsk;
        return highBid;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        if (isAsk)
            return lowAsk;
        return lowBid;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        if (isAsk)
            return closeAsk;
        return closeBid;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getOpenAsk() {
        return openAsk;
    }

    public void setOpenAsk(double openAsk) {
        this.openAsk = openAsk;
    }

    public double getHighAsk() {
        return highAsk;
    }

    public void setHighAsk(double highAsk) {
        this.highAsk = highAsk;
    }

    public double getLowAsk() {
        return lowAsk;
    }

    public void setLowAsk(double lowAsk) {
        this.lowAsk = lowAsk;
    }

    public double getCloseAsk() {
        return closeAsk;
    }

    public void setCloseAsk(double closeAsk) {
        this.closeAsk = closeAsk;
    }

    public double getOpenBid() {
        return openBid;
    }

    public void setOpenBid(double openBid) {
        this.openBid = openBid;
    }

    public double getHighBid() {
        return highBid;
    }

    public void setHighBid(double highBid) {
        this.highBid = highBid;
    }

    public double getLowBid() {
        return lowBid;
    }

    public void setLowBid(double lowBid) {
        this.lowBid = lowBid;
    }

    public double getCloseBid() {
        return closeBid;
    }

    public void setCloseBid(double closeBid) {
        this.closeBid = closeBid;
    }

    public double getRsi() {
        return rsi;
    }

    public void setRsi(double rsi) {
        this.rsi = rsi;
    }

    public String toString() {
        if (isAsk) {
            return "CandleStick [date=" + date + ", open=" + openAsk + ", low=" + lowAsk + ",high=" + highAsk + ", close=" + closeAsk + "]";
        } else {
            return "CandleStick [date=" + date + ", open=" + openBid + ", low=" + lowBid + ",high=" + highBid + ", close=" + closeBid + "]";
        }

    }
}
