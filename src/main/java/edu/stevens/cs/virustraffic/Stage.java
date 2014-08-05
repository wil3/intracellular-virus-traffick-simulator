package edu.stevens.cs.virustraffic;

public enum Stage {
	/**
	 * Free outside cell
	 */
	FREE("Free"),
	/**
	 * Bound to the cell surface
	 */
	BOUND("Bound"),
	/**
	 * Entering cell
	 */
	ENDOSOME("Endosome"),
	/**
	 * In side cell
	 */
	CYTOSOL ("Cytosol"),
	
	MICROTUBLE_ASSOC("MT"),
	
	NUCLEUS_ASSOC("Nuc");
	
	
	 private final String name;       

	    private Stage(String s) {
	        name = s;
	    }

	 public String toString(){
	       return name;
	    }
}
