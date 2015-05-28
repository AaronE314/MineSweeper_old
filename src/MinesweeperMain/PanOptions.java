package MinesweeperMain;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanOptions extends JPanel {

    PanMain parent;
    JComboBox<String> cb;
    JLabel lbBombs;
    public static int nBombs;

    public PanOptions() {
        this.setLayout(new GridLayout(10, 1));
        JButton Flag = new JButton("Flag");
        JButton NewGame = new JButton("New Game");
        String[] choices = {"Easy", "Medium", "Hard"};
        cb = new JComboBox<String>(choices);
        cb.setVisible(true);
        Flag.setBackground(Color.RED);
        add(Flag);
        add(cb);
        add(NewGame);

        class FlagModeListener implements ActionListener {

            boolean Flagon = false;

            @Override
            public void actionPerformed(ActionEvent event) {
                JButton Flag = (JButton) event.getSource();
                if (!Flagon) {
                    Flag.setBackground(Color.GREEN);
                    Flagon = true;
                    parent.panGrid.Flagon = true;
                } else {
                    Flag.setBackground(Color.RED);
                    Flagon = false;
                    parent.panGrid.Flagon = false;
                }
            }
        }
        class NGListener implements ActionListener {

            NGListener(PanOptions parentOptions) {
                this.parentOptions = parentOptions;
            }
            public PanOptions parentOptions;

            @Override
            public void actionPerformed(ActionEvent event) {
                JButton NewGame = (JButton) event.getSource();
                parentOptions.parent.panGrid.KillGrid();
                if (parentOptions.cb.getSelectedItem().equals("Easy")) {
                    parentOptions.parent.panGrid.BombNum = 10;
                    parentOptions.parent.panGrid.CreateGrid(9, 9);
                } else if (parentOptions.cb.getSelectedItem().equals("Medium")) {
                    parentOptions.parent.panGrid.BombNum = 30;
                    parentOptions.parent.panGrid.CreateGrid(15, 15);
                } else if (parentOptions.cb.getSelectedItem().equals("Hard")) {
                    parentOptions.parent.panGrid.BombNum = 50;
                    parentOptions.parent.panGrid.CreateGrid(20, 20);
                }
            }
        }

        ActionListener FlagModeListener = new FlagModeListener();
        ActionListener tempNGListener = new NGListener(this);
        Flag.addActionListener(FlagModeListener);
        NewGame.addActionListener(tempNGListener);
    }

    void SetBombLabel(int n) {
        try {
            nBombs = n;
            lbBombs.setText("There are " + Integer.toString(n) + " bombs left");
        } catch (Exception theCommonCold) {
            lbBombs = new JLabel("There are " + Integer.toString(n) + " bombs left");
            add(lbBombs);
        }
    }

    void UpdateBombLabel(int n) {
        nBombs += n;
        lbBombs.setText("There are " + Integer.toString(nBombs) + " bombs left");
    }
}
