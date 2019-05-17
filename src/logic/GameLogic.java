package logic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;

import ui.BoardPane;
import ui.GamePaneManager;
import util.Utility;

public class GameLogic {
	private static final GameLogic INSTANCE = new GameLogic();

	private int[] playerMap;		// 0 - BLUE | 1 - GREEN | 2 - RED | 3 - YELLOW
	private int gameStatus;			// 0 - start | 1 - select player | 2 - dice | 3 - show detail
									// 4 - penalty | 5 - pass | 6 - end
	private int playerTurn;
	
	// Player
	private boolean[] hasLost;
	private int[] position;
	private int[] stop;
	private int[] money;
	
	// Board
	private int[] owner;
	private int[] assetLevel;		// 0 - Land | 1 - House | 2 - Hotel | 3 - Landmark
	private int[][] buyPrice;
	private int[][] penaltyPrice;
	private int[][] takeoverPrice;
	private int[][] sellPrice;
	
	public GameLogic () {
		gameStatus = 0;
		playerTurn = 0;
		playerMap = new int[] {4, 4, 4, 4};
		
		hasLost = new boolean[] {false, false, false, false};
		position = new int[4];
		stop = new int[4];
		money = new int[] {3000, 3000, 3000, 3000};
		
		owner = new int[4 * BoardPane.SIDE + 4];
		assetLevel = new int[4 * BoardPane.SIDE + 4];
		buyPrice = new int[4 * BoardPane.SIDE + 4][4];
		penaltyPrice = new int[4 * BoardPane.SIDE + 4][4];
		takeoverPrice = new int[4 * BoardPane.SIDE + 4][3];
		sellPrice = new int[4 * BoardPane.SIDE + 4][4];
		
		for (int i = 0; i < 4 * BoardPane.SIDE + 4; ++i) {
			int idx = i - i / BoardPane.SIDE;
			
			owner[i] = 4;
			assetLevel[i] = 0;
			
			buyPrice[i][0] = Utility.roundTen(300 + 10 * idx);
			buyPrice[i][1] = Utility.roundTen(0.4 * buyPrice[i][0]);
			buyPrice[i][2] = Utility.roundTen(0.8 * buyPrice[i][0]);
			buyPrice[i][3] = Utility.roundTen(1.2 * buyPrice[i][0]);
			
			penaltyPrice[i][0] = Utility.roundTen(0.3 * buyPrice[i][0]);
			penaltyPrice[i][1] = Utility.roundTen(0.4 * (buyPrice[i][0] + buyPrice[i][1]));
			penaltyPrice[i][2] = Utility.roundTen(0.5 * (buyPrice[i][0] + buyPrice[i][1] + buyPrice[i][2]));
			penaltyPrice[i][3] = Utility.roundTen(
					0.5 * (buyPrice[i][0] + buyPrice[i][1] + buyPrice[i][2] + buyPrice[i][3]));
			
			takeoverPrice[i][0] = Utility.roundTen(1.3 * buyPrice[i][0]);
			takeoverPrice[i][1] = Utility.roundTen(1.3 * (buyPrice[i][0] + buyPrice[i][1]));
			takeoverPrice[i][2] = Utility.roundTen(1.3 * (buyPrice[i][0] + buyPrice[i][1] + buyPrice[i][2]));
			
			for (int j = 0; j < 4; ++j) {
				sellPrice[i][j] = Utility.roundTen(0.7 * buyPrice[i][j]);
			}
		}
	}
	
	public void update () {
		GamePaneManager.getInstance().getBoardPane().update();
		GamePaneManager.getInstance().getTerminal().draw();
		GamePaneManager.getInstance().getInformationPane().draw();
		
		if (gameStatus == 2) {
			GamePaneManager.getInstance().getPositionPane().draw();
		}
	}
	
	public void walk (int distance) {
		if (gameStatus != 2 || !GamePaneManager.getInstance().getTerminal().hasRolled()) {
			return;
		}
		int destination = (position[playerTurn] + distance) % BoardPane.TOTAL_CELL;
		
		Timeline timeline = new Timeline();
		for (int i = 0; i < distance; ++i) {
			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.75 + i * 0.5), event -> {
				position[playerTurn] = (position[playerTurn] + 1) % BoardPane.TOTAL_CELL;
				GamePaneManager.getInstance().getPositionPane().draw();
				if (position[playerTurn] == 0) {
					money[playerTurn] += 600;
				}
			}));
		}
		
		if (destination == 2 * BoardPane.SIDE + 2) {
			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2 + distance * 0.5), event -> {
				position[playerTurn] = 0;
				GamePaneManager.getInstance().getPositionPane().draw();
			}));
		}
		
		timeline.setCycleCount(1);
		timeline.play();
		
		timeline.setOnFinished(event -> {
			GamePaneManager.getInstance().getTerminal().setRolled(false);
			if (position[playerTurn] == 0) {
				// Start
				setGameStatus(5);
			} else if (position[playerTurn] == BoardPane.SIDE + 1) {
				// Robbed
				setGameStatus(4);
			} else if (position[playerTurn] == 3 * BoardPane.SIDE + 3) {
				// Prison
				stop[playerTurn] = 1;
				setGameStatus(5);
			} else if (owner[position[playerTurn]] == playerTurn || owner[position[playerTurn]] == 4) {
				// Buy or Takeover or trigger Sell
				setGameStatus(3);
			} else {
				// Penalty
				setGameStatus(4);
			}
		});
	}
	
	public void buy (int cellId, int inputAssetLevel) {
		if (gameStatus != 3) {
			return;
		}
		
		if (owner[cellId] != playerTurn && owner[cellId] != 4) {
			return;
		}
		
		int assetLevel;
		if (owner[cellId] != playerTurn) {
			assetLevel = 0;
		} else {
			if (this.assetLevel[cellId] == 3) {
				return;
			}
			assetLevel = this.assetLevel[cellId] + 1;
		}
		
		if (inputAssetLevel != assetLevel) {
			return;
		}
		
		if (money[playerTurn] < buyPrice[cellId][assetLevel]) {
			return;
		}
		
		owner[cellId] = playerTurn;
		this.assetLevel[cellId] = assetLevel;
		money[playerTurn] -= buyPrice[cellId][assetLevel];
		
		update();
	}
	
	public void takeover (int cellId) {
		if (gameStatus != 3) {
			return;
		}
		
		if (playerTurn == owner[cellId]) {
			return;
		}
		
		int assetLevel = this.assetLevel[cellId];
		if (assetLevel == 3) {
			return;
		}
		
		if (money[playerTurn] < takeoverPrice[cellId][assetLevel]) {
			return;
		}
		
		money[playerTurn] -= takeoverPrice[cellId][assetLevel];
		money[owner[cellId]] += takeoverPrice[cellId][assetLevel];
		owner[cellId] = playerTurn;
		
		update();
	}
	
	public void sell (int cellId, int inputAssetLevel) {
		if (gameStatus != 3) {
			return;
		}
		
		if (playerTurn != owner[cellId]) {
			return;
		}
		
		if (assetLevel[cellId] != inputAssetLevel) {
			return;
		}
		
		money[owner[cellId]] += sellPrice[cellId][assetLevel[cellId]];
		if (assetLevel[cellId] > 0) {
			--assetLevel[cellId];
		} else {
			owner[cellId] = 4;
		}
		
		update();
	}
	
	public void pay (int cellId, int cost) {
		if (gameStatus != 4) {
			return;
		}
		
		if (money[playerTurn] < cost) {
			return;
		}
		
		if (playerTurn == owner[cellId]){
			return;
		}
		
		if (cellId == BoardPane.SIDE + 1) {
			money[playerTurn] -= cost;
		} else {
			money[owner[cellId]] += cost;
			money[playerTurn] -= cost;
		}
		
		update();
	}
	
	public void changeTurn () {
		if (gameStatus != 1 && gameStatus != 3 && gameStatus != 5 && gameStatus != 2) {
			return;
		}
		
		System.out.println("Change Turn");
		GamePaneManager.getInstance().getTerminal().setAction(false);
		
		int survivor = 0;
		for (int i = 0; i < 4; ++i) {
			if (!hasLost[i]) {
				++survivor;
			}
		}
		
		if (survivor == 1) {
			setGameStatus(6);
			update();
			return;
		}
		
		while (true) {
			playerTurn = (playerTurn + 1) % 4;
			if (!hasLost[playerTurn]) {
				break;
			}
		}
		
		if (!GamePaneManager.getInstance().getTerminal().chooseColor()) {
			
		} else if (stop[playerTurn] > 0) {
			--stop[playerTurn];
			setGameStatus(5);
		} else {
			setGameStatus(2);
		}
		
		update();
	}
	
	public void addPlayerMap (int playerId) {
		playerMap[playerTurn] = playerId;
		update();
	}
	
	public void lose (int playerId) {
		hasLost[playerId] = true;
		
		update();
	}
	
	public void setGameStatus (int gameStatus) {
		System.out.println("State: " + gameStatus);
		this.gameStatus = gameStatus;
		update();
	}
	
	public int getGameStatus () {
		return gameStatus;
	}
	
	public int getPlayerTurn () {
		return playerTurn;
	}
	
	public int getPlayerMap (int playerId) {
		return playerMap[playerId];
	}
	
	public ArrayList<Integer> getPlayerAtPosition (int position) {
		ArrayList<Integer> playerList = new ArrayList<>();
		
		for (int i = 0; i < 4; ++i) {
			if (hasLost[i]) {
				continue;
			}
			
			if (this.position[i] == position) {
				playerList.add(i);
			}
		}
		
		return playerList;
	}
	
	public int getPosition (int playerId) {
		return position[playerId];
	}
	
	public int getMoney (int playerId) {
		return money[playerId];
	}
	
	public boolean hasLost (int playerId) {
		return hasLost[playerId];
	}
	
	public int getOwner (int cellId) {
		return owner[cellId];
	}
	
	public int getAssetLevel (int cellId) {
		return assetLevel[cellId];
	}
	
	public int getBuyPrice (int cellId, int assetLevel) {
		return buyPrice[cellId][assetLevel];
	}
	
	public int getPenaltyPrice (int cellId, int assetLevel) {
		return penaltyPrice[cellId][assetLevel];
	}
	
	public int getTakeoverPrice (int cellId, int assetLevel) {
		return takeoverPrice[cellId][assetLevel];
	}
	
	public int getSellPrice (int cellId, int assetLevel) {
		return sellPrice[cellId][assetLevel];
	}
	
	public static GameLogic getInstance () {
		return INSTANCE;
	}
}
