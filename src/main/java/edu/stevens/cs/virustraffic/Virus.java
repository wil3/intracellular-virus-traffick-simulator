package edu.stevens.cs.virustraffic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jgrapht.DirectedGraph;
//import edu.stevens.cs.virustraffic.VertexType;

public class Virus implements Runnable {
	Logger logger = Logger.getLogger(Virus.class);
	public static int NUMBER_OF_STAGES = 6;
	/**
	 * Keep track of incrementing ids
	 */
	public static int ID_COUNT = 0;
	
	final private Random r = new Random();
	/**
	 * Max speed virus can achieve (because it is selected at random)
	 */
	final private static double MAX_VELOCITY = 5.0;
	/**
	 * How fast can the virus travel (ie what motor protein is it using)
	 */
	private double velocity = 1.0;
	/**
	 * How to identify
	 */
	private String ID;
	/**
	 * Where in the current microtuble are we?
	 */
	private double position;
	/**
	 * Access to whole map so we can navigate
	 */
	private  DirectedGraph<MicrotubleVertex, Microtuble> graph;
	/**
	 * Current microtuble we are traveling in
	 */
	private Microtuble microtuble; 
	
	/**
	 * What stage am i in?
	 */
	private Stage stage = Stage.FREE;
	/**
	 * Am i alive?
	 */
	private boolean alive = true;
	
	/**
	 * Save this right away so we dont have to keep searching for them every time step
	 */
	private List<MicrotubleVertex> entrances = new ArrayList<MicrotubleVertex>();;
	
	public enum State {START,PAUSE,STOP};
	private State state = State.PAUSE;
	
	public Virus( DirectedGraph<MicrotubleVertex, Microtuble> graph){
		this.graph = graph;
		this.ID = "virus-" + ID_COUNT++;
		
		//Set random velocity
		this.velocity = MAX_VELOCITY * r.nextDouble();
		//Do this early on so we dont have to do it later
		findEntrances();
	}
	
	/**
	 * Find all of the entrances to the microtubles dont assign until the virus actual bounds and enters cell
	 */
	private void findEntrances(){
		
		Set<MicrotubleVertex> vs = graph.vertexSet();
		Iterator<MicrotubleVertex> it = vs.iterator();
		while (it.hasNext()){
			MicrotubleVertex v = it.next();
			if (v.getType() == VertexType.RECEPTOR){
				entrances.add(v);
			}	
		}
	}
	/**
	 * Try to get access into the cell, when we do we are assigned a microtuble
	 */
	private void attemptBound(){

	//	if (r.nextDouble() < 0.3){
			int tarIndex = r.nextInt(entrances.size());
			//Entrance vertex should only have one edge
			this.microtuble = graph.edgesOf(entrances.get(tarIndex)).iterator().next();
			
			MicrotubleVertex target = graph.getEdgeSource(microtuble);
			MicrotubleVertex source = graph.getEdgeTarget(microtuble);

			//logger.info("Bound to " + target + "\t=>\t" + source);
			this.stage = Stage.BOUND;
	//	}
	}
	/**
	 * Move the virus one step, if at the end of the current microtuble move to the next random one
	 *
	 */
	//TODO should decision be uniform random to select new path?

	private void move(){
		
		//test to see if we are at the end of the microtuble, if so randomly jump to the next
		if (microtuble.getLength() <= position){
			MicrotubleVertex target = graph.getEdgeSource(microtuble); //we work backwards
		
			Set<Microtuble> pathOptions = graph.edgesOf(target);
			if (target.getType() == VertexType.NUCLEUS){ //at the end
				this.alive = false;
				stage = Stage.NUCLEUS_ASSOC;
			}else {
				int tarIndex = r.nextInt(pathOptions.size());
				Iterator<Microtuble> it = pathOptions.iterator();
				int index = 0;
				while (it.hasNext()){
					if (tarIndex == index){
						this.microtuble = it.next();
						break;
					}
					index++;
				}
				position = 0;
			}
			//System.out.println(toString() + "\t"+ microtuble.getLength());

		} else {
			position += velocity;
		//	logger.info(ID + " Pos=" + position);
		}
		
	}
	/**
	 * Randomly decide if we should move or not
	 */
	//TODO what is the probabily of a move?
	private void considerMove(){
		//	if (r.nextDouble() < 0.3){

		move();
		//	}
	}
	/**
	 * @return the position
	 */
	public double getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(double position) {
		this.position = position;
	}
	/**
	 * @return the microtuble
	 */
	public Microtuble getMicrotuble() {
		return microtuble;
	}
	/**
	 * @param microtuble the microtuble to set
	 */
	public void setMicrotuble(Microtuble microtuble) {
		this.microtuble = microtuble;
	}
	
	/**
	 * @return the stage
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * @param stage the stage to set
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public String toString(){
		return ID;
	}
	/**
	 * Run in descrete steps
	 */
	@Override
	public void run() {
		
		while (true){
			try {
				
				if (state == State.PAUSE){
					
				} else if (state == State.STOP){
					break;
				} else {
					switch(stage){
						case FREE:
							attemptBound();
							break;
						case BOUND:
							//Should the virus move?
							stage = Stage.ENDOSOME;
							break;
						case ENDOSOME:
							stage = Stage.CYTOSOL;
							break;
						case CYTOSOL:
							stage = Stage.MICROTUBLE_ASSOC;
							break;
						case MICROTUBLE_ASSOC:
							considerMove();
							break;
						case NUCLEUS_ASSOC:
						default:
					}
				}
				Thread.sleep(VirusTrafficSimulator.TIME_STEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setState(State state){
		this.state = state;
	}
	
}
