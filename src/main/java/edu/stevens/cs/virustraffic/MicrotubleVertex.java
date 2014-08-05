package edu.stevens.cs.virustraffic;

import java.awt.Dimension;

import com.mxgraph.model.mxCell;

import edu.stevens.cs.virustraffic.visual.VisualConstants;

public class MicrotubleVertex {
	private VertexType type = null;
	public static int ID_COUNT = 0;
	private String ID;

	public MicrotubleVertex(){
		this(null);
	}
	public MicrotubleVertex(VertexType type){
		this.setType(type);
		this.ID = "v-" + ID_COUNT++;

	}
	/**
	 * @return the type
	 */
	public VertexType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(VertexType type) {
		this.type = type;
		
	}
	//TODO remove place in visual
	public Dimension getSize(){
		Dimension d = new Dimension();
		switch(type){
			case RECEPTOR:
				d.setSize(1, 1);
				break;
			case NUCLEUS:
				d.setSize(50,50);
				break;
			case JUNCTION:
				d.setSize(10, 10);
				break;
			case MTOC:
				d.setSize(20,20);
				break;
			case SUDO_MTOC:
				d.setSize(10,10);
				break;
			default:
		}
		return d;
	}
	//TODO remove place in visual

	public String getStyle(){
		  StringBuilder style = new StringBuilder(); 			
		switch(type){
			case RECEPTOR:
				style.append(VisualConstants.STYLE_V_RECEPTOR);
				break;
			case NUCLEUS:
				style.append(VisualConstants.STYLE_V_NUCLEUS);
				break;
			case JUNCTION:
				style.append(VisualConstants.STYLE_V_JUNCTION);
				break;
			case MTOC:
				style.append(VisualConstants.STYLE_V_MTOC);
				break;
			case SUDO_MTOC:
				style.append(VisualConstants.STYLE_V_SUDO_MTOC);
				break;
			default:
		}
		return style.toString();
	}
	@Override
	public String toString(){
		return ID;
	}
}