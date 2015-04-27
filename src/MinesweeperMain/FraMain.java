package MinesweeperMain;

import javax.swing.JFrame;

public class FraMain extends JFrame {

    PanMain panMain;
    FraMain(int width, int height) {
        panMain = new PanMain();
        panMain.parent = this;
        setSize(width, height);
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(panMain);
        setVisible(true);
    }
}
