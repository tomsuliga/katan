package org.suliga.toba.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Vertex {
	private Player player;
	private boolean hidden;
	private Improvement improvement; // Fort or Castle on a Vertex
	private int id;
	private Set<Integer> adjVertices;
	private List<Road> roads;
	private int col;
	private int row;
	private int rating;
	
	public Vertex() {}

	public Vertex(int id, boolean hidden, int col, int row) {
		this.id = id;
		this.hidden = hidden;
		this.col = col;
		this.row = row;
		this.improvement = Improvement.NONE;
		this.player = Player.NONE;
		adjVertices = new HashSet<>();
		roads = new ArrayList<>();
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

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	@Override
	public String toString() {
		return "Vertex " + player.toString() + ", id=" + id + ", col:row=" + col + ":" + row + ", rating=" + rating;
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

	public List<Road> getRoads() {
		return roads;
	}
	public void addRoad(Road road) {
		roads.add(road);
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public void addRating(int amount) {
		rating += amount;
	}
}



