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
			".x.x.x.x.x.x.x.", // 90 - 104 99
			"x.x.x.x.x.x.x.x", // 105 - 119 // 109 111
			"x.x.x.x.x.x.x.x", // 120 - 134
			".x.x.x.x.x.x.x.", // 135 - 149 // 144
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
		boolean conflict = true;
		while (conflict) {
			// Cannot have 6 or 8 next to each other
			Collections.shuffle(dice);
			conflict = false;
			for (int i=0;i<dice.size();i++) {
				int die = dice.get(i);
				if (die == 6 || die == 8) {
					if (i == 0) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i+3);
						int d3 = dice.get(i+4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 1) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i+3);
						int d3 = dice.get(i+4);
						int d4 = dice.get(i-1);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 2) {
						int d1 = dice.get(i+3);
						int d2 = dice.get(i+4);
						int d3 = dice.get(i-1);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 3) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i+4);
						int d3 = dice.get(i+5);
						int d4 = dice.get(i-3);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 4) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i+4);
						int d3 = dice.get(i-1);
						int d4 = dice.get(i-3);
						int d5 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8 || d5 == 6 || d5 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 5) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i+4);
						int d3 = dice.get(i-1);
						int d4 = dice.get(i-3);
						int d5 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8 || d5 == 6 || d5 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 6) {
						int d1 = dice.get(i+3);
						int d2 = dice.get(i+4);
						int d3 = dice.get(i-1);
						int d4 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 7) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i+4);
						int d3 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 8) {
						int d1 = dice.get(i-1);
						int d2 = dice.get(i+3);
						int d3 = dice.get(i+4);
						int d4 = dice.get(i-4);
						int d5 = dice.get(i-5);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8 || d5 == 6 || d5 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 9) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i+4);
						int d3 = dice.get(i+5);
						int d4 = dice.get(i-3);
						int d5 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8 || d5 == 6 || d5 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 10) {
						int d1 = dice.get(i-1);
						int d2 = dice.get(i+4);
						int d3 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 11) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i+4);
						int d3 = dice.get(i-3);
						int d4 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 12) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i+3);
						int d3 = dice.get(i+4);
						int d4 = dice.get(i-1);
						int d5 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8 || d5 == 6 || d5 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 13) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i+3);
						int d3 = dice.get(i+4);
						int d4 = dice.get(i-1);
						int d5 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8 || d5 == 6 || d5 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 14) {
						int d1 = dice.get(i+3);
						int d2 = dice.get(i-1);
						int d3 = dice.get(i-4);
						int d4 = dice.get(i-5);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 15) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i-3);
						int d3 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 16) {
						int d1 = dice.get(i+1);
						int d2 = dice.get(i-1);
						int d3 = dice.get(i-3);
						int d4 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8 || d4 == 6 || d4 == 8) {
							conflict = true;
							break;
						}
					} else if (i == 17) {
						int d1 = dice.get(i-1);
						int d2 = dice.get(i-3);
						int d3 = dice.get(i-4);
						if (d1 == 6 || d1 == 8 || d2 == 6 || d2 == 8 || d3 == 6 || d3 == 8) {
							conflict = true;
							break;
						}
					}
				}
			}
		}
		
		List<Resource> resources = Arrays.asList(
				Resource.ONE, Resource.TWO, Resource.THREE, Resource.FOUR, Resource.FIVE,
				Resource.ONE, Resource.TWO, Resource.THREE, Resource.FOUR, Resource.FIVE,
				Resource.ONE, Resource.TWO, Resource.THREE, Resource.FOUR, Resource.FIVE,
				Resource.ONE, Resource.TWO, Resource.THREE);
		
		// Plot layout sequences that we don't want
		int[][] badSeq = { {0,1,2}, {0,3,4}, {0,1,4}, {1,2,5}, {1,4,5}, {2,5,6},
				           {3,4,5}, {4,5,6}, {3,7,8}, {3,4,8}, {5,6,9}, {6,9,10},
				           {7,8,11}, {8,11,12}, {9,10,14}, {9,13,14}, {11,12,15},
				           {12,15,16}, {13,14,17}, {11,12,13}, {12,13,14}, {15,16,17},
				           {3,8,12,16}, {1,5,9,14}, {1,4,8,11}, {6,9,13,16},
				           {1,4,8}, {4,8,11}, {1,2,4}, {9,13,16}, {8,12,16},
				           {6,10,14}, {5,9,10}, {10,13,14}, {0,4,8},
				           {1,5,9}, {6,9,14}, {11,12,16}, {12,13,17}, {13,16,17},
				           {4,5,9}, {10,14,17}, {9,12,13}, {13,14,16}, {2,6,9}, {7,11,15},
				           {2,4,5}, {1,2,6}, {4,5,8}, {4,8,12}, {9,13,17}, {1,5,6},
				           {13,15,16}, {3,8,11}, {8,11,15}, {1,3,4}, {3,4,7}, {8,12,15},
				           {9,10,13}, {2,5,9}, {5,9,13}, {2,6,10},
				           {6,9,13}, {12,16,17}, {0,1,5}, {14,16,17}, {0,3,8}, {8,12,13},
				           {11,15,16}, {0,3,7}, {3,8,12}, {5,9,14}, {9,14,17}, {7,8,12},
				           {5,6,10}, {0,4,5}, {4,7,8}, {0,1,3}, {3,7,11},
				           {7,11,12}, {12,13,15}, {12,13,16} };

		conflict = true;
		while (conflict) {
			Collections.shuffle(resources);
			conflict = false;
			for (int i=0;i<badSeq.length;i++) {
				boolean allSame = true;
				Resource r = resources.get(badSeq[i][0]);
				for (int j=1;j<badSeq[i].length;j++) {
					if (r != resources.get(badSeq[i][j])) {
						allSame = false;
						break;
					}
				}
				if (allSame) {
					conflict = true;
					break;
				}
			}
		}
		
		plots.add(new Plot(2,0,Resource.WATER,0));
		plots.add(new Plot(3,0,Resource.WATER,0));
		plots.add(new Plot(4,0,Resource.WATER,0));
		plots.add(new Plot(5,0,Resource.WATER,0));

		plots.add(new Plot(1,1,Resource.WATER,0));
		plots.add(new Plot(2,1,resources.get(0),dice.get(0)));
		plots.add(new Plot(3,1,resources.get(1),dice.get(1)));
		plots.add(new Plot(4,1,resources.get(2),dice.get(2)));
		plots.add(new Plot(5,1,Resource.WATER,0));

		plots.add(new Plot(1,2,Resource.WATER,0));
		plots.add(new Plot(2,2,resources.get(3),dice.get(3)));
		plots.add(new Plot(3,2,resources.get(4),dice.get(4)));
		plots.add(new Plot(4,2,resources.get(5),dice.get(5)));
		plots.add(new Plot(5,2,resources.get(6),dice.get(6)));
		plots.add(new Plot(6,2,Resource.WATER,0));

		plots.add(new Plot(0,3,Resource.WATER,0));
		plots.add(new Plot(1,3,resources.get(7),dice.get(7)));
		plots.add(new Plot(2,3,resources.get(8),dice.get(8)));
		plots.add(new Plot(3,3,Resource.ROBBER,7));
		plots.add(new Plot(4,3,resources.get(9),dice.get(9)));
		plots.add(new Plot(5,3,resources.get(10),dice.get(10)));
		plots.add(new Plot(6,3,Resource.WATER,0));

		plots.add(new Plot(1,4,Resource.WATER,0));
		plots.add(new Plot(2,4,resources.get(11),dice.get(11)));
		plots.add(new Plot(3,4,resources.get(12),dice.get(12)));
		plots.add(new Plot(4,4,resources.get(13),dice.get(13)));
		plots.add(new Plot(5,4,resources.get(14),dice.get(14)));
		plots.add(new Plot(6,4,Resource.WATER,0));

		plots.add(new Plot(1,5,Resource.WATER,0));
		plots.add(new Plot(2,5,resources.get(15),dice.get(15)));
		plots.add(new Plot(3,5,resources.get(16),dice.get(16)));
		plots.add(new Plot(4,5,resources.get(17),dice.get(17)));
		plots.add(new Plot(5,5,Resource.WATER,0));

		plots.add(new Plot(2,6,Resource.WATER,0));
		plots.add(new Plot(3,6,Resource.WATER,0));
		plots.add(new Plot(4,6,Resource.WATER,0));
		plots.add(new Plot(5,6,Resource.WATER,0));
		
		// temp
		adjMap.get(66).setImprovement(Improvement.TOWN);
		adjMap.get(66).setOwner(Owner.P1);
		adjMap.get(68).setImprovement(Improvement.CITY);
		adjMap.get(68).setOwner(Owner.P1);

		adjMap.get(109).setImprovement(Improvement.TOWN);
		adjMap.get(109).setOwner(Owner.P2);
		adjMap.get(111).setImprovement(Improvement.CITY);
		adjMap.get(111).setOwner(Owner.P2);

		adjMap.get(157).setImprovement(Improvement.TOWN);
		adjMap.get(157).setOwner(Owner.P3);
		adjMap.get(155).setImprovement(Improvement.CITY);
		adjMap.get(155).setOwner(Owner.P3);

		adjMap.get(99).setImprovement(Improvement.TOWN);
		adjMap.get(99).setOwner(Owner.P4);
		adjMap.get(144).setImprovement(Improvement.CITY);
		adjMap.get(144).setOwner(Owner.P4);
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





