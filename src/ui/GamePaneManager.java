package ui;

public class GamePaneManager {
	private static final GamePaneManager INSTANCE = new GamePaneManager();
	
	private BoardPane boardPane;
	private PositionPane positionPane;
	private Terminal terminal;
	private InformationPane informationPane;
	
	private GamePaneManager () {
		boardPane = new BoardPane();
		positionPane = new PositionPane();
		terminal = new Terminal();
		informationPane = new InformationPane();
	}
	
	public BoardPane getBoardPane () {
		return boardPane;
	}
	
	public PositionPane getPositionPane () {
		return positionPane;
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
