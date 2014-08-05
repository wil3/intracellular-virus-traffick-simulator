package edu.stevens.cs.virustraffic.visual;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.apache.log4j.Logger;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;

import edu.stevens.cs.virustraffic.Microtuble;
import edu.stevens.cs.virustraffic.MicrotubleVertex;
import edu.stevens.cs.virustraffic.Stage;
import edu.stevens.cs.virustraffic.Virus;
import edu.stevens.cs.virustraffic.VirusTrafficSimulator;

/**
 * 
 * @author wil koch
 *
 */
public class VirusLayer extends JPanel {
	Logger logger = Logger.getLogger(VirusLayer.class);
	
	public static int REPAINT_RATE = 100;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7941091905026990584L;
	
	private Timer timer;
	private List<Virus> viruses;
	private int diameter = VisualConstants.SIZE_VIRUS;
	JGraphXAdapter<MicrotubleVertex, Microtuble> graph;

	public VirusLayer(List<Virus> viruses, 	JGraphXAdapter<MicrotubleVertex, Microtuble> graph){
		this.graph = graph;
		this.viruses = viruses;
		setSize(new Dimension(VisualVirusTraffic.WIDTH, VisualVirusTraffic.HEIGHT));
		setOpaque(false);
		setLocation(0,0);

		//TODO remove descretestep class and embedd here since all it does is repaint
	//	timer = new Timer(100, new DescreteStep(this));//VirusTrafficSimulator.TIME_STEP
		
		this.timer = new Timer(REPAINT_RATE, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				VirusLayer.this.repaint();
			}
		});//VirusTrafficSimulator.TIME_STEP

	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		Toolkit.getDefaultToolkit().sync();
		
		for (Virus virus : viruses){
			drawVirus(virus, g2d);

		}
		Toolkit.getDefaultToolkit().sync();
        g.dispose();
	}
	
	private void drawVirus(Virus virus, Graphics2D g2d){
		
		//Only draw the virus if it has bound to the cell
		if (virus.getStage() != Stage.FREE) {
			double [] xy = getGraphCoordinates(virus);
		//	logger.info(virus + "\tx=" + (xy[0]-VisualVirusTraffic.WIDTH) + "\ty="+ (xy[1]-VisualVirusTraffic.HEIGHT));
			Ellipse2D circle = new Ellipse2D.Double(xy[0], xy[1], diameter, diameter);
			//VisualConstants.COLOR_VIRUS.
			Color c = new Color(0,255,0, VisualConstants.COLOR_VIRUS_ALPHA);
			g2d.setPaint(c);//VisualConstants.COLOR_VIRUS);
			g2d.fill (circle);
			g2d.draw(circle);
			
			g2d.setPaint(Color.black);
			g2d.setStroke( new BasicStroke(1));
			g2d.draw(circle);
		}
	}
	
	/**
	 * This is where the virus model must interface with the graphics
	 * @param virus
	 * @return
	 */
	private double [] getGraphCoordinates(Virus virus){
		double pos = virus.getPosition();
		mxCell edge = getEdge(virus.getMicrotuble());
		mxICell src = edge.getSource();
		mxICell tar = edge.getTarget();
		//System.out.println("e=" + edge.getValue() + "\tsrc=" + src.getValue());
		mxGeometry srcGeo = graph.getModel().getGeometry(src);
		mxGeometry tarGeo = graph.getModel().getGeometry(tar);
		
		//where we need to go to
		double srcX = srcGeo.getCenterX();
		double srcY = srcGeo.getCenterY();
		
		//This is the start position
		double tarX = tarGeo.getCenterX();
		double tarY = tarGeo.getCenterY();
		
		double tan = (tarY - srcY)/(tarX - srcX) ;
		double angle =  Math.atan(tan);
		
		double _y = Math.sin(angle) * pos;
		double _x = Math.cos(angle) * pos;
		double y,x;
		//determine direction

		
		if (tarY >= srcY && tarX >= srcX){ //ok
			x = tarX - _x;
			y = tarY - _y;
		} else if (tarY < srcY && tarX > srcX){
			x = tarX - _x;
			y = tarY - _y;
		} else if (tarY > srcY && tarX < srcX){ //ok
			x = tarX + _x;
			y = tarY + _y;

		} else {// if (tarY < srcY && tarX < srcX){
			x = tarX + _x;
			y = tarY + _y;
		}
		//System.out.println("(" + _x + "," + _y + ")\t" + "(" + x + "," + y + ")");

		return new double[] {x,y};
	}
	
	

	/**
	 * Another adapter from jgrapht to jgraphx
	 * Find the edge in jgraphx
	 * @param m
	 * @return
	 */
	
	//TODO can this be added in JGraphXAdapter?
	private mxCell getEdge(Microtuble m){
		mxCell edge = null;
		for (mxCell cell : graph.getEdgeToCellMap().values()){
			Microtuble mt = (Microtuble) cell.getValue();
			if (mt.equals(m)){
				edge = cell;
				break;
			}
		}
		return edge;
	}

	/**
	 * @return the timer
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * @param timer the timer to set
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
}
