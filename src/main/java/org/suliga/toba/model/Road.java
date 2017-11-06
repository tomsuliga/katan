package org.suliga.toba.model;

public class Road {
	private Vertex fromVertex;
	private Vertex toVertex;
	private Owner owner;
	
	public Road(Owner owner, Vertex fromVertex, Vertex toVertex) {
		this.fromVertex = fromVertex;
		this.toVertex = toVertex;
		this.owner = owner;
	}

	public Vertex getFromVertex() {
		return fromVertex;
	}

	public void setFromVertex(Vertex fromVertex) {
		this.fromVertex = fromVertex;
	}

	public Vertex getToVertex() {
		return toVertex;
	}

	public void setToVertex(Vertex toVertex) {
		this.toVertex = toVertex;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}
}
