package edu.stevens.cs.virustraffic;

/* --------------------
 * MemoryUsageDemo.java
 * --------------------
 * (C) Copyright 2002-2006, by Object Refinery Limited.
 */


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 * A demo application showing a dynamically updated chart that displays the
 * current JVM memory usage.
 * <p>
 * IMPORTANT NOTE:  THIS DEMO IS DOCUMENTED IN THE JFREECHART DEVELOPER GUIDE.
 * DO NOT MAKE CHANGES WITHOUT UPDATING THE GUIDE ALSO!!
 */
public class MemoryUsageDemo extends JPanel {

    /** Time series for total memory used. */
    private TimeSeries total;

    /** Time series for free memory. */
    private TimeSeries free;

    /**
     * Creates a new application.
     *
     * @param maxAge  the maximum age (in milliseconds).
     */
    public MemoryUsageDemo(int maxAge) {

        super(new BorderLayout());

        // create two series that automatically discard data more than 30
        // seconds old...
        this.total = new TimeSeries("Total Memory", Millisecond.class);
        this.total.setMaximumItemAge(maxAge);
        this.free = new TimeSeries("Free Memory", Millisecond.class);
        this.free.setMaximumItemAge(maxAge);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(this.total);
        dataset.addSeries(this.free);

        DateAxis domain = new DateAxis();
        NumberAxis range = new NumberAxis();
        //domain.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        //range.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        //domain.setLabelFont(new Font("SansSerif", Font.PLAIN, 30));
        //range.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));

        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, Color.red);
        renderer.setSeriesPaint(1, Color.green);
        
        renderer.setSeriesStroke(0, new BasicStroke(3f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL));
        renderer.setSeriesStroke(1, new BasicStroke(3f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL));
        XYPlot plot = new XYPlot(dataset, domain, range, renderer);
        domain.setAutoRange(true);
        domain.setLowerMargin(0.0);
        domain.setUpperMargin(0.0);
        domain.setTickLabelsVisible(false);
        range.setTickLabelsVisible(false);

        range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        renderer.setBaseItemLabelsVisible(false);

        JFreeChart chart = new JFreeChart("JVM Memory Usage",
                new Font("SansSerif", Font.BOLD, 24), plot, true);
        //plot.setDomainGridlinesVisible(false);
        //plot.setDomainZeroBaselineVisible(false);
        //plot.setDomainCrosshairLockedOnData(true);
        //plot.setRangeCrosshairVisible(false);
        //JFreeChart chart = new JFreeChart(plot);
        chart.removeLegend();
        String t = null;
        chart.setTitle(t);
        //renderer.setBaseSeriesVisibleInLegend(false);
        //JFreeChart chart = ChartFactory.createTimeSeriesChart("Title", "", "", dataset, false, true, false);
        chart.setBackgroundPaint(Color.blue);

        ChartUtilities.applyCurrentTheme(chart);
        chart.setBorderVisible(false);
        chart.setBorderPaint(null);
        domain.setTickLabelInsets(RectangleInsets.ZERO_INSETS);
        range.setTickMarksVisible(false);
        range.setTickLabelInsets(RectangleInsets.ZERO_INSETS);
        domain.setTickMarksVisible(false);
        chart.setPadding(RectangleInsets.ZERO_INSETS);
        

        ChartPanel chartPanel = new ChartPanel(chart, true);
        chartPanel.setBorder(null);
        add(chartPanel);

    }

    /**
     * Adds an observation to the 'total memory' time series.
     *
     * @param y  the total memory used.
     */
    private void addTotalObservation(double y) {
        this.total.add(new Millisecond(), y);
    }

    /**
     * Adds an observation to the 'free memory' time series.
     *
     * @param y  the free memory.
     */
    private void addFreeObservation(double y) {
        this.free.add(new Millisecond(), y);
    }

    /**
     * The data generator.
     */
    class DataGenerator extends Timer implements ActionListener {

        /**
         * Constructor.
         *
         * @param interval  the interval (in milliseconds)
         */
        DataGenerator(int interval) {
            super(interval, null);
            addActionListener(this);
        }

        /**
         * Adds a new free/total memory reading to the dataset.
         *
         * @param event  the action event.
         */
        public void actionPerformed(ActionEvent event) {
            long f = Runtime.getRuntime().freeMemory();
            long t = Runtime.getRuntime().totalMemory();
            addTotalObservation(t);
            addFreeObservation(f);
        }

    }

    /**
     * Entry point for the sample application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        JFrame frame = new JFrame("Memory Usage Demo");
        MemoryUsageDemo panel = new MemoryUsageDemo(30000);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setBounds(200, 120, 600, 280);
        frame.setVisible(true);
        panel.new DataGenerator(100).start();

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}

