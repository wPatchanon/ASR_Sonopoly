package util;

import logic.GameLogic;
import ui.BoardPane;
import ui.GamePaneManager;

public class KeyMap {
	public void operate (int key) {
		switch (key) {
		case 0:
		case 12:
		case 13:
		case 14:
			break;
		case 1:
			GamePaneManager.getInstance().getTerminal().clickStart();
			break;
		case 2:
			GamePaneManager.getInstance().getTerminal().rollDice();
			break;
		case 3:
			GameLogic.getInstance().changeTurn();
			break;
		case 4:
			GameLogic.getInstance().lose(GameLogic.getInstance().getPlayerTurn());
			break;
		case 5:
		case 6:
			int position = GameLogic.getInstance().getPosition(GameLogic.getInstance().getPlayerTurn());
			GameLogic.getInstance().buy(position, GameLogic.getInstance().getAssetLevel(position) + 1);
			break;
		case 7:
		case 8:
		case 9:
		case 10:
			position = GameLogic.getInstance().getPosition(GameLogic.getInstance().getPlayerTurn());
			GameLogic.getInstance().sell(position, GameLogic.getInstance().getAssetLevel(position));
			break;
		case 11:
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
			break;
		}
	}
}
