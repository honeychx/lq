package com.luoquant.view;

import com.luoquant.datacenter.fxcm.CandleStick;
import com.luoquant.datacenter.fxcm.utils.MarketDataUtils;
import javafx.scene.chart.CategoryAxis;
import org.apache.commons.io.FileUtils;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.date.DateUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luoqing on 1/24/18.
 */
public class MainCandleView extends ApplicationFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainCandleView.class);
    static SimpleDateFormat targetSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static double yRangeMin = Double.MAX_VALUE;
    private static double yRangeMax = Double.MIN_VALUE;

    public static void findAllFile(String path, List<File> fileList) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] nFiles = dir.list();
            for (String nFile : nFiles) {
                File f = new File(path + "/" + nFile);
                if (f.isFile()) {
                    fileList.add(f);
                } else {
                    findAllFile(f.getAbsolutePath(), fileList);
                }
            }
        }
    }

    public static List<CandleStick> readDataFromCsv(String path) {
        List<CandleStick> candleStickList = new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        findAllFile(path, fileList);
        logger.info("get total {}, from path {}", fileList.size(), path);
        for (File file : fileList.subList(fileList.size() - Math.min(150, fileList.size()),
                fileList.size() - 1)) {
            try {
                List<CandleStick> eachList = MarketDataUtils.readCandleFromCsv(FileUtils.readLines(file, "utf-8"));
                for (CandleStick candleStick : eachList) {
                    if (candleStick.getLow() < yRangeMin)
                        yRangeMin = candleStick.getLow();
                    if (candleStick.getHigh() > yRangeMax)
                        yRangeMax = candleStick.getHigh();
                }
                candleStickList.addAll(eachList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        yRangeMin = yRangeMin * .99;
        yRangeMax = yRangeMax * 1.01;
        return candleStickList;
    }


    public DefaultHighLowDataset getDataSetFromDisk(String path) {
        List<CandleStick> candleStickList = readDataFromCsv(path);
        Date[] dateList = new Date[candleStickList.size()];
        double[] openList = new double[candleStickList.size()];
        double[] highList = new double[candleStickList.size()];
        double[] lowList = new double[candleStickList.size()];
        double[] closeList = new double[candleStickList.size()];
        double[] volumeList = new double[candleStickList.size()];
        int i = 0;
        for (CandleStick candleStick : candleStickList) {
            dateList[i] = candleStick.getStartDate();
            openList[i] = candleStick.getOpen();
            highList[i] = candleStick.getHigh();
            lowList[i] = candleStick.getLow();
            closeList[i] = candleStick.getClose();
            volumeList[i] = candleStick.getVolume();
            i += 1;
        }
        logger.info("get history data from {}, total {}", path, i);
        return new DefaultHighLowDataset(path, dateList, highList, lowList, openList, closeList, volumeList);
    }

    public MainCandleView(final String title, String path) throws Exception {
        super(title);

        final DefaultHighLowDataset dataset = getDataSetFromDisk(path);
        //createHighLowDataset();
        final JFreeChart chart = createChart(dataset, title);
        chart.getXYPlot().setOrientation(PlotOrientation.VERTICAL);
        ValueAxis aaxis = chart.getXYPlot().getRangeAxis();
        aaxis.setRange(yRangeMin, yRangeMax);
        chart.getXYPlot().setRangeAxis(aaxis);
        try {
            ChartUtilities.saveChartAsJPEG(new File("./data/" + title + ".jpg"), chart, 1600, 900);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    public static JFreeChart createCandlestickChart(String title, String timeAxisLabel, String valueAxisLabel, OHLCDataset dataset, boolean legend) {
        DateAxis timeAxis = new DateAxis(timeAxisLabel);
        NumberAxis valueAxis = new NumberAxis(valueAxisLabel);
        XYPlot plot = new XYPlot(dataset, timeAxis, valueAxis, (XYItemRenderer) null);
        CandlestickRenderer candlestickRenderer = new CandlestickRenderer();
        candlestickRenderer.setUpPaint(new Color(0, 96, 191));
        candlestickRenderer.setDownPaint(new Color(219, 0, 0));
        plot.setRenderer(candlestickRenderer);
        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, legend);

        StandardChartTheme standardChartTheme = new StandardChartTheme("JFree");
        standardChartTheme.setPlotBackgroundPaint(Color.WHITE);
        ChartTheme currentTheme = standardChartTheme;
        currentTheme.apply(chart);

        return chart;
    }

    private JFreeChart createChart(final DefaultHighLowDataset dataset, String title) {
        //ChartFactory.createCandlestickChart
        final JFreeChart chart = createCandlestickChart(
                title,
                "Time",
                "Price",
                dataset,
                true
        );
        return chart;
    }

    public static void main(String[] args) throws Exception {
        String instrument = "AUDUSDH4";
//        instrument = "EURUSD1Day";
        String historyDataPath = "data/history/" ;
//                + instrument;

        final MainCandleView demo = new MainCandleView(instrument, historyDataPath);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
