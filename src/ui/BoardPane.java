package ui;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import application.Main;
import Listener.Listener_v2;
import util.KeyMap;

public class BoardPane extends GridPane {
	public static final int SIDE = 7;
	public static final int TOTAL_CELL = 4 * SIDE + 4;
	public static final double CELL_WIDTH = Main.HEIGHT / (SIDE + 4);
	
	private BoardCell[] boardCellArray;
	
	public BoardPane () {
		getColumnConstraints().add(new ColumnConstraints(2 * CELL_WIDTH));
		for (int i = 0; i < SIDE; ++i) {
			getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
		}
		getColumnConstraints().add(new ColumnConstraints(2 * CELL_WIDTH));
		
		getRowConstraints().add(new RowConstraints(2 * CELL_WIDTH));
		for (int i = 0; i < SIDE; ++i) {
			getRowConstraints().add(new RowConstraints(CELL_WIDTH));
		}
		getRowConstraints().add(new RowConstraints(2 * CELL_WIDTH));
		
		setGridLinesVisible(true);
		
		addDefaultCell();
	}
	
	private void addDefaultCell () {
		boardCellArray = new BoardCell[4*SIDE + 4];
		
		for (int i = 0; i < 4 * SIDE; ++i) {
			BoardCell boardCell;
			
			if (i % SIDE == 0) {
				boardCell = new BoardCell(i / SIDE);
				
				GridPane.setHalignment(boardCell, HPos.CENTER);
				GridPane.setValignment(boardCell, VPos.CENTER);
				GridPane.setMargin(boardCell, new Insets(0));
				
				if (i / SIDE == 0) {
					add(boardCell, SIDE + 1, SIDE + 1);
				} else if (i / SIDE == 1) {
					add(boardCell, 0, SIDE + 1);
				} else if (i / SIDE == 2) {
					add(boardCell, 0, 0);
				} else {
					add(boardCell, SIDE + 1, 0);
				}
				
				boardCellArray[i + i / SIDE] = boardCell;
			}
			
			boardCell = new BoardCell(i, i / SIDE);
			
			GridPane.setHalignment(boardCell, HPos.CENTER);
			GridPane.setValignment(boardCell, VPos.CENTER);
			GridPane.setMargin(boardCell, new Insets(0));
			
			if (i < SIDE) {
				add(boardCell, SIDE - i, SIDE + 1);
			} else if (i < 2 * SIDE) {
				add(boardCell, 0, SIDE - (i - SIDE));
			} else if (i < 3 * SIDE) {
				add(boardCell, 1 + (i - 2 * SIDE), 0);
			} else {
				add(boardCell, SIDE + 1, 1 + (i - 3 * SIDE));
			}
			
			boardCellArray[i + i / SIDE + 1] = boardCell;
		}
	}
	
	public void update () {
		for (BoardCell each: boardCellArray) {
			Platform.runLater(() -> each.draw());
		}
	}
}
