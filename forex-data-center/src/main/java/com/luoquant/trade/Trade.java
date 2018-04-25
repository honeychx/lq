package com.luoquant.trade;

import java.util.Calendar;
import java.util.UUID;

/**
 * Created by luoqing on 23/04/2018.
 */
public class Trade {
    private UUID uuid;

    private String instrument;
    private double entryPrice;
    private double exitPrice;

    private double stopLossPrice;
    private double takeProfitPrice;

    private double commissionRate;
    private double commission;

    private double profit;
    private int quantity;

    private Calendar entryTime;
    private Calendar exitTime;

    private Constants.DirectionType directionType;


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public double getEntryPrice() {
        return entryPrice;
    }

    public void setEntryPrice(double entryPrice) {
        this.entryPrice = entryPrice;
    }

    public double getExitPrice() {
        return exitPrice;
    }

    public void setExitPrice(double exitPrice) {
        this.exitPrice = exitPrice;
    }

    public double getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(double stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public double getTakeProfitPrice() {
        return takeProfitPrice;
    }

    public void setTakeProfitPrice(double takeProfitPrice) {
        this.takeProfitPrice = takeProfitPrice;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Calendar getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Calendar entryTime) {
        this.entryTime = entryTime;
    }

    public Calendar getExitTime() {
        return exitTime;
    }

    public void setExitTime(Calendar exitTime) {
        this.exitTime = exitTime;
    }

    public Constants.DirectionType getDirectionType() {
        return directionType;
    }

    public void setDirectionType(Constants.DirectionType directionType) {
        this.directionType = directionType;
    }
}
