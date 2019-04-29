package ui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import application.Main;

public class BoardPane extends GridPane {
	public static final int SIDE = 7;
	public static final double CELL_WIDTH = Main.HEIGHT / (SIDE + 4);
	
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
		for (int i = 0; i < 4 * SIDE; ++i) {
			BoardCell boardCell = new BoardCell(i / SIDE, i, (new Integer(i + 1)).toString());
			boardCell.setOwner(i / SIDE);
			
			GridPane.setHalignment(boardCell, HPos.CENTER);
			GridPane.setValignment(boardCell, VPos.CENTER);
			
			if (i < SIDE) {
				add(boardCell, SIDE - i, SIDE + 1);
			} else if (i < 2 * SIDE) {
				add(boardCell, 0, SIDE - (i - SIDE));
			} else if (i < 3 * SIDE) {
				add(boardCell, 1 + (i - 2 * SIDE), 0);
			} else {
				add(boardCell, SIDE + 1, 1 + (i - 3 * SIDE));
			}
		}
	}
}
