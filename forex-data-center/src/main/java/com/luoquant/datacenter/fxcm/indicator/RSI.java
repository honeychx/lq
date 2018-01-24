package com.luoquant.datacenter.fxcm.indicator;

import java.util.ArrayList;
import java.util.List;

public class RSI {
    public static final double DEFAULT_INVALID_NUM = 0.0;
    private List<Double> priceSeries;
    private List<Double> rsiValue;
    private int period;

    public RSI(List<Double> priceSeries, int period) {
        this.priceSeries = new ArrayList<>(priceSeries.size());
        this.priceSeries = priceSeries;
        this.period = period;
        rsiValue = new ArrayList<>(priceSeries.size());
        doCalculateRsiValue();
    }

    public double getRsiByRs(double rs) {
        return 100 - 100 / (1 + rs);
    }

    public List<Double> getRsiValue(){
        return this.rsiValue;
    }

    public List<Double> doCalculateRsiValue() {
        double firstAvgGain = 0.0;
        double firstAvgLoss = 0.0;

        rsiValue.add(DEFAULT_INVALID_NUM);
        for (int i = 1; i < this.period; i++) {
            if (priceSeries.get(i) > priceSeries.get(i - 1)) {
                firstAvgGain += priceSeries.get(i) - priceSeries.get(i - 1);
            } else {
                firstAvgLoss += priceSeries.get(i - 1) - priceSeries.get(i);
            }
            rsiValue.add(DEFAULT_INVALID_NUM);
        }
        firstAvgGain /= this.period;
        firstAvgLoss /= this.period;
        double rs = firstAvgGain / firstAvgLoss;
        rsiValue.add(getRsiByRs(rs));

        double avgGain = firstAvgGain;
        double avgLoss = firstAvgLoss;
        for (int i = this.period + 1; i < priceSeries.size(); i++) {
            double diff = this.priceSeries.get(i) - this.priceSeries.get(i - 1);
            if (diff > 0) {
                avgGain = (firstAvgGain * (this.period - 1) + diff) / this.period;
                firstAvgGain = avgGain;
            } else {
                avgLoss = (firstAvgLoss * (this.period - 1) + diff) / this.period;
                firstAvgLoss = avgLoss;
            }

            rsiValue.add(getRsiByRs(avgGain / avgLoss));
        }
        return rsiValue;
    }

    public static void main(String[] args) {
        List<Double> series = new ArrayList<>();
        String priceSeries = "44.34, 44.09, 44.15, 43.61, 44.33, 44.83, 45.10, 45.42, 45.84, 46.08, 45.89, 46.03, 45.61, 46.28, 46.28, 46.00, 46.03, 46.41, 46.22, 45.64, 46.21, 46.25, 45.71, 46.45, 45.78, 45.35, 44.03, 44.18, 44.22, 44.57, 43.42, 42.66, 43.13";
        for (String price : priceSeries.split(",")){
            series.add(Double.valueOf(price));
        }
        RSI rsi = new RSI(series, 14);
        List<Double> rsiValues = rsi.getRsiValue();
        for (double rsiValue : rsiValues){
            System.out.println(rsiValue);
        }
    }

}
