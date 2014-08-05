package edu.stevens.cs.virustraffic.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import org.jgrapht.graph.ListenableDirectedGraph;

import edu.stevens.cs.virustraffic.Microtuble;
import edu.stevens.cs.virustraffic.MicrotubleVertex;
import edu.stevens.cs.virustraffic.VertexType;

public class PolyTreeGraph implements GraphGenerator{
	private Random r = new Random();
	private int v;
	private int e;
	private int maxLength;
	private int minLength;

	private ListenableDirectedGraph<MicrotubleVertex, Microtuble> graph;
	/**
	 * Number of vertices
	 * @param v
	 */
	public PolyTreeGraph(int v, int minLength, int maxLength){
		this.v = v;
		this.e = v-1;
		this.maxLength = maxLength;
		this.minLength = minLength;

		this.graph =  new ListenableDirectedGraph<MicrotubleVertex, Microtuble>(Microtuble.class);

	}
	@Override
	public ListenableDirectedGraph<MicrotubleVertex, Microtuble> generate() {

		// Any leaf is going to be an entrance
		
		Stack<MicrotubleVertex> vertices = new Stack<MicrotubleVertex>();


		for (int i=0; i<v-1; i++){
			MicrotubleVertex vertex = new MicrotubleVertex();
			graph.addVertex(vertex);
			vertices.push(vertex);
		}
		
		MicrotubleVertex root = new MicrotubleVertex(VertexType.NUCLEUS);
		graph.addVertex(root);
		traverse(root, vertices);
		assignTypes();
		return graph;
	}
	
	private void traverse(MicrotubleVertex root, Stack<MicrotubleVertex> vertices ){
		
		List<MicrotubleVertex> possibleNewRoots = new ArrayList<MicrotubleVertex>();
		int degree = r.nextInt(vertices.size())+1;
		for (int i=0; i<degree; i++){
			int l = r.nextInt(maxLength-minLength) + minLength;
			Microtuble edge = new Microtuble(l);
			MicrotubleVertex vertex = vertices.pop();
			possibleNewRoots.add(vertex);
			graph.addEdge(root, vertex ,edge);
		}
		if (vertices.size() > 0 && possibleNewRoots.size() > 0){
			//Randomly select the next root of the ones just added
			MicrotubleVertex newRoot = possibleNewRoots.get(r.nextInt(possibleNewRoots.size()));
			traverse(newRoot, vertices);
		}
	}

	private void assignTypes(){
		for (MicrotubleVertex vertex : graph.vertexSet()){
			int degree = graph.outDegreeOf(vertex);
			if (vertex.getType() == null){
				if (degree > 0){
					vertex.setType(VertexType.JUNCTION);
				} else {
					vertex.setType(VertexType.RECEPTOR);
	
				}
			}
		}
	}
}
