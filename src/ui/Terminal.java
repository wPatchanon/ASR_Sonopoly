package ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import logic.GameLogic;

public class Terminal extends Canvas {
	public static final double SIZE = BoardPane.CELL_WIDTH * BoardPane.SIDE;
	
	private GraphicsContext gc;
	
	public Terminal () {
		setWidth(SIZE);
		setHeight(SIZE);
		
		gc = getGraphicsContext2D();
		
		draw();
	}
	
	public void draw () {
		gc.clearRect(0, 0, SIZE, SIZE);
		
		gc.setFill(Color.gray(0.1));
		gc.fillRect(0, 0, SIZE, SIZE);
		
		if (GameLogic.getInstance().getGameStatus() == 0) {
			drawStart();
		} else if (GameLogic.getInstance().getGameStatus() == 1) {
			drawChooseColor();
		}
	}
	
	private void drawStart () {
		gc.setFill(Color.WHITESMOKE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(Font.font("TH Sarabun New", FontWeight.EXTRA_BOLD, 48));
		gc.fillText("START", SIZE / 2, 3 * SIZE / 4);
		
		gc.setStroke(Color.WHITESMOKE);
		gc.setLineWidth(2);
		gc.strokeRoundRect(SIZE / 4, 2 * SIZE / 3, SIZE / 2, SIZE / 6, 20, 20);
		
		setOnMouseClicked(event -> {
			if (event.getX() < SIZE / 4 || event.getX() > 3 * SIZE / 4) {
				return;
			}
			
			if (event.getY() < 2 * SIZE / 3 || event.getY() > 5 * SIZE / 6) {
				return;
			}
			
			clickStart();
		});
	}
	
	private void drawChooseColor () {
		int playerTurn = GameLogic.getInstance().getPlayerTurn();
		
		gc.setFill(Color.WHITESMOKE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(Font.font("TH Sarabun New", FontWeight.BOLD, 32));
		gc.fillText("Player #" + Integer.toString(playerTurn) + " chooses the color", 
				SIZE / 2, SIZE / 2);
		
		for (int i = 0; i < 4; ++i) {
			gc.setFill(BoardCell.COLOR[i]);
			gc.fillRect(SIZE / 6 + i * SIZE / 6, 2 * SIZE / 3, SIZE / 8, SIZE / 8);
		}
	}
	
	public void clickStart () {
		GameLogic.getInstance().setGameStatus(1);
	}
}
