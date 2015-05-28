package MinesweeperMain;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanMain extends JPanel {

    PanGrid panGrid = new PanGrid(9, 9);
    PanOptions panOptions = new PanOptions();
    JFrame parent;

    public PanMain() {
        panGrid.parent = (this);
        panOptions.parent = (this);
        setLayout(new BorderLayout());
        add(panGrid, BorderLayout.CENTER);
        add(panOptions, BorderLayout.EAST);
        panOptions.nBombs = panGrid.BombNum;
        panOptions.SetBombLabel(panGrid.BombNum);
    }
}
