package org.suliga.toba.model;

import java.util.concurrent.ThreadLocalRandom;

public class TobaMessage {
	private Road road1;
	private Vertex vertex1;
	private int numTurns;
	private int die1;
	private int die2;

	public Road getRoad1() {
		return road1;
	}
	
	public void rollDice() {
		die1 = ThreadLocalRandom.current().nextInt(6) + 1;
		die2 = ThreadLocalRandom.current().nextInt(6) + 1;
	}

	public void setRoad1(Road road1) {
		this.road1 = road1;
	}

	public Vertex getVertex1() {
		return vertex1;
	}

	public void setVertex1(Vertex vertex1) {
		this.vertex1 = vertex1;
	}

	public int getNumTurns() {
		return numTurns;
	}

	public void setNumTurns(int numTurns) {
		this.numTurns = numTurns;
	}

	public int getDie1() {
		return die1;
	}

	public void setDie1(int die1) {
		this.die1 = die1;
	}

	public int getDie2() {
		return die2;
	}

	public void setDie2(int die2) {
		this.die2 = die2;
	}
	
}
