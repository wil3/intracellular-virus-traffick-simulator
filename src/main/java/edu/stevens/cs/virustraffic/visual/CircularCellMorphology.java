package edu.stevens.cs.virustraffic.visual;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public class CircularCellMorphology implements CellMorphology {
	private double diameter;
	private JPanel panel;
	public CircularCellMorphology(double diameter){
		this.diameter = diameter;
		
		createPanel();
		//this.panel = new MyPanel();
	}
	
	private void createPanel(){
		this.panel = new JPanel(){

			private static final long serialVersionUID = -8851565719382463946L;
			public void paintComponent(Graphics g){
				
				super.paintComponent(g);
				//System.out.println("repaint membrane");
				Graphics2D g2d = (Graphics2D)g;
				Toolkit.getDefaultToolkit().sync();
				
				Ellipse2D circle = new Ellipse2D.Double((VisualVirusTraffic.WIDTH/2) - (diameter/2), (VisualVirusTraffic.HEIGHT/2) - (diameter/2), diameter,diameter);
			//	g2d.setPaint(Color.blue);
			//	g2d.fill (circle);
			//	g2d.draw(circle);
				
				g2d.setPaint(Color.black);
				g2d.setStroke( new BasicStroke(3));
				g2d.draw(circle);
				Toolkit.getDefaultToolkit().sync();

		        g.dispose();
				
			}
		};
		//panel.setSize(new Dimension(VisualVirusTraffic.WIDTH, VisualVirusTraffic.HEIGHT));
		//setBackground(Color.gray);
		panel.setOpaque(false);
		panel.setLocation(0,0);
	}
	@Override
	public double getXOrigin() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getYOrigin() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point2D.Double getPointOnSurface(double angle) {

		double y = Math.sin(angle) * (diameter/2);
		double x = Math.cos(angle) * (diameter/2);
		return new Point2D.Double(x, y);
	}

	
	@Override
	public JPanel getPanel() {
		return panel;
	}
	/*
	public class MyPanel extends JPanel {
		
		private static final long serialVersionUID = -5647651971212067513L;
		public MyPanel(){
			setLayout(new BorderLayout());
			setOpaque(false);
			setLocation(0,0);
		}

		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			Graphics2D g2d = (Graphics2D)g;
			Toolkit.getDefaultToolkit().sync();
			
			Ellipse2D circle = new Ellipse2D.Double((VisualVirusTraffic.WIDTH/2) - (diameter/2), (VisualVirusTraffic.HEIGHT/2) - (diameter/2), diameter,diameter);
		//	g2d.setPaint(Color.blue);
		//	g2d.fill (circle);
		//	g2d.draw(circle);
			
			g2d.setPaint(Color.black);
			g2d.setStroke( new BasicStroke(3));
			g2d.draw(circle);
			
	        g.dispose();
		}
	}*/
}
