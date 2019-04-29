package ui;

import javafx.scene.paint.Color;

public class GamePaneManager {
	private static final GamePaneManager INSTANCE = new GamePaneManager();
	
	private BoardPane boardPane;
	private Terminal terminal;
	private InformationPane informationPane;
	
	private GamePaneManager () {
		boardPane = new BoardPane();
		terminal = new Terminal();
		informationPane = new InformationPane();
	}
	
	public BoardPane getBoardPane () {
		return boardPane;
	}
	
	public Terminal getTerminal () {
		return terminal;
	}
	
	public InformationPane getInformationPane() {
		return informationPane;
	}
	
	public static GamePaneManager getInstance () {
		return INSTANCE;
	}
}
