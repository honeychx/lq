package com.luoquant.datacenter.fxcm.strategy.base;

import com.luoquant.datacenter.fxcm.CandleStick;

import java.util.List;

/**
 * Created by luoqing on 1/24/18.
 */
public interface Strategy {
    StrategyResult runStrategy(List<CandleStick> candleStickList);
}
