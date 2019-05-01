package ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.Random;

import logic.GameLogic;

public class Terminal extends Canvas {
	public static final double SIZE = BoardPane.CELL_WIDTH * BoardPane.SIDE;
	
	private static final Image[] DICE_IMAGE;
	public static final Image LOGO;
	
	private GraphicsContext gc;
	
	private boolean hoverStatus;
	
	private boolean rolled;
	private int firstDiceSide;
	private int secondDiceSide;
	
	static {
		String path = "dice/";
		String extension = ".png";
		DICE_IMAGE = new Image[] {
				new Image(path + "1" + extension), 
				new Image(path + "2" + extension), 
				new Image(path + "3" + extension), 
				new Image(path + "4" + extension), 
				new Image(path + "5" + extension), 
				new Image(path + "6" + extension)
		};
		
		LOGO = new Image("logo/logo.png");
	}
	
	public Terminal () {
		setWidth(SIZE);
		setHeight(SIZE);
		
		gc = getGraphicsContext2D();
		
		draw();
	}
	
	public void rollDice () {
		Timeline timeline = new Timeline();
		
		for (int i = 0; i < 15; ++i) {
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50 * i), event -> {
				drawDice();
				}));
		}
		
		timeline.setCycleCount(1);
		timeline.play();
		
		timeline.setOnFinished(event -> {
			GameLogic.getInstance().walk(firstDiceSide + secondDiceSide);
		});
	}
	
	public void draw () {
		gc.clearRect(0, 0, SIZE, SIZE);
		
		gc.setFill(Color.gray(0.25));
		gc.fillRect(0, 0, SIZE, SIZE);
		
		if (GameLogic.getInstance().getGameStatus() == 0) {
			drawStartTerminal();
		} else if (GameLogic.getInstance().getGameStatus() == 1) {
			drawChooseColorTerminal();
		} else if (GameLogic.getInstance().getGameStatus() == 2) {
			rolled = false;
			drawDiceTerminal();
		}
	}
	
	private void drawStartTerminal () {
		gc.drawImage(LOGO, 2 * SIZE / 7, SIZE / 8, 3 * SIZE / 7, 3 * SIZE / 7);
		
		drawStartButton(false);
		
		setOnMouseMoved(event -> {
			if (isInStartButton(event.getX(), event.getY())) {
				if (!hoverStatus) {
					hoverStatus = true;
					drawStartButton(hoverStatus);
				}
			} else {
				if (hoverStatus) {
					hoverStatus = false;
					drawStartButton(hoverStatus);
				}
			}
		});
		
		setOnMouseClicked(event -> {
			if (!isInStartButton(event.getX(), event.getY())) {
				return;
			}
			
			clickStart();
		});
	}
	
	private void drawStartButton (boolean hover) {
		gc.setFill(hover ? Color.gray(0.5) : Color.gray(0.25));
		gc.fillRoundRect(SIZE / 4, 2 * SIZE / 3, SIZE / 2, SIZE / 6, 20, 20);
		
		gc.setFill(Color.WHITESMOKE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(Font.font("TH Sarabun New", FontWeight.EXTRA_BOLD, 48));
		gc.fillText("START", SIZE / 2, 3 * SIZE / 4);
		
		gc.setStroke(Color.WHITESMOKE);
		gc.setLineWidth(2);
		gc.strokeRoundRect(SIZE / 4, 2 * SIZE / 3, SIZE / 2, SIZE / 6, 20, 20);
	}
	
	private boolean isInStartButton (double x, double y) {
		if (x < SIZE / 4 || x > 3 * SIZE / 4) {
			return false;
		}
		
		if (y < 2 * SIZE / 3 || y > 5 * SIZE / 6) {
			return false;
		}
		
		return true;
	}
	
	private void drawChooseColorTerminal () {
		int playerTurn = GameLogic.getInstance().getPlayerTurn();
		
		gc.setFill(Color.WHITESMOKE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(Font.font("TH Sarabun New", FontWeight.BOLD, 48));
		gc.fillText("Choose color", SIZE / 2, SIZE / 3);
		
		for (int i = 0; i < 4; ++i) {
			gc.setFill(BoardCell.COLOR[i]);
			for (int j = 0; j < GameLogic.getInstance().getPlayerTurn(); ++j) {
				if (GameLogic.getInstance().getPlayerMap(j) == i) {
					gc.setFill(Color.GRAY);
					break;
				}
			}
			gc.fillRect(SIZE / 6 + i * 13 * SIZE / 72, SIZE / 2, SIZE / 8, SIZE / 8);
		}
		
		setOnMouseClicked(event -> {
			int block = -1;
			for (int i = 0; i < 4; ++i) {
				if (event.getX() >= SIZE / 6 + i * 13 * SIZE / 72 &&
						event.getX() <= SIZE / 6 + i * 13 * SIZE / 72 + SIZE / 8) {
					block = i;
					break;
				}
			}
			
			if (block == -1) {
				return;
			}
			
			if (event.getY() < SIZE / 2 || event.getY() > SIZE / 2 + SIZE / 8) {
				return;
			}
			
			for (int i = 0; i < GameLogic.getInstance().getPlayerTurn(); ++i) {
				if (GameLogic.getInstance().getPlayerMap(i) == block) {
					return;
				}
			}
			
			GameLogic.getInstance().addPlayerMap(block);
			GameLogic.getInstance().changeTurn();
			
			if (GameLogic.getInstance().getPlayerTurn() == 0) {
				GameLogic.getInstance().setGameStatus(2);
			}
		});
	}
	
	private void drawDiceTerminal () {
		gc.setFill(Color.WHITESMOKE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(Font.font("TH Sarabun New", FontWeight.BOLD, 44));
		gc.fillText("Roll the dice", SIZE / 2, SIZE / 3);
		
		drawDice();
		
		setOnMouseClicked(event -> {
			if (!rolled) {
				rolled = true;
				rollDice();
			}
		});
	}
	
	private void drawDice () {
		int tmpDiceSide;
		while (true) {
			tmpDiceSide = (new Random()).nextInt(6) + 1;
			if (firstDiceSide != tmpDiceSide) {
				firstDiceSide = tmpDiceSide;
				break;
			}
		}
		while (true) {
			tmpDiceSide = (new Random()).nextInt(6) + 1;
			if (secondDiceSide != tmpDiceSide) {
				secondDiceSide = tmpDiceSide;
				break;
			}
		}
		
		gc.drawImage(DICE_IMAGE[firstDiceSide - 1], 1 * SIZE / 4, SIZE / 2, SIZE / 5, SIZE / 5);
		gc.drawImage(DICE_IMAGE[secondDiceSide - 1], 11 * SIZE / 20, SIZE / 2, SIZE / 5, SIZE / 5);
	}
	
	public void clickStart () {
		GameLogic.getInstance().setGameStatus(1);
		setOnMouseMoved(event -> {});
	}
}
