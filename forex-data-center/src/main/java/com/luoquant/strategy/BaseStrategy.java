package com.luoquant.strategy;

import com.luoquant.models.Bin;
import com.luoquant.repository.DataRepository;
import com.luoquant.trade.Portfolio;
import com.luoquant.trade.Trade;

import java.util.List;
import java.util.Map;

/**
 * Created by luoqing on 23/04/2018.
 */
public class BaseStrategy {
    public Map<String,String> params;
    DataRepository dataRepository;

    public void runSimulation(){
        //get data from DataRepository
        List<Bin> dailyBars = dataRepository.getBinsDaily();
        List<Bin> minute1Bars = dataRepository.getBinsM1();
        double entryPrice = 1.0d;
        for (Bin dailyBin : dailyBars){
            for (Bin minute1Bin : minute1Bars){
                if (minute1Bin.highAsk >= entryPrice &&
                        minute1Bin.lowAsk <= entryPrice){
                    // add pending order
                    Trade trade = new Trade();
                    trades.add(trade);
                }

                for (Trade trade : trades){
                    if ( minute1Bin.highAsk >= trade.getTakeProfitPrice()){// take profit
                        trades.remove(trade);
                    }
                    if (minute1Bin.lowAsk <= trade.getStopLossPrice()){
                        // stop loss
                        trades.remove(trade);
                    }
                }
            }
        }
    }

    public void runRealtime(){
        // get data from DataRepository
    }

    List<Trade> trades;

    Portfolio.CostFunction cf;
    public void calcCostFunction(){

    }
}
