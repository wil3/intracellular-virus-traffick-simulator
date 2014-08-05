package edu.stevens.cs.virustraffic;

public enum VertexType {
	RECEPTOR ("receptor"), 
	NUCLEUS ("nucleus"),
	MTOC ("mtoc"),
	SUDO_MTOC("sudo_mtoc"),
	JUNCTION ("junction");
	
	
	 private final String name;       

    private VertexType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }
}
