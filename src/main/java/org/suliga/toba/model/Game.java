package org.suliga.toba.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {
	private static final Logger logger = LoggerFactory.getLogger(Game.class);

	public static final int NUM_COLS = 15;
	public static final int NUM_ROWS = 16;
	
	private String sessionId;
	
	private Vertex[][] vertices;
	private List<Vertex> verticesList;
	
	private Map<Integer, Vertex> adjMap;

	private Map<Integer, Set<Plot>> vertexPlotMap;
	
	private List<Plot> plots;

	private List<Road> roads;
	
	private int[][] playerCards;
	
	private int[] playerCardsTotal;

	private int currentTurn;
	
	private Player currentPlayer;
	
	private int[] numForts;
	
	private int[] numCastles;

	private int[] numVictoryPoints;

	private int[] numLongestRoad;
		
	private int die1;
	private int die2;
	
	public Game() {}
	
	public Game(String sessionId) {
		this.sessionId = sessionId;
		init();
	}
	
	public void init() {
		vertices = new Vertex[NUM_COLS][NUM_ROWS];
		verticesList = new ArrayList<>();
		adjMap  = new HashMap<>();
		vertexPlotMap = new HashMap<>();
		plots = new ArrayList<>();
		roads = new ArrayList<>();
		numForts = new int[4];
		numCastles = new int[4];
		numVictoryPoints = new int[4];
		numLongestRoad = new int[4];
		playerCards = new int[4][5];
		playerCardsTotal = new int[4];
		int id = 0;
		Vertex v = null;
		currentTurn = 1;
		currentPlayer = Player.P1;
		die1 = 0;
		die2 = 0;
		
		String layout[] = {
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
		
		// Create full matrix grid for all possible points - visible and hidden
		for (int row=0;row<NUM_ROWS;row++) {
			for (int col=0;col<NUM_COLS;col++) {
				boolean hidden = layout[row].charAt(col) == '.' ? true : false;
				v = new Vertex(id, hidden, col, row);
				vertices[col][row] = v;
				verticesList.add(v);
				adjMap.put(id,  v);
				id++;
			}
		}
		
		// Create edges for the 37 resource plots
		int[] hexEdges = {
				4,6,8,10,
				33,35,37,39,41,
				62,64,66,68,70,72,
				91,93,95,97,99,101,103,
				122,124,126,128,130,132,
				153,155,157,159,161,
				184,186,188,190
		};
		for (int i=0;i<hexEdges.length;i++) {
			buildHexEdges(hexEdges[i]);
		}
		
		// Don't allow dice 6 and 8 to be next to each other
		List<Integer> dice = Arrays.asList(2,3,4,5,6,8,9,10,11,12,6,8,5,9,4,10,3,11);
		int[][] badDice = { {0,1,3,4},
				            {1,2,4,5,0},
				            {2,5,6,1},
				            {3,4,7,8,0},
				            {4,5,8,3,1,0},
				            {5,6,9,4,2,1},
				            {6,9,10,5,2},
				            {7,8,11,3},
				            {8,7,11,12,4,3},
				            {9,10,13,14,6,5},
				            {10,9,14,6},
				            {11,12,15,8,7},
				            {12,13,15,16,11,8},
				            {13,14,16,17,12,9},
				            {14,17,13,10,9},
				            {15,16,12,11},
				            {16,17,15,13,12},
				            {17,16,14,13}};
		boolean conflict = true;
		while (conflict) {
			// Cannot have 6 or 8 next to each other
			Collections.shuffle(dice);
			conflict = false;
			for (int i=0;i<badDice.length;i++) {
				int die = dice.get(badDice[i][0]);
				if (die == 6 || die == 8) {
					for (int j=1;j<badDice[i].length;j++) {
						die = dice.get(badDice[i][j]);
						if (die == 6 || die == 8) {
							conflict = true;
							break;
						}
					}
				}
				if (conflict) {
					break;
				}
			}
		}
		
		// Number of resource plots is either 3 or 4 per type
		List<Resource> resources = Arrays.asList(
				Resource.ONE, Resource.TWO, Resource.THREE, Resource.FOUR, Resource.FIVE,
				Resource.ONE, Resource.TWO, Resource.THREE, Resource.FOUR, Resource.FIVE,
				Resource.ONE, Resource.TWO, Resource.THREE, Resource.FOUR, Resource.FIVE,
				Resource.ONE, Resource.TWO, Resource.THREE);
		
		// Resource plot layout sequences that we don't want
		int[][] badResources = { 
				{0,1,2}, {0,3,4}, {0,1,4}, {1,2,5}, {1,4,5}, {2,5,6},
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
			for (int i=0;i<badResources.length;i++) {
				boolean allSame = true;
				Resource r = resources.get(badResources[i][0]);
				for (int j=1;j<badResources[i].length;j++) {
					if (r != resources.get(badResources[i][j])) {
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
		
		plots.add(new Plot(2,0,Resource.WATER,-1));
		plots.add(new Plot(3,0,Resource.WATER,0));
		plots.add(new Plot(4,0,Resource.WATER,1));
		plots.add(new Plot(5,0,Resource.WATER,0));

		plots.add(new Plot(1,1,Resource.WATER,0));
		plots.add(new Plot(2,1,resources.get(0),dice.get(0)));
		plots.add(new Plot(3,1,resources.get(1),dice.get(1)));
		plots.add(new Plot(4,1,resources.get(2),dice.get(2)));
		plots.add(new Plot(5,1,Resource.WATER,-2));

		plots.add(new Plot(1,2,Resource.WATER,1));
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
		plots.add(new Plot(6,3,Resource.WATER,1));

		plots.add(new Plot(1,4,Resource.WATER,-3));
		plots.add(new Plot(2,4,resources.get(11),dice.get(11)));
		plots.add(new Plot(3,4,resources.get(12),dice.get(12)));
		plots.add(new Plot(4,4,resources.get(13),dice.get(13)));
		plots.add(new Plot(5,4,resources.get(14),dice.get(14)));
		plots.add(new Plot(6,4,Resource.WATER,0));

		plots.add(new Plot(1,5,Resource.WATER,0));
		plots.add(new Plot(2,5,resources.get(15),dice.get(15)));
		plots.add(new Plot(3,5,resources.get(16),dice.get(16)));
		plots.add(new Plot(4,5,resources.get(17),dice.get(17)));
		plots.add(new Plot(5,5,Resource.WATER,-4));

		plots.add(new Plot(2,6,Resource.WATER,1));
		plots.add(new Plot(3,6,Resource.WATER,0));
		plots.add(new Plot(4,6,Resource.WATER,-5));
		plots.add(new Plot(5,6,Resource.WATER,0));	
		
		// Needed for all 18 plots, each plot has 6 points
		
		int[][] pointTips = { 
					{5,35}, {6,37}, {7,39}, 
					{10,64}, {11,66}, {12,68}, {13,70},
					{16,93}, {17,95}, {19,99}, {20,101},
					{23,124}, {24,126}, {25,128}, {26,130},
					{29,155}, {30,157}, {31,159} };
		
		int[] pointOffsets = { 0, 14, 16, 29, 31, 45 };
		
		for (int i=0;i<pointTips.length;i++) {
			for (int j=0;j<pointOffsets.length;j++) {
				int pointId = pointTips[i][1] + pointOffsets[j];
				Set<Plot> setPlots = vertexPlotMap.get(pointId);
				if (setPlots == null) {
					setPlots = new HashSet<>();
				}
				setPlots.add(plots.get(pointTips[i][0]));
				vertexPlotMap.put(pointId, setPlots);
			}
		}
		
		rateVertices();
	}
	
	private void rateVertices() {
		verticesList.forEach(v -> {
			v.setRating(0);
			Set<Plot> s = vertexPlotMap.get(v.getId());
			if (s != null) {
				s.forEach(p -> {
					v.addRating(p.getDieRated());
				});
			}		
		});
		Collections.sort(verticesList, (v1,v2) -> v2.getRating() - v1.getRating());
	}

	public void doBestPlacementMove() {
		for (int i=0;i<verticesList.size();i++) {
			Vertex v = verticesList.get(i);
			if (isImprovementAllowed(v)) {
				v.setPlayer(currentPlayer);
				v.setImprovement(Improvement.FORT);
				addNumForts(currentPlayer.ordinal()-1);
				if (currentTurn >= 5 && currentTurn <= 8) {
					handOutResourcesForVertex(v);
				}
				// Rewrite this Road code at some point...
				Set<Integer> pool = v.getAdjVertices();
				List<Integer> pool2 = pool.stream().collect(Collectors.toList());
				int id = pool2.get(ThreadLocalRandom.current().nextInt(pool2.size()));
				Vertex v2 = adjMap.get(id);
				Road road = new Road(currentPlayer, v, v2);
				v.addRoad(road);
				v2.addRoad(road);
				addRoad(road);
				break;
			}
		}
	}
	
	public boolean isImprovementAllowed(Vertex v) {
		if (v.getImprovement() != Improvement.NONE) {
			return false;
		}
		
		boolean[] good = new boolean[1];
		good[0] = true;
		v.getAdjVertices().forEach(i -> {
			Vertex v2 = this.getAdjMap().get(i);
			if (v2.getImprovement() != Improvement.NONE) {
				good[0] = false; 
			}
		});
		return good[0];
	}
	
	private void calculateLongestRoad() {
		numLongestRoad = new int[4];
		for (int currPlayer=1;currPlayer<5;currPlayer++) {
			boolean[] pointsVisited = new boolean[240];
			int[] owner = new int[1];
			owner[0] = currPlayer;
			Deque<Road> roadStack = new LinkedList<>();
			verticesList.forEach(v -> {
				recursiveLongestRoad(pointsVisited, v, owner, roadStack);
				pointsVisited[v.getId()] = false;
			});
		}
	}
	
	private void recursiveLongestRoad(boolean[] pointsVisited, Vertex v, int[] owner, Deque<Road> roadStack) {
		pointsVisited[v.getId()] = true;
		v.getRoads().forEach(r -> {
			if (r.getPlayer().ordinal() == owner[0]) {
				if (r != roadStack.peek()) {
					roadStack.push(r);
					numLongestRoad[owner[0]-1] = Math.max(numLongestRoad[owner[0]-1], roadStack.size());
					Vertex nextVertex = adjMap.get(r.getToVertexId());
					if (!pointsVisited[nextVertex.getId()]) {
						if (nextVertex.getImprovement() == Improvement.NONE || nextVertex.getPlayer().ordinal() == owner[0]) {
							recursiveLongestRoad(pointsVisited, nextVertex, owner, roadStack);
						}
					}
					roadStack.pop();
				}
			}
		});
	}		

	public void handOutResourcesForVertex(Vertex v) {
		Set<Plot> s = vertexPlotMap.get(v.getId());
		if (s != null) {
			s.forEach(p -> {
				int i = v.getPlayer().ordinal()-1;
				int j = p.getResource().ordinal()-1;
				playerCards[i][j]++;
				playerCardsTotal[i]++;
				logger.info("i,j=" + i + "," + j + ", value=" + playerCards[i][j]);
			});
		}
	}
	
	private void buildHexEdges(int id) {
		// These are the 6 edges - each with a From and a To
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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void endTurn() {
		currentTurn++;
		
		switch (currentPlayer) {
		case P1:
			currentPlayer = Player.P2;
			break;
		case P2:
			currentPlayer = Player.P3;
			break;
		case P3:
			currentPlayer = Player.P4;
			break;
		case P4:
			currentPlayer = Player.P1;
			break;
		case NONE:
		default:
			break;
		}
	}

	public int[] getNumForts() {
		return numForts;
	}

	public void setNumForts(int[] numForts) {
		this.numForts = numForts;
	}
	
	public void addNumForts(int index) {
		numForts[index]++;
		addNumVictoryPoints(index);
	}

	public int[] getNumVictoryPoints() {
		return numVictoryPoints;
	}

	public void setNumVictoryPoints(int[] numVictoryPoints) {
		this.numVictoryPoints = numVictoryPoints;
	}
	
	public void addNumVictoryPoints(int index) {
		this.numVictoryPoints[index]++;
	}

	public int[] getNumCastles() {
		return numCastles;
	}

	public void setNumCastles(int[] numCastles) {
		this.numCastles = numCastles;
	}
	
	public void addNumCastles(int index) {
		numCastles[index]++;
		addNumVictoryPoints(index);
		addNumVictoryPoints(index);
	}

	public int[] getNumLongestRoad() {
		return numLongestRoad;
	}
	
	public void addRoad(Road road) {
		roads.add(road);
		calculateLongestRoad();
	}
	

	public List<Road> getRoads() {
		return roads;
	}

	public int[][] getPlayerCards() {
		return playerCards;
	}

	public int[] getPlayerCardsTotal() {
		return playerCardsTotal;
	}

	public int getDie1() {
		return die1;
	}

	public int getDie2() {
		return die2;
	}
	
	public void rollDice() {
		die1 = ThreadLocalRandom.current().nextInt(6) + 1;
		die2 = ThreadLocalRandom.current().nextInt(6) + 1;
	}
}









