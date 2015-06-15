package MinesweeperMain;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class PanOptions extends JPanel {

    PanMain parent;
    JComboBox<String> cbDif;
    public static JComboBox<String> cbColour;
    JLabel lbBombs, lbTime, lbScore1, lbScore2, lbScore3, lbScoreHeader;
    public static int nBombs, nTimePlayed = 0;
    PrintWriter HSFile;
    Timer TimePassed;
    int highScore = 999, highScore2 = 999, highScore3 = 999;

    public PanOptions() {
//        if(HSFile)
//        try {
//            HSFile.println("The first line");
//            HSFile.println("The second line");
//            HSFile = new PrintWriter("HighScores.txt", "UTF-8");
//        } catch (IOException e) {
//            
//            
//        }
        TimePassed = new Timer(1000, TimePlayed);
        this.setLayout(new GridLayout(10, 1));
        JButton Flag = new JButton("Flag");
        JButton NewGame = new JButton("New Game");
        String[] Difchoices = {"Easy", "Medium", "Hard", "Extreme", "Insanity"};
        String[] Colchoices = {"Gray", "Blue", "Green", "Orange"};
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
        //Key map for activating Flag mode with F
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

        //Listenter For Flag Mode
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

        //Listener for new Game
        class NGListener implements ActionListener {

            NGListener(PanOptions parentOptions) {
                this.parentOptions = parentOptions;
            }
            public PanOptions parentOptions;

            @Override
            public void actionPerformed(ActionEvent event) {
                //Setting Difficulty for new Game
                parentOptions.TimePassed.stop();
                parentOptions.parent.panGrid.playSound("New_Game.wav");
                JButton NewGame = (JButton) event.getSource();
                parentOptions.parent.panGrid.KillGrid();
                parentOptions.parent.panGrid.BGC = getcolour();
                if (parentOptions.cbDif.getSelectedItem().equals("Easy")) {
                    parentOptions.parent.panGrid.BombNum = 10;
                    parentOptions.parent.panGrid.CreateGrid(9, 9);
                    parentOptions.GetScores("Easy.txt");
                    parentOptions.UpdateScoreLabel(highScore, highScore2, highScore3);
                } else if (parentOptions.cbDif.getSelectedItem().equals("Medium")) {
                    parentOptions.parent.panGrid.BombNum = 30;
                    parentOptions.parent.panGrid.CreateGrid(15, 15);
                    parentOptions.GetScores("Medium.txt");
                    parentOptions.UpdateScoreLabel(highScore, highScore2, highScore3);
                } else if (parentOptions.cbDif.getSelectedItem().equals("Hard")) {
                    parentOptions.parent.panGrid.BombNum = 50;
                    parentOptions.parent.panGrid.CreateGrid(20, 20);
                    parentOptions.GetScores("Hard.txt");
                    parentOptions.UpdateScoreLabel(highScore, highScore2, highScore3);
                } else if (parentOptions.cbDif.getSelectedItem().equals("Extreme")) {
                    parentOptions.parent.panGrid.BombNum = 125;
                    parentOptions.parent.panGrid.CreateGrid(20, 20);
                    parentOptions.GetScores("Extreme.txt");
                    parentOptions.UpdateScoreLabel(highScore, highScore2, highScore3);
                } else if (parentOptions.cbDif.getSelectedItem().equals("Insanity")) {
                    parentOptions.parent.panGrid.BombNum = 395;
                    parentOptions.parent.panGrid.CreateGrid(20, 20);
                    parentOptions.GetScores("Insanity.txt");
                    parentOptions.UpdateScoreLabel(highScore, highScore2, highScore3);
                }
            }
        }

        ActionListener FlagModeListener = new FlagModeListener();
        ActionListener NGListener = new NGListener(this);
        Flag.addActionListener(FlagModeListener);
        NewGame.addActionListener(NGListener);
    }

    //Setting amount of bombs left
    void SetBombLabel(int n) {
        try {
            nBombs = n;
            lbBombs.setText("There are " + Integer.toString(n) + " bombs left");
        } catch (Exception theCommonCold) {
            lbBombs = new JLabel("There are " + Integer.toString(n) + " bombs left");
            add(lbBombs);
        }
    }

    //updating amount of bombs left
    void UpdateBombLabel(int n) {
        nBombs += n;
        lbBombs.setText("There are " + Integer.toString(nBombs) + " bombs left");
    }

    void SetTimeLabel() {
        try {
            lbTime.setText("Time Played " + 0 + "Sec");
        } catch (Exception theCommonCold2) {
            lbTime = new JLabel("Time Played " + 0 + "Sec");
            add(lbTime);
        }
    }

    void UpdateTimeLabel(int nTime) {
        lbTime.setText("Time Played " + nTime + "Sec");
    }

    void SetScoreLabel(int Score1, int Score2, int Score3) {
        try {
            lbScore1.setText("1. " + Score1);
            lbScore2.setText("2. " + Score2);
            lbScore3.setText("3. " + Score3);
        } catch (Exception theCommonCold3) {
            lbScore1 = new JLabel("1. " + Score1);
            lbScore2 = new JLabel("2. " + Score2);
            lbScore3 = new JLabel("3. " + Score3);
            add(lbScore1);
            add(lbScore2);
            add(lbScore3);
        }
    }

    void UpdateScoreLabel(int Score1, int Score2, int Score3) {
        lbScore1.setText("1. " + Score1);
        lbScore2.setText("2. " + Score2);
        lbScore3.setText("3. " + Score3);
    }

    //Getting selected Tile Bacground Colour
    public static Color getcolour() {
        if (cbColour.getSelectedItem().equals("Blue")) {
            return (new Color(1, 97, 255));
        } else if (cbColour.getSelectedItem().equals("Green")) {
            return (new Color(13, 177, 17));
        } else if (cbColour.getSelectedItem().equals("Orange")) {
            return (Color.ORANGE);
        } else {
            return Color.LIGHT_GRAY;

        }
    }
    ActionListener TimePlayed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            nTimePlayed++;
            UpdateTimeLabel(nTimePlayed);
        }
    };

    void GetScores(String file) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) // read the score file line by line
            {
                try {
                    int score = Integer.parseInt(line.trim());   // parse each line as an int
                    if (score < highScore) // and keep track of the largest
                    {
                        highScore3 = highScore2;
                        highScore2 = highScore;
                        highScore = score;
                    } else if (score < highScore2) {
                        highScore3 = highScore2;
                        highScore2 = score;
                    } else if (score < highScore3) {
                        highScore3 = score;
                    }
                } catch (NumberFormatException e1) {
                    // ignore invalid scores
                    //System.err.println("ignoring invalid score: " + line);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ex) {
            System.err.println("ERROR reading scores from file");
        }
    }

    void AddScore(String file) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
            output.newLine();
            output.append(" + points");
            output.close();

        } catch (IOException ex1) {
            System.out.printf("ERROR writing score to file: %s\n", ex1);
        }
    }
}
