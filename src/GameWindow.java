import javax.swing.*;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public class GameWindow extends JFrame
{
    private final GameArea ga;
    private JPanel panel1;
    private JLabel scoreDisplay;

    public GameWindow()
    {
        ga = new GameArea();
        this.add(ga);
        this.setSize(475, 600);
        this.setLocation(400, 100);
        this.setResizable(false);
        initControls();
        scoreDisplay = new JLabel("Score: 0");
        panel1 = new JPanel();
        panel1.add(scoreDisplay);

    }

    private void initControls()
    {
        InputMap im = this.getRootPane().getInputMap();
        ActionMap am = this.getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        im.put(KeyStroke.getKeyStroke("LEFT"), "left");
        im.put(KeyStroke.getKeyStroke("DOWN"), "softDrop");
        im.put(KeyStroke.getKeyStroke(' '), "hardDrop");
        im.put(KeyStroke.getKeyStroke('z'), "CCW");
        im.put(KeyStroke.getKeyStroke('x'), "CW");
        im.put(KeyStroke.getKeyStroke('a'), "180");
        im.put(KeyStroke.getKeyStroke('c'), "hold");
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "terminate");

        am.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.moveRight();
            }
        });
        am.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.moveLeft();
            }
        });
        am.put("softDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.softDrop();
            }
        });
        am.put("hardDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.hardDrop();
            }
        });
        am.put("CCW", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.rotateCCW();

            }
        });
        am.put("CW", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.rotateCW();
            }
        });
        am.put("180", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.rotate180();
            }
        });
        am.put("hold", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.hold();
            }
        });
        am.put("terminate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public void startGame()
    {
        new GameThread(ga).start();
    }
}