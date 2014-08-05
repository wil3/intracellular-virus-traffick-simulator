package edu.stevens.cs.virustraffic.visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jgrapht.graph.ListenableDirectedGraph;

import edu.stevens.cs.virustraffic.Microtuble;
import edu.stevens.cs.virustraffic.MicrotubleVertex;
import edu.stevens.cs.virustraffic.Virus;
import edu.stevens.cs.virustraffic.VirusTrafficSimulator;
import edu.stevens.cs.virustraffic.visual.TimePlotSimulator.DataGenerator;


/**
 * Main frame
 * @author wil
 *
 */
public class VisualVirusTraffic extends JFrame {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4105896379113176496L;
	public static int HEIGHT;
	public static int WIDTH;
	VisualVirusTrafficSimulator simulationView;
	public VisualVirusTraffic( VirusTrafficSimulator env,  int width, int height){
		WIDTH = width;
		HEIGHT = height;
		this.simulationView = new VisualVirusTrafficSimulator(env.getGraph(),env.getViruses() );
		
		List<RealTimeSimulation> sims = new ArrayList<RealTimeSimulation>();
		
		TimePlotSimulator graph = new TimePlotSimulator(30000, env.getViruses());

		sims.add(simulationView);
		sims.add(env);
		sims.add(graph);
		//getContentPane().setBackground(Color.red);
		 
		 /*
		  * Add main display panel
		  */
        getContentPane().add(simulationView, BorderLayout.PAGE_START);
        
        /*
         * Add the control panel
         */
        getContentPane().add(new ControlPanel(sims),BorderLayout.CENTER);
        
        
        /*
         * add time plot
         */
		//DataGenerator gen =   graph.new DataGenerator(VirusTrafficSimulator.TIME_STEP);
	   // gen.start();
	    
        getContentPane().add(graph, BorderLayout.PAGE_END);
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
	}
	

	/**
	 * @return the simulationView
	 */
	public VisualVirusTrafficSimulator getSimulationView() {
		return simulationView;
	}

	/**
	 * @param simulationView the simulationView to set
	 */
	public void setSimulationView(VisualVirusTrafficSimulator simulationView) {
		this.simulationView = simulationView;
	}
}
