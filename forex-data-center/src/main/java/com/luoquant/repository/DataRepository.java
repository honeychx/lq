package com.luoquant.repository;

import com.luoquant.models.Bin;

import java.util.List;

/**
 * Created by luoqing on 23/04/2018.
 */
public class DataRepository {
    //singleton

    List<Bin> binsDaily;
    List<Bin> binsH4;
    List<Bin> binsM1;

    public List<Bin> getBinsDaily() {
        return binsDaily;
    }

    public void setBinsDaily(List<Bin> binsDaily) {
        this.binsDaily = binsDaily;
    }

    public List<Bin> getBinsH4() {
        return binsH4;
    }

    public void setBinsH4(List<Bin> binsH4) {
        this.binsH4 = binsH4;
    }

    public List<Bin> getBinsM1() {
        return binsM1;
    }

    public void setBinsM1(List<Bin> binsM1) {
        this.binsM1 = binsM1;
    }
}
