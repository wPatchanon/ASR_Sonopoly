package ui;

import javafx.scene.image.Image;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import application.Main;
import logic.GameLogic;
import util.Utility;

public class InformationPane extends Canvas {
	public static final double WIDTH = Main.WIDTH - Main.HEIGHT;
	public static final double HEIGHT = Main.HEIGHT;
	
	private static final Image[] AVATAR;
	
	static {
		String path = "avatar/";
		String extension = ".png";
		AVATAR = new Image[] {
				new Image(path + "blue" + extension),
				new Image(path + "green" + extension),
				new Image(path + "red" + extension),
				new Image(path + "yellow" + extension)
		};
	}
	
	public InformationPane () {
		setWidth(WIDTH);
		setHeight(HEIGHT);
		
		draw();
	}
	
	public void draw () {
		GraphicsContext gc = getGraphicsContext2D();
		
		gc.setFill(Color.gray(0.25));
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		
		gc.drawImage(Terminal.LOGO, 3 * WIDTH / 10, WIDTH / 10, 2 * WIDTH / 5, 2 * WIDTH / 5);
		
		for (int i = 0; i < 4; ++i) {
			if (GameLogic.getInstance().hasLost(i)) {
				continue;
			}
			
			if (GameLogic.getInstance().getPlayerMap(i) == 4) {
				continue;
			}
			
			if (GameLogic.getInstance().getPlayerTurn() == i) {
				gc.setFill(Color.gray(0.75));
				gc.fillRect(0, 7 * HEIGHT / 36 + WIDTH / 10 + i * HEIGHT / 9, WIDTH, HEIGHT / 9);
			}
			
			gc.drawImage(AVATAR[GameLogic.getInstance().getPlayerMap(i)], 
					WIDTH / 7, HEIGHT / 4 + i * HEIGHT / 9, WIDTH / 5, WIDTH / 5);
			
			gc.setFill(Color.WHITESMOKE);
			gc.setTextBaseline(VPos.CENTER);
			
			gc.setFont(Font.font("TH Sarabun New", FontWeight.BOLD, 36));
			gc.setTextAlign(TextAlignment.LEFT);
			gc.fillText("$", 2 * WIDTH / 5, HEIGHT / 4 + i * HEIGHT / 9 + WIDTH / 10);
			
			gc.setFont(Font.font("TH Sarabun New", 36));
			gc.setTextAlign(TextAlignment.RIGHT);
			gc.fillText(Utility.commaNumber(GameLogic.getInstance().getMoney(i)), 
					4 * WIDTH / 5, HEIGHT / 4 + i * HEIGHT / 9 + WIDTH / 10);
		}
	}
}
