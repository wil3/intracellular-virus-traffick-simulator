package edu.stevens.cs.virustraffic.graph;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.ListenableDirectedGraph;

import edu.stevens.cs.virustraffic.Microtuble;
import edu.stevens.cs.virustraffic.MicrotubleVertex;
import edu.stevens.cs.virustraffic.VertexType;

public class SimpleGraph implements GraphGenerator{

	public SimpleGraph(){
		
	}

	@Override
	public ListenableDirectedGraph<MicrotubleVertex, Microtuble> generate() {
		ListenableDirectedGraph<MicrotubleVertex, Microtuble> microtubules =  new ListenableDirectedGraph<MicrotubleVertex, Microtuble>(Microtuble.class);

		//Create the vertices
		MicrotubleVertex in1 = new MicrotubleVertex(VertexType.RECEPTOR);
		Microtuble m1 = new Microtuble();
		m1.setLength(50);
		
		MicrotubleVertex j1 = new MicrotubleVertex(VertexType.JUNCTION);
		Microtuble m_j1_out = new Microtuble();
		m_j1_out.setLength(100);
		
		
		MicrotubleVertex in2 = new MicrotubleVertex(VertexType.RECEPTOR);
		Microtuble m2 = new Microtuble();
		m2.setLength(125);
		
		MicrotubleVertex j2 = new MicrotubleVertex(VertexType.JUNCTION);
		Microtuble m_j2_out = new Microtuble();
		m_j2_out.setLength(75);
		
		
		
		MicrotubleVertex out = new MicrotubleVertex(VertexType.NUCLEUS);
		
		
		
		microtubules.addVertex(in1);
		microtubules.addVertex(in2);
		microtubules.addVertex(j1);
		microtubules.addVertex(j2);
		microtubules.addVertex(out);

		microtubules.addEdge(out,j1, m_j1_out);
		microtubules.addEdge(j1,in1, m1);

		
		microtubules.addEdge(out,j2,m_j2_out);
		microtubules.addEdge(j2,in2, m2);

		
		return microtubules;
		
	}
}
