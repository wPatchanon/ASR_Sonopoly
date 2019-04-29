package logic;

import ui.GamePaneManager;

public class GameLogic {
	private static final GameLogic INSTANCE = new GameLogic();

	private int gameStatus;		// 0 - start | 1 - select player | 2 - play
	private int playerTurn;
	private int[] colorMap;		// 0 - BLUE | 1 - GREEN | 2 - RED | 3 - YELLOW
	
	public GameLogic () {
		gameStatus = 0;
		playerTurn = 0;
		colorMap = new int[] {0, 1, 2, 3};
	}
	
	public void update () {
		GamePaneManager.getInstance().getTerminal().draw();
		
		if (gameStatus == 0) {
			
		} else if (gameStatus == 1) {
			
		}
	}
	
	public void changeTurn () {
		playerTurn = (playerTurn + 1) % 4;
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
	
	public int[] getColorMap () {
		return colorMap;
	}
	
	public static GameLogic getInstance () {
		return INSTANCE;
	}
}
