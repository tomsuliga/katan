package org.suliga.toba.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
	public static final int NUM_COLS = 15;
	public static final int NUM_ROWS = 16;
	
	private Vertex[][] vertices;
	
	private Map<Integer, Vertex> adjMap;
	
	private List<Plot> plots;
	
	private String layout[] = {
			"....x.x.x.x....", // 0 - 14
			"...x.x.x.x.x...", // 15 - 29
			"...x.x.x.x.x...", // 30 - 44
			"..x.x.x.x.x.x..", // 45 - 59
			"..x.x.x.x.x.x..", // 60 - 74
			".x.x.x.x.x.x.x.", // 75 - 89
			".x.x.x.x.x.x.x.", // 90 - 104
			"x.x.x.x.x.x.x.x", // 105 - 119
			"x.x.x.x.x.x.x.x", // 120 - 134
			".x.x.x.x.x.x.x.", // 135 - 149
			".x.x.x.x.x.x.x.", // 150 - 164
			"..x.x.x.x.x.x..", // 165 - 179
			"..x.x.x.x.x.x..", // 180 - 194
			"...x.x.x.x.x...", // 195 - 209
			"...x.x.x.x.x...", // 210 - 224
			"....x.x.x.x...."  // 225 - 239
	};
	
	public Game() {
		init();
	}
	
	private void init() {
		vertices = new Vertex[NUM_COLS][NUM_ROWS];
		adjMap  = new HashMap<>();
		plots = new ArrayList<>();
		int id = 0;
		Vertex v = null;
		
		for (int row=0;row<NUM_ROWS;row++) {
			for (int col=0;col<NUM_COLS;col++) {
				boolean hidden = layout[row].charAt(col) == '.' ? true : false;
				v = new Vertex(id, hidden, col, row);
				vertices[col][row] = v;
				adjMap.put(id,  v);
				id++;
			}
		}
		
		buildHexEdges(4);		
		buildHexEdges(6);
		buildHexEdges(8);
		buildHexEdges(10);

		buildHexEdges(33);
		buildHexEdges(35);
		buildHexEdges(37);
		buildHexEdges(39);
		buildHexEdges(41);

		buildHexEdges(62);
		buildHexEdges(64);
		buildHexEdges(66);
		buildHexEdges(68);
		buildHexEdges(70);
		buildHexEdges(72);

		buildHexEdges(91);
		buildHexEdges(93);
		buildHexEdges(95);
		buildHexEdges(97);
		buildHexEdges(99);
		buildHexEdges(101);
		buildHexEdges(103);

		buildHexEdges(122);
		buildHexEdges(124);
		buildHexEdges(126);
		buildHexEdges(128);
		buildHexEdges(130);
		buildHexEdges(132);

		buildHexEdges(153);
		buildHexEdges(155);
		buildHexEdges(157);
		buildHexEdges(159);
		buildHexEdges(161);

		buildHexEdges(184);
		buildHexEdges(186);
		buildHexEdges(188);
		buildHexEdges(190);
		
		List<Integer> dice = Arrays.asList(2,3,4,5,6,8,9,10,11,12,6,8,5,9,4,10,3,11);
		Collections.shuffle(dice);
		
		plots.add(new Plot(2,0,Resource.WATER,0));
		plots.add(new Plot(3,0,Resource.WATER,0));
		plots.add(new Plot(4,0,Resource.WATER,0));
		plots.add(new Plot(5,0,Resource.WATER,0));

		plots.add(new Plot(1,1,Resource.WATER,0));
		plots.add(new Plot(2,1,Resource.ONE,dice.get(0)));
		plots.add(new Plot(3,1,Resource.TWO,dice.get(1)));
		plots.add(new Plot(4,1,Resource.THREE,dice.get(2)));
		plots.add(new Plot(5,1,Resource.WATER,0));

		plots.add(new Plot(1,2,Resource.WATER,0));
		plots.add(new Plot(2,2,Resource.FOUR,dice.get(3)));
		plots.add(new Plot(3,2,Resource.FIVE,dice.get(4)));
		plots.add(new Plot(4,2,Resource.ONE,dice.get(5)));
		plots.add(new Plot(5,2,Resource.TWO,dice.get(6)));
		plots.add(new Plot(6,2,Resource.WATER,0));

		plots.add(new Plot(0,3,Resource.WATER,0));
		plots.add(new Plot(1,3,Resource.THREE,dice.get(7)));
		plots.add(new Plot(2,3,Resource.FOUR,dice.get(8)));
		plots.add(new Plot(3,3,Resource.ROBBER,7));
		plots.add(new Plot(4,3,Resource.FIVE,dice.get(9)));
		plots.add(new Plot(5,3,Resource.ONE,dice.get(10)));
		plots.add(new Plot(6,3,Resource.WATER,0));

		plots.add(new Plot(1,4,Resource.WATER,0));
		plots.add(new Plot(2,4,Resource.TWO,dice.get(11)));
		plots.add(new Plot(3,4,Resource.THREE,dice.get(12)));
		plots.add(new Plot(4,4,Resource.FOUR,dice.get(13)));
		plots.add(new Plot(5,4,Resource.FIVE,dice.get(14)));
		plots.add(new Plot(6,4,Resource.WATER,0));

		plots.add(new Plot(1,5,Resource.WATER,0));
		plots.add(new Plot(2,5,Resource.ONE,dice.get(15)));
		plots.add(new Plot(3,5,Resource.TWO,dice.get(16)));
		plots.add(new Plot(4,5,Resource.THREE,dice.get(17)));
		plots.add(new Plot(5,5,Resource.WATER,0));

		plots.add(new Plot(2,6,Resource.WATER,0));
		plots.add(new Plot(3,6,Resource.WATER,0));
		plots.add(new Plot(4,6,Resource.WATER,0));
		plots.add(new Plot(5,6,Resource.WATER,0));
		
		// temp
		adjMap.get(66).setImprovement(Improvement.TOWN);
		adjMap.get(68).setImprovement(Improvement.CITY);
	}
	
	private void buildHexEdges(int id) {
		adjMap.get(id).addAdjVertex(id+14);
		adjMap.get(id+14).addAdjVertex(id);
		
		adjMap.get(id+14).addAdjVertex(id+29);
		adjMap.get(id+29).addAdjVertex(id+14);
		
		adjMap.get(id+29).addAdjVertex(id+45);
		adjMap.get(id+45).addAdjVertex(id+29);
		
		adjMap.get(id+45).addAdjVertex(id+31);
		adjMap.get(id+31).addAdjVertex(id+45);
		
		adjMap.get(id+31).addAdjVertex(id+16);
		adjMap.get(id+16).addAdjVertex(id+31);
		
		adjMap.get(id).addAdjVertex(id+16);
		adjMap.get(id+16).addAdjVertex(id);
	}

	public Vertex[][] getVertices() {
		return vertices;
	}

	public Map<Integer, Vertex> getAdjMap() {
		return adjMap;
	}

	public List<Plot> getPlots() {
		return plots;
	}
}





