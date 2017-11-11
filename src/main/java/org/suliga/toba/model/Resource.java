package org.suliga.toba.model;

public enum Resource {
	NONE("None"), ONE("Wheat"), TWO("Grass"), THREE("Rubbies"), FOUR("Cash"), FIVE("Lava"), WATER, ROBBER;
	
	private String desc;
	
	private Resource() {}
	
	private Resource(String desc) {
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		return "Resource: " + desc;
	}
}

