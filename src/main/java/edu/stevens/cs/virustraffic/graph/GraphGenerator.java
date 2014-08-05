package edu.stevens.cs.virustraffic.graph;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.ListenableDirectedGraph;

import edu.stevens.cs.virustraffic.Microtuble;
import edu.stevens.cs.virustraffic.MicrotubleVertex;

public interface GraphGenerator {

	public ListenableDirectedGraph<MicrotubleVertex, Microtuble> generate();
}
