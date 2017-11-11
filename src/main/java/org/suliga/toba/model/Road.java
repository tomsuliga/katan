package org.suliga.toba.model;

import java.util.Objects;

public class Road {
	private Player player;
	private int fromVertexId;
	private int fromCol;
	private int fromRow;
	private int toVertexId;
	private int toCol;
	private int toRow;
	
	public Road(Player player, Vertex fromVertex, Vertex toVertex) {
		Objects.requireNonNull(player);
		Objects.requireNonNull(fromVertex);
		Objects.requireNonNull(toVertex);
				
		this.player = player;
		this.fromVertexId = fromVertex.getId();
		this.fromCol = fromVertex.getCol();
		this.fromRow = fromVertex.getRow();
		this.toVertexId = toVertex.getId();
		this.toCol = toVertex.getCol();
		this.toRow = toVertex.getRow();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
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
		return "Road: " + player.toString() + ", " + fromVertexId + ", " + fromCol + ":" + fromRow + ", " + toVertexId + ", " + toCol + ":" + toRow;
	}
}










