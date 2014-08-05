package edu.stevens.cs.virustraffic;

import org.jgrapht.graph.DefaultEdge;

import edu.stevens.cs.virustraffic.visual.VisualConstants;

public class Microtuble extends DefaultEdge {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2146345406730212238L;
	private double length;
	private String id;
	private String style = VisualConstants.STYLE_E_MT;

	public Microtuble(){
		
	}
	public Microtuble(int length){
		this.length = length;
	}
	/**
	 * @return the length
	 */
	public double getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(double length) {
		this.length = length;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString(){
		return length + "";
	}
	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}
	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}
}
