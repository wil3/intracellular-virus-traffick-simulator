package edu.stevens.cs.virustraffic.visual;

import edu.stevens.cs.virustraffic.Microtuble;
import edu.stevens.cs.virustraffic.MicrotubleVertex;

public interface CellLayoutManager {

	public CellMorphology getCellMorphology();
	public void createLayout(double x, double y);
	public void setGraphAdapter(JGraphXAdapter<MicrotubleVertex, Microtuble> graph);
}
