package com.luoquant.datacenter.fxcm;

import com.fxcm.external.api.transport.FXCMLoginProperties;
import com.fxcm.external.api.transport.GatewayFactory;
import com.fxcm.external.api.transport.IGateway;
import com.fxcm.external.api.transport.listeners.IGenericMessageListener;
import com.fxcm.external.api.transport.listeners.IStatusMessageListener;
import com.fxcm.fix.FXCMTimingIntervalFactory;
import com.fxcm.fix.IFixDefs;
import com.fxcm.fix.Instrument;
import com.fxcm.fix.SubscriptionRequestTypeFactory;
import com.fxcm.fix.UTCDate;
import com.fxcm.fix.UTCTimeOnly;
import com.fxcm.fix.UTCTimestamp;
import com.fxcm.fix.pretrade.MarketDataRequest;
import com.fxcm.fix.pretrade.MarketDataRequestReject;
import com.fxcm.fix.pretrade.MarketDataSnapshot;
import com.fxcm.fix.pretrade.TradingSessionStatus;
import com.fxcm.messaging.ISessionStatus;
import com.fxcm.messaging.ITransportable;

import java.text.SimpleDateFormat;
import java.util.*;

public class HistoryMiner implements IGenericMessageListener, IStatusMessageListener {
    private static final String timeZone = "America/New_York";
    private static final String server = "http://www.fxcorporate.com/Hosts.jsp";
    private Instrument asset;
    private UTCDate startDate;
    private UTCTimeOnly startTime;
    private FXCMLoginProperties login;

    private IGateway gateway;
    private String currentRequest;

    private final HashMap<UTCDate, MarketDataSnapshot> historicalRates = new HashMap<UTCDate, MarketDataSnapshot>();
    private int dataCounter = 0;
    private UTCTimestamp openTimestamp;
    boolean stillMining = true;

    List<CandleStick> candleStickList = new ArrayList<CandleStick>();


    public HistoryMiner(String username, String password, String terminal, UTCDate startDate,
                        UTCTimeOnly startTime, Instrument asset) {
        this.asset = asset;
        this.startDate = startDate;
        this.startTime = startTime;

        //create a local loginProperty
        // terminal is "Demo" or "Real"
        this.login = new FXCMLoginProperties(username, password, terminal, server);

    }

    public boolean login(IGenericMessageListener genericMessageListener, IStatusMessageListener statusMessageListener) {
        try {
            if (gateway == null)
                gateway = GatewayFactory.createGateway();
            gateway.registerGenericMessageListener(genericMessageListener);
            gateway.registerStatusMessageListener(statusMessageListener);
            if (!gateway.isConnected())
                gateway.login(this.login);
            currentRequest = gateway.requestTradingSessionStatus(); // return that this process was successful
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // if any error occurred, return that this process failed
        return false;
    }

    // market data
    public void messageArrived(ITransportable message) {
        // decide which child function to send a cast instance of the message
        try {
            if (message instanceof MarketDataSnapshot) messageArrived((MarketDataSnapshot) message);
            if (message instanceof MarketDataRequestReject) messageArrived((MarketDataRequestReject) message);
            else if (message instanceof TradingSessionStatus) messageArrived((TradingSessionStatus) message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void messageArrived(TradingSessionStatus tss) {
        if (currentRequest.equals(tss.getRequestID())) {
            MarketDataRequest mdr = new MarketDataRequest();
// set the subscription type to ask for only a snapshot of the history mdr.setSubscriptionRequestType(SubscriptionRequestTypeFactory.SNAPSHOT); // request the response to be formated FXCM style mdr.setResponseFormat(IFixDefs.MSGTYPE_FXCMRESPONSE);
// set the interval of the data candles mdr.setFXCMTimingInterval(FXCMTimingIntervalFactory.MIN1); mdr.setFXCMStartDate(startDate);
//            mdr.setFXCMStartDate(startDate);
            mdr.setFXCMStartTime(startTime);
            mdr.addRelatedSymbol(asset);
//            System.out.println("current date="+startDate + "currentTime="+startTime+" and add asset "+asset);
            currentRequest = sendRequest(mdr);
        }
    }

    public void messageArrived(MarketDataSnapshot mds) {
//        System.out.println("arrive market data======"+mds);
        if (mds.getRequestID() != null && mds.getRequestID().equals(currentRequest))
            historicalRates.put(mds.getDate(), mds);
        if (mds.getRequestID() != null) {
            dataCounter++;
            if (openTimestamp == null) {
//get the time stamp of first candle of this batch. use it to set the end time of next batch.
                openTimestamp = mds.getOpenTimestamp();
                System.out.println("first\t= " + mds.getOpenTimestamp() + " = " + mds.getRequestID());
            }
            if (mds.getFXCMContinuousFlag() == IFixDefs.FXCMCONTINUOUS_END) { // end of packet. System.out.println("last\t= " + mds.getOpenTimestamp() + " = " + mds.getRequestID()+"\n");
                MarketDataRequest mdr = new MarketDataRequest();
                mdr.setSubscriptionRequestType(SubscriptionRequestTypeFactory.SNAPSHOT);
                mdr.setResponseFormat(IFixDefs.MSGTYPE_FXCMRESPONSE);
                mdr.setFXCMTimingInterval(FXCMTimingIntervalFactory.MIN1);
                mdr.setFXCMStartDate(startDate);
                mdr.setFXCMStartTime(startTime);
                mdr.setFXCMEndDate(new UTCDate(openTimestamp));
                mdr.setFXCMEndTime(new UTCTimeOnly(openTimestamp));
                mdr.addRelatedSymbol(asset);
                System.out.println("FXCMStartDate\t= " + mdr.getFXCMStartDate());
                System.out.println("FXCMStartTime\t= " + mdr.getFXCMStartTime());
                System.out.println("FXCMEndDate\t\t= " + mdr.getFXCMEndDate());
                System.out.println("FXCMEndTime\t\t= " + mdr.getFXCMEndTime());
                System.out.println("-----------------------\ntotal mds received = " + dataCounter + "\n");
                if (!(mds.getOpenTimestamp().equals(openTimestamp))) { // send another request for historical data currentRequest=sendRequest(mdr);
                    openTimestamp = null;
                } else {
                    stillMining = false;
                    System.out.println("mining over...");
                }
            }
        }
    }

    public void messageArrived(MarketDataRequestReject mdrr) {
        System.out.println("Historical data rejected; " + mdrr.getMDReqRejReason());
        stillMining = false;
    }

    public String sendRequest(MarketDataRequest mdr) {
        try {
            currentRequest = gateway.sendMessage(mdr);
            return currentRequest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void messageArrived(ISessionStatus status) {
        // check the status code
        if (status.getStatusCode() == ISessionStatus.STATUSCODE_ERROR || status.getStatusCode() == ISessionStatus.STATUSCODE_DISCONNECTING || status.getStatusCode() == ISessionStatus.STATUSCODE_CONNECTING || status.getStatusCode() == ISessionStatus.STATUSCODE_CONNECTED || status.getStatusCode() == ISessionStatus.STATUSCODE_CRITICAL_ERROR || status.getStatusCode() == ISessionStatus.STATUSCODE_EXPIRED || status.getStatusCode() == ISessionStatus.STATUSCODE_LOGGINGIN || status.getStatusCode() == ISessionStatus.STATUSCODE_LOGGEDIN || status.getStatusCode() == ISessionStatus.STATUSCODE_PROCESSING || status.getStatusCode() == ISessionStatus.STATUSCODE_DISCONNECTED) {
            System.out.println("\t\t" + status.getStatusMessage());
        }
    }

    public void logout(IGenericMessageListener genericMessageListener, IStatusMessageListener statusMessageListener) {
        // attempt to logout of the api
        gateway.logout();
        // remove the generic message listener, stop listening to updates
        gateway.removeGenericMessageListener(genericMessageListener);
        // remove the status message listener, stop listening to status changes
        gateway.removeStatusMessageListener(statusMessageListener);
    }

    public void convertHistoricalRatesToCandleSticks() {
        SortedSet<UTCDate> dateList = new TreeSet<UTCDate>(historicalRates.keySet());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));

        for (UTCDate utcDate : dateList) {
            MarketDataSnapshot candleData;
            candleData = historicalRates.get(utcDate);
            Date candleDate = utcDate.toDate();
            String sdfDate = sdf.format(candleDate);
            double open = candleData.getBidOpen();
            double high = candleData.getBidHigh();
            double low = candleData.getBidLow();
            double close = candleData.getBidClose();
            double closeAsk = candleData.getAskClose();


            CandleStick candleStick = new CandleStick(sdfDate, open, high, low, close, closeAsk);
            candleStickList.add(candleStick);
        }
    }

    public void displayHistory(){
        if (candleStickList.size() < 1){
            System.out.println("No data to display");
            return;
        }
        for (CandleStick candleStick : candleStickList){
            System.out.println(candleStick.toString());
        }
    }
}
