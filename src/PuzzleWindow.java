import javax.swing.*;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public class PuzzleWindow extends JFrame
{
    private final PuzzleArea pa;

    public PuzzleWindow(int level)
    {
        pa = new PuzzleArea(level);
        this.add(pa);
        this.setSize(475, 600);
        this.setLocation(400, 100);
        this.setResizable(false);
        initControls();
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
        im.put(KeyStroke.getKeyStroke('1'), "spawnI");
        im.put(KeyStroke.getKeyStroke('2'), "spawnJ");
        im.put(KeyStroke.getKeyStroke('3'), "spawnL");
        im.put(KeyStroke.getKeyStroke('4'), "spawnZ");
        im.put(KeyStroke.getKeyStroke('5'), "spawnS");
        im.put(KeyStroke.getKeyStroke('6'), "spawnT");
        im.put(KeyStroke.getKeyStroke('7'), "spawnO");
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "return");

        am.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.moveRight();
            }
        });
        am.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.moveLeft();
            }
        });
        am.put("softDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.softDrop();
            }
        });
        am.put("hardDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.hardDrop();
            }
        });
        am.put("CCW", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.rotateCCW();
            }
        });
        am.put("CW", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.rotateCW();
            }
        });
        am.put("180", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.rotate180();
            }
        });
        am.put("spawnI", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.spawnBlock(0);
            }
        });
        am.put("spawnJ", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.spawnBlock(1);
            }
        });
        am.put("spawnL", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.spawnBlock(2);
            }
        });
        am.put("spawnZ", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.spawnBlock(3);
            }
        });
        am.put("spawnS", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.spawnBlock(4);
            }
        });
        am.put("spawnT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.spawnBlock(5);
            }
        });
        am.put("spawnO", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pa.spawnBlock(6);
            }
        });
        am.put("return", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Tetris.showPuzzleForm();
            }
        });
    }

    public void startGame()
    {
        new PuzzleThread(pa).start();
    }
}