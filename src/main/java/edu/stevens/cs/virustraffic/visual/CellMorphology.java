package edu.stevens.cs.virustraffic.visual;

import java.awt.Point;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public interface CellMorphology {

	public double getXOrigin();
	public double getYOrigin();
	public JPanel getPanel();
	public Point2D.Double  getPointOnSurface(double angle);
}
