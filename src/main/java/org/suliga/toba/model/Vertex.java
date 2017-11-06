package org.suliga.toba.model;

import java.util.HashSet;
import java.util.Set;

public class Vertex {
	private boolean hidden;
	private Improvement improvement;
	private Owner owner;
	private int id;
	private Set<Integer> adjVertices;
	private int col;
	private int row;
	
	public Vertex() {}

	public Vertex(int id, boolean hidden, int col, int row) {
		this.id = id;
		this.hidden = hidden;
		this.col = col;
		this.row = row;
		this.improvement = Improvement.NONE;
		this.owner = Owner.NONE;
		adjVertices = new HashSet<>();
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public Improvement getImprovement() {
		return improvement;
	}

	public void setImprovement(Improvement improvement) {
		this.improvement = improvement;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}
	
	@Override
	public String toString() {
		if (hidden) {
			return ".";
		}
		
		return "x";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<Integer> getAdjVertices() {
		return adjVertices;
	}

	public void addAdjVertex(Integer v) {
		adjVertices.add(v);
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}
}



