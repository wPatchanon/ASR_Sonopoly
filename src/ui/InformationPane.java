package ui;

import application.Main;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class InformationPane extends Canvas {
	public static final double WIDTH = Main.WIDTH - Main.HEIGHT;
	public static final double HEIGHT = Main.HEIGHT;
	
	public InformationPane () {
		setWidth(WIDTH);
		setHeight(HEIGHT);
		
		draw();
	}
	
	public void draw () {
		GraphicsContext gc = getGraphicsContext2D();
		
		gc.setFill(Color.NAVY);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
	}
	
	
}
