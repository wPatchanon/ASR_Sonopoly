package logic;

import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.KeyFrame;

import java.util.ArrayList;

import ui.BoardPane;
import ui.GamePaneManager;

public class GameLogic {
	private static final GameLogic INSTANCE = new GameLogic();

	private int[] playerMap;		// 0 - BLUE | 1 - GREEN | 2 - RED | 3 - YELLOW
	private int gameStatus;		// 0 - start | 1 - select player | 2 - play
	private int playerTurn;
	
	private boolean[] hasLost;
	private int[] position;
	private int[] stop;
	private int[] money;
	
	public GameLogic () {
		gameStatus = 0;
		playerTurn = 0;
//		playerMap = new int[] {4, 4, 4, 4};
		playerMap = new int[] {0, 1, 2, 3};
		
		hasLost = new boolean[] {false, false, false, false};
		position = new int[4];
		stop = new int[4];
		money = new int[] {3000, 3000, 3000, 3000};
	}
	
	public void update () {
		GamePaneManager.getInstance().getTerminal().draw();
		GamePaneManager.getInstance().getInformationPane().draw();
		
		if (gameStatus == 0) {
			
		} else if (gameStatus == 1) {
			
		} else if (gameStatus == 2) {
			GamePaneManager.getInstance().getPositionPane().draw();
		}
	}
	
	public void walk(int distance) {
		int destination = (position[playerTurn] + distance) % BoardPane.TOTAL_CELL;
		
		Timeline timeline = new Timeline();
		for (int i = 0; i < distance; ++i) {
			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.75 + i * 0.5), event -> {
				position[playerTurn] = (position[playerTurn] + 1) % BoardPane.TOTAL_CELL;
				GamePaneManager.getInstance().getPositionPane().draw();
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
//			checkTerminalState
		});
	}
	
	public void changeTurn () {
		playerTurn = (playerTurn + 1) % 4;
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
	
	public static GameLogic getInstance () {
		return INSTANCE;
	}
}
