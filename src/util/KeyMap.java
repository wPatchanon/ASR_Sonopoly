package util;

import javafx.application.Platform;
import logic.GameLogic;
import ui.BoardPane;
import ui.GamePaneManager;

public class KeyMap {
	public static void operate (int key) {
		Platform.runLater(() -> {
			System.out.println("-------- Transcription: " + key);
			switch (key) {
			case 0:
			case 12:
			case 13:
			case 14:
				break;
			case 1:
				if (GameLogic.getInstance().getGameStatus() != 1) {
					break;
				}
				GamePaneManager.getInstance().getTerminal().clickStart();
				break;
			case 2:
				GamePaneManager.getInstance().getTerminal().setRolled(true);
				GamePaneManager.getInstance().getTerminal().rollDice();
				break;
			case 3:
				GameLogic.getInstance().changeTurn();
				break;
			case 4:
				GameLogic.getInstance().lose(GameLogic.getInstance().getPlayerTurn());
				GameLogic.getInstance().changeTurn();
				break;
			case 5:
			case 6:
				if (GamePaneManager.getInstance().getTerminal().actionDone()) {
					break;
				}
				int position = GameLogic.getInstance().getPosition(GameLogic.getInstance().getPlayerTurn());
				GamePaneManager.getInstance().getTerminal().setAction(true);
				GameLogic.getInstance().buy(position, GameLogic.getInstance().getAssetLevel(position) + 1);
				GameLogic.getInstance().buy(position, GameLogic.getInstance().getAssetLevel(position));
				break;
			case 7:
			case 8:
			case 9:
			case 10:
				if (GamePaneManager.getInstance().getTerminal().actionDone()) {
					break;
				}
				position = GameLogic.getInstance().getPosition(GameLogic.getInstance().getPlayerTurn());
				GameLogic.getInstance().sell(position, GameLogic.getInstance().getAssetLevel(position));
				break;
			case 11:
				if (GamePaneManager.getInstance().getTerminal().actionDone()) {
					break;
				}
				position = GameLogic.getInstance().getPosition(GameLogic.getInstance().getPlayerTurn());
				GameLogic.getInstance().takeover(position);
				break;
			case 15:
				position = GameLogic.getInstance().getPosition(GameLogic.getInstance().getPlayerTurn());
				int assetLevel = GameLogic.getInstance().getAssetLevel(position);
				int cost;
				if (position != BoardPane.SIDE + 1) {
					cost = GameLogic.getInstance().getPenaltyPrice(position, assetLevel);
				} else {
					cost = 300;
				}
				
				GameLogic.getInstance().pay(position, cost);
				if (position != BoardPane.SIDE + 1) {
					GameLogic.getInstance().setGameStatus(3);
				} else {
					GameLogic.getInstance().setGameStatus(2);
				}
				break;
			}
		});
	}
}
