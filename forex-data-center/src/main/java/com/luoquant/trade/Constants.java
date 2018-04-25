package com.luoquant.trade;

/**
 * Created by luoqing on 23/04/2018.
 */
public class Constants {
    public enum DirectionType{
        LONG(0),
        SHORT(1);

        private int type;
        private DirectionType(int type){
            this.type = type;
        }

        public int getType(){
            return type;
        }

        public void setType(int type){
            this.type = type;
        }
    }
}
