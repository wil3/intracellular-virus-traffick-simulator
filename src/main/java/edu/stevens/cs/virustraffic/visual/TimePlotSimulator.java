package edu.stevens.cs.virustraffic.visual;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.stevens.cs.virustraffic.Stage;
import edu.stevens.cs.virustraffic.Virus;
import edu.stevens.cs.virustraffic.VirusTrafficSimulator;

/**
 * For displaying plot
 * @author wil
 *
 */
public class TimePlotSimulator extends JPanel implements RealTimeSimulation{

	
	 /**
	 * 
	 */
	private static final long serialVersionUID = -3883059834790967123L;
	/** Time series for total memory used. */
    //private TimeSeries bound;
    
    private HashMap<Stage,TimeSeries>  stageSeries = new HashMap<Stage,TimeSeries>();
    List<Virus> viruses;
 
    private int maxAge;
    
	private Timer timer;

    /**
     * Creates a new application.
     *
     * @param maxAge  the maximum age (in milliseconds).
     */
    public TimePlotSimulator(int maxAge, List<Virus> viruses) {

        super(new BorderLayout());   
        Dimension preferredSize = new  Dimension(VisualVirusTraffic.WIDTH, 200);
		setPreferredSize(preferredSize);
        this.maxAge = maxAge;
        this.viruses = viruses;


        initializeStages();
        
        // create two series that automatically discard data more than 30
        // seconds old...
      //  this.bound = new TimeSeries("Bound");
      //  this.bound.setMaximumItemAge(maxAge);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        addToDataset(dataset);
      //  dataset.addSeries(this.bound);

        DateAxis domain = new DateAxis();
        NumberAxis range = new NumberAxis();
        domain.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        range.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        domain.setLabelFont(new Font("SansSerif", Font.PLAIN, 30));
        
        range.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));

        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        
        XYPlot plot = new XYPlot(dataset, domain, range, renderer);
        domain.setAutoRange(true);
        domain.setLowerMargin(0.0);
        domain.setUpperMargin(0.0);
        //domain.setTickLabelsVisible(false);
        //range.setTickLabelsVisible(false);

        range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        renderer.setBaseItemLabelsVisible(false);

        JFreeChart chart = new JFreeChart("JVM Memory Usage",
                new Font("SansSerif", Font.BOLD, 24), plot, true);

       // chart.removeLegend();
        String t = null;
        chart.setTitle(t);
     
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
        
        this.timer = new Timer(VirusTrafficSimulator.TIME_STEP, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
	            updateStages();

			}
		});
    }

    private void initializeStages(){
        Stage [] stages = Stage.BOUND.getDeclaringClass().getEnumConstants();

        for (int i=0; i<stages.length; i++){
        	TimeSeries ts = new TimeSeries(stages[i].toString());
        	ts.setMaximumItemAge(maxAge);
        	stageSeries.put(stages[i], ts);
        }
        // create two series that automatically discard data more than 30
        // seconds old...
       
    }
    
    private void addToDataset(TimeSeriesCollection dataset){

    	Iterator<TimeSeries> it = stageSeries.values().iterator();
    	while (it.hasNext()){
    		TimeSeries ts = it.next();
    		dataset.addSeries(ts);
    	}
    }
    /**
     * Adds an observation to the 'total memory' time series.
     *
     * @param y  the total memory used.
     */
    private void updateStages() {
    	
    	 HashMap<Stage,Integer> stagePopulations = getPopulations();
       // this.bound.add(new Millisecond(), stagePopulations[0]);
        
        Stage [] stages = Stage.BOUND.getDeclaringClass().getEnumConstants();

        for (int i=0; i<stages.length; i++){
        	stageSeries.get(stages[i]).add(new Millisecond(), stagePopulations.get(stages[i]));
        	
        }
    }


    private HashMap<Stage,Integer> getPopulations(){
    	HashMap<Stage,Integer> pops = new HashMap<Stage,Integer>();
    	Stage [] stages = Stage.BOUND.getDeclaringClass().getEnumConstants();
    	//init
        for (int i=0; i<stages.length; i++){
        	pops.put(stages[i], 0);
        }

    	for (Virus virus : viruses){
    		
    		int newPop = pops.get(virus.getStage()) + 1;
    		pops.put(virus.getStage(), newPop);
    	
		}
    	
    	return pops;
    }

    /**
     * The data generator.
     */
    public class DataGenerator extends Timer implements ActionListener {

        /**
		 * 
		 */
		private static final long serialVersionUID = 3620656738563644970L;

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
   
            updateStages();
        }
    }

	@Override
	public void start() {
		timer.start();
	}

	@Override
	public void stop() {
		timer.stop();
	}

	@Override
	public void pause() {
		timer.stop();
	}
}
