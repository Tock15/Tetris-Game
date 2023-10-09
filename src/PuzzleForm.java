import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class PuzzleForm extends JFrame
{
    private JPanel panel1;
    private JButton level1Button;
    private JButton level2Button;
    private JButton level3Button;
    private JButton level4Button;
    private JButton level5Button;
    private JButton returnButton;
    private static PuzzleWindow pw;

    public PuzzleForm(){
        this.add(panel1);
        this.setSize(475, 600);
        this.setLocation(400, 100);
        this.setResizable(false);
        this.setBackground(new Color(35, 35, 35));

        level1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                startPuzzle(1);
            }
        });
        level2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                startPuzzle(2);
            }
        });
        level3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                startPuzzle(3);
            }
        });
        level4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                startPuzzle(4);
            }
        });
        level5Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                startPuzzle(5);
            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Tetris.showStartUp();
            }
        });
        /*returnButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setVisible(false);
                    Tetris.showStartUp();
                }
            }
        });*/
    }

    public static void startPuzzle(int level)
    {
        pw = new PuzzleWindow(level);
        pw.setVisible(true);
        pw.startGame();
    }
}
