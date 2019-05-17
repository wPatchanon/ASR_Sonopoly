package ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.Random;

import Listener.Listener_v2;
import logic.GameLogic;
import util.KeyMap;
import util.Utility;

public class Terminal extends Canvas {
	public static final double SIZE = BoardPane.CELL_WIDTH * BoardPane.SIDE;
	
	private static final Image[] DICE_IMAGE;
	private static final Image FLAG;
	public static final Image LOGO;
	
	private GraphicsContext gc;
	
	private boolean chooseColor;
	
	private boolean hoverStatus;
	
	private boolean hasRolled;
	private int firstDiceSide;
	private int secondDiceSide;
	
	private boolean actionDone;
	
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
		
		FLAG = new Image("cell_icon/flag.png");
		LOGO = new Image("logo/logo.png");
	}
	
	public Terminal () {
		setWidth(SIZE);
		setHeight(SIZE);
		
		chooseColor = false;
		
		gc = getGraphicsContext2D();
		
		draw();
	}
	
	// General draw
	public void draw () {
		gc.clearRect(0, 0, SIZE, SIZE);
		
		gc.setFill(Color.gray(0.25));
		gc.fillRect(0, 0, SIZE, SIZE);
		
		if (GameLogic.getInstance().getGameStatus() == 0) {
			drawStartTerminal();
		} else if (GameLogic.getInstance().getGameStatus() == 1) {
			drawChooseColorTerminal();
		} else if (GameLogic.getInstance().getGameStatus() == 2) {
			drawDiceTerminal();
		} else if (GameLogic.getInstance().getGameStatus() == 3) {
			drawDetailTerminal();
		} else if (GameLogic.getInstance().getGameStatus() == 4) {
			drawPayment();
		} else if (GameLogic.getInstance().getGameStatus() == 5) {
			drawPass();
		} else {
			drawEnd();
		}
	}
	
	// GameStatus = 0
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
	
	public void clickStart () {
		GameLogic.getInstance().setGameStatus(1);
		setOnMouseMoved(event -> {});
	}
	
	// GameStatus = 1
	private void drawChooseColorTerminal () {
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
			
			if (GameLogic.getInstance().getPlayerTurn() == 3) {
				chooseColor = true;
			}
			
			GameLogic.getInstance().changeTurn();
		});
	}
	
	// GameStatus = 2
	private void drawDiceTerminal () {
		gc.drawImage(FLAG, SIZE / 40 + 18 * SIZE / 20, SIZE / 40, SIZE / 20, SIZE / 10);
		
		gc.setFill(Color.WHITESMOKE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(Font.font("TH Sarabun New", FontWeight.BOLD, 44));
		gc.fillText("Roll the dice", SIZE / 2, SIZE / 3);
		
		drawDice();
		
		setOnMouseClicked(event -> {
			if (event.getX() >= SIZE / 40 + 18 * SIZE / 20 && event.getY() <= SIZE / 40 + SIZE / 10) {
				GameLogic.getInstance().lose(GameLogic.getInstance().getPlayerTurn());
				GameLogic.getInstance().changeTurn();
			} else if (!hasRolled) {
				hasRolled = true;
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
//			GameLogic.getInstance().walk(1);
		});
	}
	
	// GameStatus = 3
	private void drawDetailTerminal () {
		int playerTurn = GameLogic.getInstance().getPlayerTurn();
		int playerTurnMoney = GameLogic.getInstance().getMoney(playerTurn);
		int cellId = GameLogic.getInstance().getPosition(playerTurn);
		int owner = GameLogic.getInstance().getOwner(cellId);
		int assetLevel = GameLogic.getInstance().getAssetLevel(cellId);
		
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(Color.WHITESMOKE);
		
		gc.setFont(Font.font("TH Sarabun New", FontWeight.BOLD, 28));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText("Land Information", SIZE / 2, SIZE / 6);
		
		gc.setFont(Font.font("TH Sarabun New", 28));
		gc.setTextAlign(TextAlignment.LEFT);
		gc.fillText("Land:", SIZE / 8, 2 * SIZE / 5);
		gc.fillText("House:", SIZE / 8, 2 * SIZE / 5 + SIZE / 10);
		gc.fillText("Hotel:", SIZE / 8, 2 * SIZE / 5 + 2 * SIZE / 10);
		gc.fillText("Landmark:", SIZE / 8, 2 * SIZE / 5 + 3 * SIZE / 10);
		
		gc.setTextAlign(TextAlignment.RIGHT);
		gc.fillText("Price", SIZE / 2 + SIZE / 40, 7 * SIZE / 24);
		gc.fillText("Penalty", SIZE / 2 + SIZE / 4, 7 * SIZE / 24);
		gc.fillText("Sell", SIZE / 2 + 2 * SIZE / 5, 7 * SIZE / 24);
		
		for (int i = 0; i < 4; ++i) {
			int buyPrice = GameLogic.getInstance().getBuyPrice(cellId, i);
			int penaltyPrice = GameLogic.getInstance().getPenaltyPrice(cellId, i);
			int sellPrice = GameLogic.getInstance().getSellPrice(cellId, i);
			
			gc.fillText(Utility.commaNumber(buyPrice), SIZE / 2 + SIZE / 40, 2 * SIZE / 5 + i * SIZE / 10);
			gc.fillText(Utility.commaNumber(penaltyPrice), 
					SIZE / 2 + 9 * SIZE / 40, 2 * SIZE / 5 + i * SIZE / 10);
			gc.fillText(Utility.commaNumber(sellPrice), SIZE / 2 + 2 * SIZE / 5, 2 * SIZE / 5 + i * SIZE / 10);
		}
		
		int buyMode = playerTurn == owner ? 0 : owner == 4 ? 1 : 2;
		String buyButton = buyMode == 0 ? "Upgrade" : buyMode == 1 ? "Buy" : "Takeover"; 
		
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setLineWidth(2);
		
		int actionAssetLevel;
		boolean canBuy;
		boolean canSell = playerTurn == owner;
		if (buyMode == 0) {
			actionAssetLevel = assetLevel + 1;
		} else {
			actionAssetLevel = assetLevel;
		}
		
		if (buyMode < 2) {
			if (actionAssetLevel <= 3) {
				canBuy = playerTurnMoney >= GameLogic.getInstance().getBuyPrice(cellId, actionAssetLevel);
			} else {
				canBuy = false;
			}
		} else {
			if (actionAssetLevel == 3) {
				canBuy = false;
			} else {
				canBuy = playerTurnMoney >= GameLogic.getInstance().getTakeoverPrice(cellId, actionAssetLevel);
			}
		}
		
		if (!canBuy || actionDone) {
			gc.setFill(Color.gray(0.5));
			gc.fillRoundRect(SIZE / 16, 4 * SIZE / 5, SIZE / 4, SIZE / 8, 20, 20);
			gc.setFill(Color.gray(0.25));
		} else {
			gc.setFill(Color.WHITESMOKE);
		}
		
		gc.setFont(Font.font("TH Sarabun New", 28));
		gc.strokeRoundRect(SIZE / 16, 4 * SIZE / 5, SIZE / 4, SIZE / 8, 20, 20);
		gc.fillText(buyButton, 3 * SIZE / 16, 69 * SIZE / 80);
		
		if (!canSell || actionDone) {
			gc.setFill(Color.gray(0.5));
			gc.fillRoundRect(2 * SIZE / 16 + SIZE / 4, 4 * SIZE / 5, SIZE / 4, SIZE / 8, 20, 20);
			gc.setFill(Color.gray(0.25));
		} else {
			gc.setFill(Color.WHITESMOKE);
		}
		
		gc.strokeRoundRect(2 * SIZE / 16 + SIZE / 4, 4 * SIZE / 5, SIZE / 4, SIZE / 8, 20, 20);
		gc.fillText("Sell", SIZE / 2, 69 * SIZE / 80);
		
		if (buyMode == 2) {
			if (assetLevel <= 3) {
				int takeoverPrice = GameLogic.getInstance().getTakeoverPrice(cellId, actionAssetLevel);
				
				gc.setFont(Font.font("TH Sarabun New", 18));
				gc.setFill(Color.WHITESMOKE);
				gc.fillText("$ " + Utility.commaNumber(takeoverPrice), 3 * SIZE / 16, 72 * SIZE / 80);
			}
		} else {
			gc.setFont(Font.font("TH Sarabun New", 28));
		}
		
		gc.setFill(Color.WHITESMOKE);
		gc.strokeRoundRect(3 * SIZE / 16 + 2 * SIZE / 4, 4 * SIZE / 5, SIZE / 4, SIZE / 8, 20, 20);
		gc.fillText("Pass", 13 * SIZE / 16, 69 * SIZE / 80);
		
		setOnMouseClicked(event -> {
			double x = event.getX();
			double y = event.getY();
			
			// In Buy/Takeover
			if (x >= SIZE / 16 && x <= SIZE / 16 + SIZE / 4 &&
					y >= 4 * SIZE / 5 && y <= 4 * SIZE / 5 + SIZE / 8) {
				if (canBuy && !actionDone) {
					actionDone = true;
					if (buyMode < 2) {
						GameLogic.getInstance().buy(cellId, actionAssetLevel);
					} else {
						if (assetLevel < 3) {
							GameLogic.getInstance().takeover(cellId);
						}
					}
				}
			}
			
			// In Sell
			if (x >= 2 * SIZE / 16 + SIZE / 4 && x <= 2 * SIZE / 16 + SIZE / 4 + SIZE / 4 &&
					y >= 4 * SIZE / 5 && y <= 4 * SIZE / 5 + SIZE / 8) {
				if (!actionDone) {
					actionDone = true;
					GameLogic.getInstance().sell(cellId, actionAssetLevel);
				}
			}
			
			// In Pass
			if (x >= 3 * SIZE / 16 + 2 * SIZE / 4 && x <= 3 * SIZE / 16 + 2 * SIZE / 4 + SIZE / 4 &&
					y >= 4 * SIZE / 5 && y <= 4 * SIZE / 5 + SIZE / 8) {
				GameLogic.getInstance().changeTurn();
			}
		});
	}
	
	// GameStatus = 4
	private void drawPayment () {
		int playerTurn = GameLogic.getInstance().getPlayerTurn();
		int playerTurnMoney = GameLogic.getInstance().getMoney(playerTurn);
		int cellId = GameLogic.getInstance().getPosition(playerTurn);
		int assetLevel = GameLogic.getInstance().getAssetLevel(cellId);
		int cost;
		
		if (cellId != BoardPane.SIDE + 1) {
			cost = GameLogic.getInstance().getPenaltyPrice(cellId, assetLevel);
		} else {
			cost = 300;
		}
		
		gc.setFill(Color.WHITESMOKE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(Font.font("TH Sarabun New", FontWeight.BOLD, 44));
		gc.fillText("Payment", SIZE / 2, SIZE / 4);
		
		gc.setFont(Font.font("TH Sarabun New", 100));
		gc.fillText("$ " + Utility.commaNumber(cost), SIZE / 2, SIZE / 2);
		
		boolean canPay = playerTurnMoney >= cost;
		
		if (!canPay) {
			gc.setFill(Color.gray(0.5));
			gc.fillRoundRect(SIZE / 2 - SIZE / 4 - SIZE / 16, 11 * SIZE / 16, SIZE / 4, SIZE / 8, 20, 20);
			gc.setFill(Color.gray(0.25));
		} else {
			gc.setFill(Color.WHITESMOKE);
		}
		
		gc.setFont(Font.font("TH Sarabun New", 28));
		gc.strokeRoundRect(SIZE / 2 - SIZE / 4 - SIZE / 16, 11 * SIZE / 16, SIZE / 4, SIZE / 8, 20, 20);
		gc.fillText("Pay", 5 * SIZE / 16, 3 * SIZE / 4);
		
		gc.setFill(Color.WHITESMOKE);
		gc.strokeRoundRect(SIZE / 2 + SIZE / 16, 11 * SIZE / 16, SIZE / 4, SIZE / 8, 20, 20);
		gc.fillText("Surrender", 11 * SIZE / 16, 3 * SIZE / 4);
		
		setOnMouseClicked(event -> {
			double x = event.getX();
			double y = event.getY();
			
			if (x >= SIZE / 2 - SIZE / 4 - SIZE / 16 && x <= SIZE / 2 - SIZE / 16 &&
					y >= 11 * SIZE / 16 - SIZE / 16 && y <= 11 * SIZE / 16 + SIZE / 16)
			{
				if (!canPay) {
					return;
				}
				
				GameLogic.getInstance().pay(cellId, cost);
				if (cellId != BoardPane.SIDE + 1) {
					GameLogic.getInstance().setGameStatus(3);
				} else {
					GameLogic.getInstance().setGameStatus(2);
				}
			}
			
			if (x >= SIZE / 2 + SIZE / 16 && x <= SIZE / 2 + SIZE / 16 + SIZE / 4 &&
					y >= 11 * SIZE / 16 - SIZE / 16 && y <= 11 * SIZE / 16 + SIZE / 16)
			{
				GameLogic.getInstance().lose(playerTurn);
				GameLogic.getInstance().changeTurn();
			}
		});
	}
	
	private void drawPass () {
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(Font.font("TH Sarabun New", FontWeight.EXTRA_BOLD, 100));
		gc.setFill(Color.WHITESMOKE);
		gc.fillText("Pass", SIZE / 2, SIZE / 2);
		
		setOnMouseClicked(event -> {
			GameLogic.getInstance().changeTurn();
		});
	}
	
	private void drawEnd () {
		int winnerColor = 0;
		for (int i = 0; i < 4; ++i) {
			if (!GameLogic.getInstance().hasLost(i)) {
				winnerColor = GameLogic.getInstance().getPlayerMap(i);
				break;
			}
		}
		
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(Font.font("TH Sarabun New", FontWeight.EXTRA_BOLD, 48));
		
		String winnerText = "";
		switch (winnerColor) {
		case 0:
			winnerText = "BLUE";
			break;
		case 1:
			winnerText = "GREEN";
			break;
		case 2:
			winnerText = "RED";
			break;
		case 3:
			winnerText = "YELLOW";
			break;
		}
		
		gc.setFill(BoardCell.COLOR[winnerColor]);
		gc.fillText(winnerText + " PLAYER WINS", SIZE / 2, SIZE / 2);
		
		setOnMouseClicked(event -> {
			Platform.exit();
		});
	}
	
	public void setChooseColor (boolean chooseColor) {
		this.chooseColor = chooseColor;
	}
	
	public void setAction (boolean actionDone) {
		this.actionDone = actionDone;
	}
	
	public void setRolled (boolean hasRolled) {
		this.hasRolled = hasRolled;
	}
	
	public boolean hasRolled () {
		return hasRolled;
	}
	
	public boolean actionDone () {
		return actionDone;
	}
	
	public boolean chooseColor () {
		return chooseColor;
	}
}
