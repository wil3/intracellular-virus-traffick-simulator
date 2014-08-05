package edu.stevens.cs.virustraffic.visual;

import java.awt.Color;

public class VisualConstants {
	/**
	 * Shared amoung all
	 */
	public static String STYLE_V = "noLabel=true;";//"labelBackgroundColor=white;";//
	public static String STYLE_V_RECEPTOR = STYLE_V + "fillColor=white;strokeColor=black;shape=rectangle;";
	public static String STYLE_V_NUCLEUS = STYLE_V + "fillColor=blue;strokeColor=blue;shape=ellipse";
	public static String STYLE_V_JUNCTION = STYLE_V + "fillColor=black;strokeColor=black;shape=ellipse";
	public static String STYLE_V_MTOC = STYLE_V +  "fillColor=orange;strokeColor=black;shape=ellipse";
	public static String STYLE_V_SUDO_MTOC = STYLE_V + "fillColor=red;strokeColor=black;shape=ellipse";
	
	
	public static String STYLE_E = "noLabel=true;";
	public static String STYLE_E_MT = STYLE_E + "strokeWidth=1;endArrow=none;startArrow=none";
	public static String STYLE_E_SUDO2MTOC = STYLE_E + "strokeColor=red;strokeWidth=1;endArrow=none;startArrow=none";
	public static String SYTLE_E_MTOC2NUCLEUS = STYLE_E + "strokeColor=orange;strokeWidth=4;endArrow=none;startArrow=none";

	
	
	public static int SIZE_VIRUS = 10;
	
	public static int COLOR_VIRUS_ALPHA = 50;
	public static Color COLOR_VIRUS = new Color(0,255,0);
}
