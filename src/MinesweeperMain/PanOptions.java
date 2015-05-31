package MinesweeperMain;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class PanOptions extends JPanel {

    PanMain parent;
    JComboBox<String> cbDif;
    public static JComboBox<String> cbColour;
    JLabel lbBombs;
    public static int nBombs;

    public PanOptions() {
        this.setLayout(new GridLayout(10, 1));
        JButton Flag = new JButton("Flag");
        JButton NewGame = new JButton("New Game");
        String[] Difchoices = {"Easy", "Medium", "Hard"};
        String[] Colchoices = {"Gray", "Blue", "Green"};
        cbDif = new JComboBox<String>(Difchoices);
        cbColour = new JComboBox<String>(Colchoices);
        cbDif.setVisible(true);
        cbColour.setVisible(true);
        this.setFocusable(true);
        Flag.setBackground(Color.RED);
        add(Flag);
        add(cbDif);
        add(cbColour);
        add(NewGame);
        InputMap im = Flag.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = Flag.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "clickMe");
        am.put("clickMe", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton btn = (JButton) e.getSource();
                btn.doClick();
            }
        });

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
                parentOptions.parent.panGrid.playSound("New_Game.wav");
                JButton NewGame = (JButton) event.getSource();
                parentOptions.parent.panGrid.KillGrid();
                parentOptions.parent.panGrid.BGC = getcolour();
                if (parentOptions.cbDif.getSelectedItem().equals("Easy")) {
                    parentOptions.parent.panGrid.BombNum = 10;
                    parentOptions.parent.panGrid.CreateGrid(9, 9);
                } else if (parentOptions.cbDif.getSelectedItem().equals("Medium")) {
                    parentOptions.parent.panGrid.BombNum = 30;
                    parentOptions.parent.panGrid.CreateGrid(15, 15);
                } else if (parentOptions.cbDif.getSelectedItem().equals("Hard")) {
                    parentOptions.parent.panGrid.BombNum = 50;
                    parentOptions.parent.panGrid.CreateGrid(20, 20);
                }
            }
        }

        ActionListener FlagModeListener = new FlagModeListener();
        ActionListener NGListener = new NGListener(this);
        Flag.addActionListener(FlagModeListener);
        NewGame.addActionListener(NGListener);
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

    public static Color getcolour() {
        if (cbColour.getSelectedItem().equals("Blue")) {
            return (new Color(1, 97, 255));
        } else if (cbColour.getSelectedItem().equals("Green")) {
            return (new Color(13, 177, 17));
        } else {
            return Color.LIGHT_GRAY;
        }
    }

}
