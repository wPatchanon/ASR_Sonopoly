package ui;

import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

import application.Main;
import logic.GameLogic;

public class PositionPane extends Canvas {
	private static final Image[] IMAGE;
	private static final double ICON_WIDTH = BoardPane.CELL_WIDTH / 4;
	private static final double  ICON_HEIGHT = BoardPane.CELL_WIDTH / 2;
	
	private GraphicsContext gc;
	
	static {
		String path = "player_icon/";
		String extension = ".png";
		IMAGE = new Image[] {
				new Image(path + "blue" + extension),
				new Image(path + "green" + extension),
				new Image(path + "red" + extension),
				new Image(path + "yellow" + extension)
				};
	}
	
	public PositionPane () {
		setWidth(Main.HEIGHT);
		setHeight(Main.HEIGHT);
		
		gc = getGraphicsContext2D();
	}
	
	public void draw () {
		gc.clearRect(0, 0, Main.HEIGHT, Main.HEIGHT);
		
		ArrayList<Integer> playerList;
		double row;
		double column;
		int position;
		Image image;
		
		// corner
		for (int i = 0; i < 4; ++i)
		{
			int lastRow = 1 - i / 2;
			int lastCol = 1 - Math.min(1, i % 3);
			playerList = GameLogic.getInstance().getPlayerAtPosition(i * BoardPane.SIDE + i);
			
			for (int j = 0; j < playerList.size(); ++j) {
				row = getRow(j, playerList.size());
				column = getColumn(j, playerList.size());
				image = IMAGE[GameLogic.getInstance().getPlayerMap(playerList.get(j))];
				
				gc.drawImage(image, 
						(lastCol * (BoardPane.SIDE + 2) + 0.75 + column * 0.5) * BoardPane.CELL_WIDTH - ICON_WIDTH / 2, 
						(lastRow * (BoardPane.SIDE + 2) + 0.7 + row * 0.6) * BoardPane.CELL_WIDTH - ICON_HEIGHT / 2,
						ICON_WIDTH,
						ICON_HEIGHT);
			}
		}
		
		// vertical cell
		for (int i = 1; i <= BoardPane.SIDE; ++i) {
			playerList = GameLogic.getInstance().getPlayerAtPosition(i);
			for (int j = 0; j < playerList.size(); ++j) {
				row = getRow(j, playerList.size());
				column = getColumn(j, playerList.size());
				position = BoardPane.SIDE + 1 - GameLogic.getInstance().getPosition(playerList.get(j));
				image = IMAGE[GameLogic.getInstance().getPlayerMap(playerList.get(j))];
				
				gc.drawImage(image, 
						((1 + 0.3 + position) + column * 0.4) * BoardPane.CELL_WIDTH - ICON_WIDTH / 2, 
						(BoardPane.SIDE + 2 + 1 + row * 0.6) * BoardPane.CELL_WIDTH - ICON_HEIGHT / 2,
						ICON_WIDTH,
						ICON_HEIGHT);
			}
		}
		
		for (int i = 2 * BoardPane.SIDE + 3; i <= 3 * BoardPane.SIDE + 2; ++i) {
			playerList = GameLogic.getInstance().getPlayerAtPosition(i);
			for (int j = 0; j < playerList.size(); ++j) {
				row = getRow(j, playerList.size());
				column = getColumn(j, playerList.size());
				position = GameLogic.getInstance().getPosition(playerList.get(j)) - 2 * BoardPane.SIDE - 2;
				image = IMAGE[GameLogic.getInstance().getPlayerMap(playerList.get(j))];
				
				gc.drawImage(image, 
						((1 + 0.3 + position) + column * 0.4) * BoardPane.CELL_WIDTH - ICON_WIDTH / 2, 
						(0.35 + row * 0.6) * BoardPane.CELL_WIDTH - ICON_HEIGHT / 2, 
						ICON_WIDTH,
						ICON_HEIGHT);
			}
		}
		
		// horizontal cell
		for (int i = BoardPane.SIDE + 2; i <= 2 * BoardPane.SIDE + 1; ++i) {
			playerList = GameLogic.getInstance().getPlayerAtPosition(i);
			for (int j = 0; j < playerList.size(); ++j) {
				column = (j - (playerList.size() - 1) / 2.0) * 0.3;
				row = 2 * BoardPane.SIDE + 1 - i;
				image = IMAGE[GameLogic.getInstance().getPlayerMap(playerList.get(j))];
				
				gc.drawImage(image, 
						(0.65 + column) * BoardPane.CELL_WIDTH - ICON_WIDTH / 2, 
						(row + 2 + 0.5) * BoardPane.CELL_WIDTH - ICON_HEIGHT / 2,
						ICON_WIDTH,
						ICON_HEIGHT);
			}
		}
		
		for (int i = 3 * BoardPane.SIDE + 4; i <= 4 * BoardPane.SIDE + 3; ++i) {
			playerList = GameLogic.getInstance().getPlayerAtPosition(i);
			for (int j = 0; j < playerList.size(); ++j) {
				column = (j - (playerList.size() - 1) / 2.0) * 0.3;
				row = i - 3 * BoardPane.SIDE - 4;
				image = IMAGE[GameLogic.getInstance().getPlayerMap(playerList.get(j))];
				
				gc.drawImage(image, 
						(BoardPane.SIDE + 2 + 1.35 + column) * BoardPane.CELL_WIDTH - ICON_WIDTH / 2, 
						(row + 2 + 0.5) * BoardPane.CELL_WIDTH - ICON_HEIGHT / 2,
						ICON_WIDTH,
						ICON_HEIGHT);
			}
		}
	}
	
	private double getRow (int index, int size) {
		return size == 3 ? (index + 1) / 2 : 0.5 + 0.5 * ((size - 1) / 2) * (Math.signum(index - 1.5));
	}
	
	private double getColumn (int index, int size) {
		return size == 3 ?
				0.5 + Math.signum(index) * (index - 1.5): 0.5 + Math.signum(size / 2) * ((index % 2) - 0.5);
	}
}
