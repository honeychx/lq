package com.luoquant.datacenter.fxcm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.fxcm.fix.Instrument;
import com.fxcm.fix.UTCDate;
import com.fxcm.fix.UTCTimeOnly;
import com.luoquant.datacenter.fxcm.strategy.RsiStrategy;
import com.luoquant.datacenter.fxcm.strategy.base.Strategy;
import com.luoquant.datacenter.fxcm.strategy.base.StrategyResult;

public class Tester {
    public static void main(String[] args) {
        List<CandleStick> candleSticksList;
        UTCDate startDate;
        UTCTimeOnly startTime;
        double stopLossPerc=0.003;
        double takeProfitPerc=0.003;
        int bottomRSI=10;
        List <StrategyResult> strategySummary = new ArrayList<StrategyResult>();
        String instrument = "GBP/JPY";
        Instrument asset = new Instrument(instrument);
        // get the current time and roll back 1 year
        Calendar instance = Calendar.getInstance();
        instance.roll(Calendar.DAY_OF_WEEK, -1);
        System.out.println(instance.getTime());
        // set the starting date and time of the historical data
        startDate = new UTCDate(instance.getTime());
        startTime = new UTCTimeOnly(instance.getTime());
        try {
            // create an instance of the JavaFixHistoryMiner
            /*70934807 7519 http://www.fxcorporate.com*/
            HistoryMiner miner = new HistoryMiner("70934807", "7519", "Demo", startDate, startTime, asset);
            // login to the api
            miner.login(miner, miner);
            // keep mining for historical data before logging out
            while (miner.stillMining) {
                Thread.sleep(1000);
            }
            Thread.sleep(1000);
            // log out of the api
            miner.logout(miner, miner);
            // convert rates to candlesticks
            miner.convertHistoricalRatesToCandleSticks(); // display the collected rates
            miner.displayHistory();

            candleSticksList = miner.candleStickList;
            RsiStrategy.calculateRsiForDataSet(candleSticksList, 14);
            for (double i = stopLossPerc; i < stopLossPerc + 0.005; i += 0.0005) {
                // adjust stop loss
                for (double j = takeProfitPerc; j < takeProfitPerc + 0.005; j += 0.0005) { // adjust take profit
                    for (int k = bottomRSI; k <= bottomRSI + 20; k += 5) { // adjust bottom and top rsi
                        Strategy rsiSignals = new RsiStrategy(i, j, k);
                        StrategyResult sr = ((RsiStrategy) rsiSignals).runStrategy(candleSticksList);
                        strategySummary.add(sr);
                    }
                }
            }
            Collections.sort(strategySummary, new StrategyResult()); // sort results list
            double avergeProfit = StrategyResult.calculateAvgStrategyProfit(strategySummary);
            System.out.println("Average profit=> " + (double) Math.round(10000 * avergeProfit) / 100 + "%");

            System.out.println("10 best results ______________________________________________________________ ");
            for (int i = 0; i < 10; i++) {
                RsiStrategy rs = (RsiStrategy) strategySummary.get(i).getStrategy();
                System.out.println("profit:" + (double) Math.round(strategySummary.get(i).getProfit() * 10000) / 100 + "%" + " | max-drawdown:" + (double) Math.round(strategySummary.get(i).getMaxDrawdown() * 10000) / 100 + "%" +
                        " | wins:" + (double) Math.round(10000 * strategySummary.get(i).getWinsRatio()) / 100 + "%" +
                        " | losses:" + (double) Math.round(10000 * strategySummary.get(i).getLossesRatio()) / 100 + "%" +
                        " | s-l:" + (double) Math.round(rs.getStopLoss() * 10000) / 100 + "%" + " | t-p:" + (double) Math.round(rs.getTakeProfit() * 10000) / 100 + "%" + " | bottom-rsi:" + rs.getFloorRSI() +
                        " | top-rsi:" + rs.getCeilingRSI() + "\n");
            }
            System.out.println("10 worst results _____________________________________________________________ ");
            for (int i = strategySummary.size() - 1; i > strategySummary.size() - 11; i--) {
                RsiStrategy rs = (RsiStrategy) strategySummary.get(i).getStrategy();
                System.out.println("profit: " + (double) Math.round(strategySummary.get(i).getProfit() * 10000) / 100 + "%" + " | max-drawdown:" + (double) Math.round(strategySummary.get(i).getMaxDrawdown() * 10000) / 100 + "%" +
                        " | wins:" + (double) Math.round(10000 * strategySummary.get(i).getWinsRatio()) / 100 + "%" +
                        " | losses:" + (double) Math.round(10000 * strategySummary.get(i).getLossesRatio()) / 100 + "%" +
                        " | s-l:" + (double) Math.round(rs.getStopLoss() * 10000) / 100 + "%" + " | t-p:" + (double) Math.round(rs.getTakeProfit() * 10000) / 100 + "%" + " | bottom-rsi:" + rs.getFloorRSI() +
                        " | top-rsi:" + rs.getCeilingRSI() + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
