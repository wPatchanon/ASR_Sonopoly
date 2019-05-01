package ui;

import java.util.Random;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import logic.GameLogic;

public class BoardCell extends Canvas {
	// Blue | Green | Red | Yellow | Gray
	public static final Color[] COLOR;
	
	private static final Image[][] ASSET;
	private static final Image[] IMAGE;
	
	private double width;
	private double height;
	private double bandWidth;
	
	private GraphicsContext gc;
	
	private boolean corner;
	private int cellId;
	private int cornerType;
	private int owner;
	private int assetLevel;		// 0 - Land | 1 - House | 2 - Hotel | 3 - Landmark
	
	static {
		COLOR = new Color[] {
				Color.rgb(73, 116, 209), 
				Color.rgb(1, 208, 117), 
				Color.rgb(253, 104, 105), 
				Color.rgb(253, 180, 50), 
				Color.GRAY
				};
		
		String path = "asset/";
		String extension = ".png";
		ASSET = new Image[4][4];
		for (int i = 0; i < 4; ++i) {
			String color = (i == 0 ? "blue" : i == 1 ? "green" : i == 2 ? "red" : "yellow") + "/";
			ASSET[i][0] = new Image(path + color + "land" + extension);
			ASSET[i][1] = new Image(path + color + "house" + extension);
			ASSET[i][2] = new Image(path + color + "hotel" + extension);
			ASSET[i][3] = new Image(path + color + "landmark" + extension);
		}
		
		path = "cell_icon/";
		IMAGE = new Image[] {
				new Image(path + "start" + extension),
				new Image(path + "robber" + extension),
				new Image(path + "plane" + extension),
				new Image(path + "prison" + extension)
				};
	}
	
	public BoardCell (int cellId, int cellOrientation) {
		this.cellId = cellId;
		this.corner = false;
//		this.owner = 4;
		this.owner = (new Random()).nextInt(4);
//		this.assetLevel = 0;
		this.assetLevel = (new Random()).nextInt(4);
		
		width = BoardPane.CELL_WIDTH;
		height = 2 * BoardPane.CELL_WIDTH;
		bandWidth = 0.2 * height;
		
		setWidth(width);
		setHeight(height);
		
		// 0 - Bottom | 1 - Left | 2 - Top | 3 - Right
		setRotate(cellOrientation * 90);
		
		gc = getGraphicsContext2D();
		
		drawCell();
	}
	
	public BoardCell (int cornerType) {
		this.cornerType = cornerType;
		this.corner = true;
		
		width = 2 * BoardPane.CELL_WIDTH;
		height = 2 * BoardPane.CELL_WIDTH;
		
		setWidth(width);
		setHeight(height);
		
		gc = getGraphicsContext2D();
		
		drawCorner();
	}
	
	public void draw () {
		gc.clearRect(0, 0, width, height);
		
		if (corner) {
			drawCorner();
		} else {
			drawCell();
		}
	}
	
	public void drawCell () {
		gc.setFill(Color.gray(0.9));
		gc.fillRect(0, 0, width, height);
		
		if (owner != 4) {
			gc.drawImage(ASSET[GameLogic.getInstance().getPlayerMap(owner)][assetLevel], 
					width / 2 - width / 3,
					2 * height / 3 - width / 3,
					2 * width / 3,
					2 * width / 3);
		}
		
		gc.setFill(Color.rgb(255, 255, 255, 0.7));
		gc.fillRect(0, 0, width, height);
		
		gc.setFill(owner == 4 ? COLOR[owner] : COLOR[GameLogic.getInstance().getPlayerMap(owner)]);
		gc.fillRect(0, 0, width, bandWidth);
		
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(0.5);
		gc.strokeRect(0, 0, width, height);
		gc.strokeRect(0, 0, width, bandWidth);
		
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("TH Sarabun New", 18));
		gc.setTextBaseline(VPos.CENTER);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText(Integer.toString(cellId + 1), width / 2, 1.5 * bandWidth);
	}
	
	public void drawCorner () {
		gc.drawImage(IMAGE[cornerType], 0, 0, width,  height);
		
		if (GameLogic.getInstance().getPlayerAtPosition(cornerType * BoardPane.SIDE + cornerType).size() > 0) {
//			gc.setFill(Color.rgb(0, 0, 0, 0.75));
			gc.setFill(Color.rgb(255, 255, 255, 0.5));
			gc.fillRect(0, 0, width, height);
		}
	}
	
	public void setOwner (int owner) {
		this.owner = owner;
		draw();
	}
}
