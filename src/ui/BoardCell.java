package ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import logic.GameLogic;

public class BoardCell extends Canvas {
	// Blue | Green | Red | Yellow | Gray
	public static final Color[] COLOR = new Color[] {
			Color.rgb(73, 116, 209), 
			Color.rgb(1, 208, 117), 
			Color.rgb(253, 104, 105), 
			Color.rgb(253, 180, 50), 
			Color.GRAY
			};
	
	private double width;
	private double height;
	private double bandWidth;
	
	private GraphicsContext gc;
	
	private int cellOrientation;	// 0 - Bottom | 1 - Left | 2 - Top | 3 - Right
	private int cellNumber;
	private String cellName;
	private int owner;
	
	public BoardCell (int cellOrientation, int cellNumber, String cellName) {
		this.cellNumber = cellNumber;
		this.cellName = cellName;
		this.cellOrientation = cellOrientation;
		this.owner = 4;
		
		width = BoardPane.CELL_WIDTH;
		height = 2 * BoardPane.CELL_WIDTH;
		bandWidth = 0.2 * height;
		
		setWidth(width);
		setHeight(height);
		
		setRotate(cellOrientation * 90);
		
		gc = getGraphicsContext2D();
		
		draw();
	}
	
	public void draw () {
		gc.clearRect(0, 0, width, height);
		
		gc.setFill(Color.ALICEBLUE);
		gc.fillRect(0, 0, width, height);
		
		gc.setFill(owner == 4 ? COLOR[owner] : COLOR[GameLogic.getInstance().getColorMap()[owner]]);
		gc.fillRect(0, 0, width, bandWidth);
		
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(0.5);
		gc.strokeRect(0, 0, width, height);
		gc.strokeRect(0, 0, width, bandWidth);
		
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("TH Sarabun New", 18));
		gc.setTextBaseline(VPos.CENTER);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText(cellName, width / 2, 1.5 * bandWidth);
	}
	
	public void setOwner (int owner) {
		this.owner = owner;
		draw();
	}
}
