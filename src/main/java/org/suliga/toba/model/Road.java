package org.suliga.toba.model;

import java.util.Objects;

public class Road {
	private Owner owner;
	private int fromVertexId;
	private int fromCol;
	private int fromRow;
	private int toVertexId;
	private int toCol;
	private int toRow;
	
	public Road(Owner owner, Vertex fromVertex, Vertex toVertex) {
		Objects.requireNonNull(owner);
		Objects.requireNonNull(fromVertex);
		Objects.requireNonNull(toVertex);
				
		this.owner = owner;
		this.fromVertexId = fromVertex.getId();
		this.fromCol = fromVertex.getCol();
		this.fromRow = fromVertex.getRow();
		this.toVertexId = toVertex.getId();
		this.toCol = toVertex.getCol();
		this.toRow = toVertex.getRow();
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public int getFromVertexId() {
		return fromVertexId;
	}

	public void setFromVertexId(int fromVertexId) {
		this.fromVertexId = fromVertexId;
	}

	public int getFromCol() {
		return fromCol;
	}

	public void setFromCol(int fromCol) {
		this.fromCol = fromCol;
	}

	public int getFromRow() {
		return fromRow;
	}

	public void setFromRow(int fromRow) {
		this.fromRow = fromRow;
	}

	public int getToVertexId() {
		return toVertexId;
	}

	public void setToVertexId(int toVertexId) {
		this.toVertexId = toVertexId;
	}

	public int getToCol() {
		return toCol;
	}

	public void setToCol(int toCol) {
		this.toCol = toCol;
	}

	public int getToRow() {
		return toRow;
	}

	public void setToRow(int toRow) {
		this.toRow = toRow;
	}
	
	@Override
	public String toString() {
		return "Road: " + owner.toString() + ", " + fromVertexId + ", " + fromCol + ":" + fromRow + ", " + toVertexId + ", " + toCol + ":" + toRow;
	}
}










