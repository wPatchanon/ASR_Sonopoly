package application;
	
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;

import ui.*;

public class Main extends Application {
	private static final double SCREEN_WIDTH;
	private static final double SCREEN_HEIGHT;
	
	public static final double WIDTH;
	public static final double HEIGHT;
	
	static {
		Rectangle2D screenBound = Screen.getPrimary().getBounds();
		SCREEN_WIDTH = screenBound.getWidth();
		SCREEN_HEIGHT = screenBound.getHeight();
		
		HEIGHT = Math.min(0.75 * SCREEN_HEIGHT, 0.6 * SCREEN_WIDTH);
		WIDTH = HEIGHT * 1.4;
	}
	
	@Override
	public void start (Stage primaryStage) {
		StackPane root = new StackPane();
		
		HBox gamePane = new HBox();
		
		StackPane leftPane = new StackPane();
		
		BoardPane boardPane = GamePaneManager.getInstance().getBoardPane();
		PositionPane positionPane = GamePaneManager.getInstance().getPositionPane();
		Terminal terminal = GamePaneManager.getInstance().getTerminal();
		
		leftPane.getChildren().add(boardPane);
		leftPane.getChildren().add(positionPane);
		leftPane.getChildren().add(terminal);
		
		InformationPane informationPane = GamePaneManager.getInstance().getInformationPane();
		
		gamePane.getChildren().add(leftPane);
		gamePane.getChildren().add(informationPane);
		
		root.getChildren().add(gamePane);
		
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.setTitle("GOWAJEE Monopoly - ASR Project");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main (String[] args) {
		launch(args);
	}
}
