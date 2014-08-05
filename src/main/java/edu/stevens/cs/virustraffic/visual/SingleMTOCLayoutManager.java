package edu.stevens.cs.virustraffic.visual;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

import edu.stevens.cs.virustraffic.Microtuble;
import edu.stevens.cs.virustraffic.MicrotubleVertex;
import edu.stevens.cs.virustraffic.VertexType;


/**
 * Responsible for positioning the graph in the correct layout format along the cell membrane
 * @author wil
 *
 */
//TODO have as super class to MultiMTOC
public class SingleMTOCLayoutManager implements CellLayoutManager {
	
	private JGraphXAdapter<MicrotubleVertex, Microtuble> graph;
	private Random rand = new Random();
	private CellMorphology morphology;
	
	public SingleMTOCLayoutManager( CellMorphology morphology){
		this.morphology = morphology;
	}
	
	/**
	 * 
	 */
	public void createLayout(double x_origin, double y_origin){
		
		//Set location of nucleus in the center
		mxCell root = getCellByType(VertexType.NUCLEUS).get(0);
		Dimension size = graph.getCellToVertexMap().get(root).getSize();
		MicrotubleVertex vRoot = (MicrotubleVertex)root.getValue();
		graph.getModel().setStyle(root, vRoot.getStyle());
		graph.getModel().setGeometry(root, new mxGeometry(x_origin, y_origin, size.getWidth(), size.getHeight()));

		//Set location of MTOC
		
		//To select a random length between nucleus and MTOC
		//int l = r.nextInt(maxLength-minLength) + minLength;
		//e.setLength(l);
		
		
		mxCell mtoc = getCellByType(VertexType.MTOC).get(0);
		int angle = rand.nextInt(360);
		double distance = 100;
		double y = Math.sin(angle) * (double)distance;
		double x = Math.cos(angle) * (double)distance;
		MicrotubleVertex v = (MicrotubleVertex)mtoc.getValue();
		graph.getModel().setStyle(mtoc, v.getStyle());
		graph.getModel().setGeometry(mtoc, new mxGeometry(x + x_origin, y+ y_origin, v.getSize().getWidth(), v.getSize().getHeight()));

		//handle the rest
		positionReceptors(x_origin, y_origin);
				
		setEdgeStyles();
	}
	
	private void positionReceptors(double x, double y){
		List<mxCell> cells = getCellByType(VertexType.RECEPTOR);

		for (mxCell cell : cells){
			int angle = rand.nextInt(360);
			Point2D.Double p = morphology.getPointOnSurface(angle);
			
			MicrotubleVertex v = (MicrotubleVertex)cell.getValue();
			graph.getModel().setStyle(cell, v.getStyle());
			graph.getModel().setGeometry(cell, new mxGeometry(p.getX() + x, p.getY() + y, v.getSize().getWidth(), v.getSize().getHeight()));

		}
		
	}
	private void setEdgeStyles(){
		for (mxCell cell : graph.getEdgeToCellMap().values()) {
			Microtuble e = (Microtuble)cell.getValue();
			graph.getModel().setStyle(cell, e.getStyle());
		}
	}
	private List<mxCell> getCellByType(VertexType type){
		List<mxCell> cells = new ArrayList<mxCell>();
		mxCell root = null;
		for (mxCell cell : graph.getVertexToCellMap().values()) {
			MicrotubleVertex v = (MicrotubleVertex)cell.getValue();
			if (v.getType() == type){
				cells.add(cell);
			}
		}
		return cells;

	}

	@Override
	public void setGraphAdapter(
			JGraphXAdapter<MicrotubleVertex, Microtuble> graph) {
		this.graph = graph;
	}

	@Override
	public CellMorphology getCellMorphology() {
		return morphology;
	}


}
