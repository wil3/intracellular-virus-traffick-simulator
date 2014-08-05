package edu.stevens.cs.virustraffic.visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import org.jgrapht.graph.ListenableDirectedGraph;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph.mxICellVisitor;

import edu.stevens.cs.virustraffic.Microtuble;
import edu.stevens.cs.virustraffic.MicrotubleVertex;
import edu.stevens.cs.virustraffic.VertexType;
import edu.stevens.cs.virustraffic.Virus;

public class VisualVirusTrafficSimulator  extends JLayeredPane  implements RealTimeSimulation{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3522733381363225710L;
	private JGraphXAdapter<MicrotubleVertex, Microtuble> graph;
	ListenableDirectedGraph<MicrotubleVertex, Microtuble> grapht;
	

	public static int X_ORIGIN = VisualVirusTraffic.WIDTH/2;
	public static int Y_ORIGIN = VisualVirusTraffic.HEIGHT/2;

	private Random rand = new Random();
	private List<Virus> viruses;
	private CellLayoutManager cellLayout;
	VirusLayer vl;
	
	public VisualVirusTrafficSimulator(ListenableDirectedGraph<MicrotubleVertex, Microtuble> g ,List<Virus> viruses){
		//TODO move this to function parameter
		this.viruses = viruses;
		Dimension preferredSize = new  Dimension(VisualVirusTraffic.WIDTH, VisualVirusTraffic.HEIGHT);
		setPreferredSize(preferredSize);
		setLocation(0,0);

		this.grapht = g;
		this.graph = new JGraphXAdapter<MicrotubleVertex, Microtuble>(g);

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setBorder(BorderFactory.createLineBorder(Color.black));
		//graphComponent.getViewport().setOpaque(false);
		//graphComponent.setOpaque(false);
		graphComponent.setSize(VisualVirusTraffic.WIDTH, VisualVirusTraffic.HEIGHT);
		//graphComponent.getViewport().setPreferredSize(preferredSize);
		graphComponent.setLocation(0,0);

//		graph.getModel().beginUpdate();
//		CellMorphology ccm = new CircularCellMorphology(500);
//		new SingleMTOCLayoutManager(graph,ccm).createLayout(X_ORIGIN, Y_ORIGIN);
//		graph.getModel().endUpdate();


		add(graphComponent,new Integer(50));
		

	
	}
	
	
	/**
	 * This must be called after a cell layout is set
	 * @throws Exception 
	 */
	public void runSimulation() throws Exception{
		if (cellLayout == null){
			throw new Exception("Cell layout is null");
		}
		graph.getModel().beginUpdate();
		cellLayout.setGraphAdapter(graph);
		cellLayout.createLayout(X_ORIGIN, Y_ORIGIN);
		graph.getModel().endUpdate();

		JPanel morphologyPanel = cellLayout.getCellMorphology().getPanel();
		morphologyPanel.setSize(new Dimension(VisualVirusTraffic.WIDTH, VisualVirusTraffic.HEIGHT));

	//	j.setLayout(new BorderLayout());
		//j.repaint();
		add( morphologyPanel, new Integer(60));
	//	revalidate();

	//	CellMembrane c = new CellMembrane(500);
	//	add(c,new Integer(60));
	//	j.repaint();
		
		//need to update the lengths of the viruses
		updateMicrotubleLengths();
		
		/*
		 * This is when viruses start getting painted
		 */
		this.vl = new VirusLayer(viruses, graph);
		add(vl,new Integer(100));

	}

	private void updateMicrotubleLengths(){
		
		for (Microtuble mt  : grapht.edgeSet()){
			mxCell edge = getMicrotubleEdge(mt);
			mxGeometry src = edge.getSource().getGeometry();
			mxGeometry tar = edge.getTarget().getGeometry();
			Point2D.Double pt = new Point2D.Double(src.getX(), src.getY());
			
			double distance = pt.distance(tar.getX(), tar.getY());
			mt.setLength(distance);
			
		}
	}
	private mxCell getMicrotubleEdge(Microtuble mt){

		mxCell edge = null;
		for (mxCell cell : graph.getEdgeToCellMap().values()) {
			Microtuble e = (Microtuble)cell.getValue();
			if (e.equals(mt)){
				edge = cell;
				break;
			}
		}
		return edge;

	}
	private mxCell getRoot(){

		mxCell root = null;
		for (mxCell cell : graph.getVertexToCellMap().values()) {
			MicrotubleVertex v = (MicrotubleVertex)cell.getValue();
			if (v.getType() == VertexType.NUCLEUS){
				root = cell;
				break;
			}
		}
		return root;

	}

	private void printCorrdinates(){
		graph.traverse(getRoot(), true, new mxICellVisitor() { 
			public boolean visit(Object vertex, Object edge) { 
				mxCell  mxVertex = (mxCell)vertex;
				MicrotubleVertex v = (MicrotubleVertex)mxVertex.getValue();

				mxCell  mxEdge = (mxCell)edge;
				if (mxEdge != null){
					Microtuble e = (Microtuble)mxEdge.getValue();
					System.out.println(" edge=" + e.toString() + " l=" + e.getLength());

					double l = e.getLength();
					mxGeometry srcGeo = mxEdge.getSource().getGeometry();
					mxGeometry tarGeo =  mxEdge.getTarget().getGeometry();

					System.out.println("src= (" + srcGeo.getX() + "," + srcGeo.getY() + ")" + "\ttar= (" + tarGeo.getX() + "," + tarGeo.getY() + ")");

				}

				return true;
			}
		});
	}
/*

	private void grow(){
	
		//center the nucleus
		mxCell root = getRoot();
		Dimension size = graph.getCellToVertexMap().get(root).getSize();
		graph.getModel().setGeometry(root, new mxGeometry(VisualVirusTraffic.WIDTH/2, VisualVirusTraffic.HEIGHT/2, size.getWidth(), size.getHeight()));

		
		graph.traverse(root, true, new mxICellVisitor() { 
			public boolean visit(Object vertex, Object edge) { 
				mxCell  mxVertex = (mxCell)vertex;

				MicrotubleVertex v = (MicrotubleVertex)mxVertex.getValue();
				
				graph.getModel().setStyle(mxVertex, v.getStyle());
				System.out.println(" vertex=" + v.toString() );

				
				mxCell  mxEdge = (mxCell)edge;
				if (mxEdge != null){
					Microtuble e = (Microtuble)mxEdge.getValue();
					double distance = e.getLength();
					
					System.out.println(" edge=" + e.toString() );
					mxICell srcVertex = mxEdge.getSource();
					mxICell tarVertex =  mxEdge.getTarget();

					//Randomly place the microtuble on the nucleus
					int angle = rand.nextInt(360);
					
					double y = Math.sin(angle) * (double)distance;
					double x = Math.cos(angle) * (double)distance;

					
					//Need to have the start point in which we will place the n
					
					mxGeometry srcGeo = graph.getModel().getGeometry(srcVertex);
					
					double Xnew = srcGeo.getX() + x;
					double Ynew = srcGeo.getY() + y;
					
					Dimension size = ((MicrotubleVertex)tarVertex.getValue()).getSize();
					
					graph.getModel().setGeometry(tarVertex, new mxGeometry(Xnew, Ynew, size.getHeight(), size.getWidth()));

				}
				
					
				
				return true;
			}
			
			
		});
	}
*/
	/**
	 * @return the cellLayout
	 */
	public CellLayoutManager getCellLayout() {
		return cellLayout;
	}

	/**
	 * @param cellLayout the cellLayout to set
	 */
	public void setCellLayout(CellLayoutManager cellLayout) {
		this.cellLayout = cellLayout;
		try {
			runSimulation();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void start() {

		vl.getTimer().start();
	}

	@Override
	public void stop() {
		//vl.getTimer().stop();
	}

	@Override
	public void pause() {
		//vl.getTimer().stop();
		
	}

	
}
