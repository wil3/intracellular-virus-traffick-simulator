package edu.stevens.cs.virustraffic.graph;

import java.util.Random;

import org.jgrapht.graph.ListenableDirectedGraph;

import edu.stevens.cs.virustraffic.Microtuble;
import edu.stevens.cs.virustraffic.MicrotubleVertex;
import edu.stevens.cs.virustraffic.VertexType;
import edu.stevens.cs.virustraffic.visual.VisualConstants;

public class SingleMTOCGraph implements GraphGenerator{
	private Random r = new Random();

	private int numberOfMicrotubles;
	private ListenableDirectedGraph<MicrotubleVertex, Microtuble> graph;
	public static int MAX_LENGTH = 200;
	public static int MIN_LENGTH = 100;
	
	
	public SingleMTOCGraph(int numberOfMicrotubles){
		this.numberOfMicrotubles = numberOfMicrotubles;
		this.graph =  new ListenableDirectedGraph<MicrotubleVertex, Microtuble>(Microtuble.class);

	}
	
	/**
	 * 
	 * @param root	The root type
	 * @param children	The children type
	 * @return
	 */
	protected MicrotubleVertex constructSingleLayerTree(VertexType root, VertexType children){
		MicrotubleVertex mtoc = new MicrotubleVertex(root);
		graph.addVertex(mtoc);
		
		for (int i=0; i<numberOfMicrotubles; i++){
			MicrotubleVertex v = new MicrotubleVertex(children);
			graph.addVertex(v);
			
			Microtuble e = new Microtuble();
			graph.addEdge(mtoc, v, e);
		}
		return mtoc;
	}

	@Override
	public ListenableDirectedGraph<MicrotubleVertex, Microtuble> generate() {
		
		MicrotubleVertex nucleus = new MicrotubleVertex(VertexType.NUCLEUS);
		graph.addVertex(nucleus);
		
		Microtuble e = new Microtuble();
		e.setStyle(VisualConstants.SYTLE_E_MTOC2NUCLEUS);
		e.setLength(MIN_LENGTH);
		
		MicrotubleVertex mtoc = constructSingleLayerTree(VertexType.MTOC,VertexType.RECEPTOR);
		
		graph.addEdge(nucleus,mtoc, e );
		
		return graph;
	}

	/**
	 * @return the numberOfMicrotubles
	 */
	public int getNumberOfMicrotubles() {
		return numberOfMicrotubles;
	}

	/**
	 * @param numberOfMicrotubles the numberOfMicrotubles to set
	 */
	public void setNumberOfMicrotubles(int numberOfMicrotubles) {
		this.numberOfMicrotubles = numberOfMicrotubles;
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

}
