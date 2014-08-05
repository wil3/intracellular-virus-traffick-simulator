package edu.stevens.cs.virustraffic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import edu.stevens.cs.virustraffic.Virus.State;
import edu.stevens.cs.virustraffic.graph.GraphGenerator;
import edu.stevens.cs.virustraffic.graph.MultiMTOCGraph;
import edu.stevens.cs.virustraffic.graph.PolyTreeGraph;
import edu.stevens.cs.virustraffic.graph.SimpleGraph;
import edu.stevens.cs.virustraffic.graph.SingleMTOCGraph;
import edu.stevens.cs.virustraffic.visual.CellLayoutManager;
import edu.stevens.cs.virustraffic.visual.CellMorphology;
import edu.stevens.cs.virustraffic.visual.CircularCellMorphology;
import edu.stevens.cs.virustraffic.visual.MultiMTOCLayoutManager;
import edu.stevens.cs.virustraffic.visual.RealTimeSimulation;
import edu.stevens.cs.virustraffic.visual.SingleMTOCLayoutManager;
import edu.stevens.cs.virustraffic.visual.VisualVirusTrafficSimulator;
import edu.stevens.cs.virustraffic.visual.VisualVirusTraffic;

/**
 * Main class for simulator
 * 
 * Simulator is broken into 3 parts
 * (1) Virus agents - each virus is its own independent thread
 * (2) Graph modeling - implementation of actual mathematical model using jgrapht
 * (3) GUI - visual display of the simulation using jgraphx
 * 
 * Parameters
 * -Cell morphology
 * -Position of entry (x,y) coordinate
 * -shape of cell
 * -organization of MT
 * -number of viruses
 * -density of sudo MTOCs
 * -binding rates
 * -max velocity
 * -alter pdf for rates
 * 
 * cytosol -> bound to MT -> moving along MT -> (intersection w/ nucleus or arrival at MTOC(microtuble organizing center))
 * 
 * need to be able to release from MT and transfer to another MT
 * 
 * -be able to vary all probabilities
 * -change concentration and locations of viruses
 * 
 * -Look into Gilespe's model
 * -Have a graph of bound, endosome, cytosol, MT assoc., nucleas assoc. (density vs time)
 * 
 * 
 * Once the virus becomes bound it enters the cell and is in the cytoplasm.
 * The virus then can move slightly in a certain range to try and enter a MTs. If the virus is not in the 
 * area of a MT then it may just get stuck there.
 * 
 * @author wil koch
 *
 */
public class VirusTrafficSimulator implements RealTimeSimulation {
	final private Random r = new Random();

	/**
	 * Total virus population
	 */
	private int population;
	/**
	 * Track all viruses being modelled
	 */
	private List<Virus> viruses = new ArrayList<Virus>();
	/**
	 * Microtuble graph
	 */
	private  ListenableDirectedGraph<MicrotubleVertex, Microtuble> graph;
	
	/**
	 * Used globally for simulation speed
	 */
	public static int TIME_STEP = 200;
	
	public VirusTrafficSimulator(int population, ListenableDirectedGraph<MicrotubleVertex, Microtuble> graph){
		this.population = population;
		this.graph = graph;
		initViruses();
	}
	/**
	 * Create all new viruses 
	 */
	private void initViruses(){
		for (int i=0; i<population; i++){
			viruses.add(new Virus(graph));
		}
		
		//Update state of all viruses
				for (Virus virus : viruses){
					new Thread(virus).start();
				}
	}
	/**
	 * Validate the graph
	 */
	private void validate(){
		//Make sure all microtubles have a length
	}
	public void start(){
		for (Virus virus : viruses){
			virus.setState(State.START);

		}
		
		
	}
	
	public void setStructure( ListenableDirectedGraph<MicrotubleVertex, Microtuble> microtubules){
		this.graph = microtubules;
	}
	/**
	 * @return the viruses
	 */
	public List<Virus> getViruses() {
		return viruses;
	}
	/**
	 * @param viruses the viruses to set
	 */
	public void setViruses(List<Virus> viruses) {
		this.viruses = viruses;
	}
	
	
	/**
	 * @return the graph
	 */
	public ListenableDirectedGraph<MicrotubleVertex, Microtuble> getGraph() {
		return graph;
	}
	/**
	 * @param graph the graph to set
	 */
	public void setGraph(ListenableDirectedGraph<MicrotubleVertex, Microtuble> graph) {
		this.graph = graph;
	}
	/**
	 * @param args population number_mts number_sudo_mtocs
	 */
	public static void main(String[] args) {
		if (args.length != 3){
			throw new IllegalArgumentException("Not enough parameters, required: population number_mts number_sudo_mtocs" );
		}
		
		final int population = Integer.parseInt(args[0]);
		final int numberMTs = Integer.parseInt(args[1]);
		final int numberSudoMTOCs = Integer.parseInt(args[2]);

		
		GraphGenerator map = (numberSudoMTOCs == 0) ? new SingleMTOCGraph(numberMTs) : new MultiMTOCGraph(numberSudoMTOCs, numberMTs);

		
		final ListenableDirectedGraph<MicrotubleVertex, Microtuble> graph = map.generate();
		
		
		for (Microtuble edge : graph.edgeSet()){
			System.out.println(edge);
		}
		for (MicrotubleVertex vertex : graph.vertexSet()){
			System.out.println(vertex + "\t" + vertex.getType().toString());
		}
		System.out.println("start sim");
		//Start the simulation
		final VirusTrafficSimulator env = new VirusTrafficSimulator(population, graph);
		
		
		
		//Start the visualizer
		 java.awt.EventQueue.invokeLater(new Runnable() {
	         public void run() {
	        	CellMorphology ccm = new CircularCellMorphology(500);
	        	
	     		CellLayoutManager layout = (numberSudoMTOCs == 0) ? new SingleMTOCLayoutManager(ccm) : new MultiMTOCLayoutManager(ccm);
	     		//CellLayoutManager layout = new MultiMTOCLayoutManager(ccm);

	     		VisualVirusTraffic visual = new VisualVirusTraffic(env, 800, 600);
	     		visual.getSimulationView().setCellLayout(layout);

	     		//visual.getSimulationView().runSimulation();
				//env.start();

	         }
	      });
		 
		

	}
	@Override
	public void stop() {
		for (Virus virus : viruses){
			virus.setState(State.STOP);
		}		
	}
	@Override
	public void pause() {
		for (Virus virus : viruses){
			virus.setState(State.PAUSE);
		}
	}

}
