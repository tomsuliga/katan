package org.suliga.toba.model;

public class Plot {
	private int col;
	private int row;
	private int die;
	private Resource resource;
	
	
	public Plot(int col, int row, Resource resource, int die) {
		this.col = col;
		this.row = row;
		this.resource = resource;
		this.die = die;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getDie() {
		return die;
	}
	public void setDie(int die) {
		this.die = die;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	@Override
	public String toString() {
		return "plot: " + resource + ", " + col + ":" + row + ", die=" + die;
	}
}
