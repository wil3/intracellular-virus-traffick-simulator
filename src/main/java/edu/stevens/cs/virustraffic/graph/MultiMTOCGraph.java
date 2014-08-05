package edu.stevens.cs.virustraffic.graph;

import java.util.Random;

import org.jgrapht.graph.ListenableDirectedGraph;

import edu.stevens.cs.virustraffic.Microtuble;
import edu.stevens.cs.virustraffic.MicrotubleVertex;
import edu.stevens.cs.virustraffic.VertexType;
import edu.stevens.cs.virustraffic.visual.VisualConstants;

/**
 * In multi MTOC graphs, there are multiple sudo MTOCs and one true MTOC.
 * Each MT connected to a sudo MTOC can either be inward from the MTOC or outward.
 * 
 * Each sudo MTOC is connected to the true MTOC in a single inward direction.
 * 
 * From the true MTOC there is a single connection to the nucleus.
 * 
 * Connections from sudo MTOC to true MTOC that cross over the nucleus have the ability to have the virus enter the 
 * nucleus without having to go to the true MTOC. This is because when the virus leaves the sudo MTOC it is unpacked.
 * 
 * @author wil
 *
 */
public class MultiMTOCGraph extends SingleMTOCGraph {
	private int numberOfMicrotubles;
	private int numberOfMTOCs;
	private final static Random rand = new Random();

	/**
	 * For when it shoulld be exact
	 * @param numberOfMTOCs
	 * @param numberOfMicrotubles
	 */
	public MultiMTOCGraph(int numberOfMTOCs, int numberOfMicrotubles){
		super(numberOfMicrotubles);

		this.numberOfMTOCs = numberOfMTOCs;
	}
	
	/**
	 * For when it shoud be random
	 * @param lowerBoundNumberOfMTOCs
	 * @param lowerBoundNumberOfMTOCs
	 * @param numberOfMicrotubles
	 * @param numberOfMicrotubles
	 */
	public MultiMTOCGraph(int lowerBoundNumberOfMTOCs, int upperBoundNumberOfMTOCs, int lowerNumberOfMicrotubles, int upperNumberOfMicrotubles){
		super(rand.nextInt(upperNumberOfMicrotubles-lowerNumberOfMicrotubles) + lowerNumberOfMicrotubles);
		

	}
	@Override
	public ListenableDirectedGraph<MicrotubleVertex, Microtuble> generate() {

		//add nucleus
		MicrotubleVertex nucleus = new MicrotubleVertex(VertexType.NUCLEUS);
		getGraph().addVertex(nucleus);
		
		//add real mtoc
		MicrotubleVertex mtoc = new MicrotubleVertex(VertexType.MTOC);
		getGraph().addVertex(mtoc);
		
		
		//add all sudo mtocs to real mtoc
		for (int i=0; i<numberOfMTOCs; i++){
			MicrotubleVertex sudoMTOC = constructSingleLayerTree(VertexType.SUDO_MTOC,VertexType.RECEPTOR);
			Microtuble e = new Microtuble();
			e.setLength(MIN_LENGTH);
			e.setStyle(VisualConstants.STYLE_E_SUDO2MTOC);
			getGraph().addEdge(mtoc,sudoMTOC, e );
		}
		
		//add the real mtoc to the nucleus
		Microtuble e1 = new Microtuble();
		e1.setLength(MIN_LENGTH);
		e1.setStyle(VisualConstants.SYTLE_E_MTOC2NUCLEUS);
		getGraph().addEdge(nucleus,mtoc, e1 );

		return getGraph();
	}

	
}
