import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartUpForm extends JFrame {
    private JButton gameButton;
    private JPanel panel1;
    private JButton leaderboardButton;
    private JButton quitButton;
    private JLabel title;
    private JLabel author;
    private JButton puzzleButton;

    public StartUpForm(){
        this.add(panel1);
        this.setSize(475, 600);
        this.setLocation(400, 100);
        this.setResizable(false);


        gameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Tetris.startGame();
            }
        });
        puzzleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Tetris.showPuzzleForm();
            }
        });

        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Tetris.showLeaderboard();

            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);

            }
        });

    }
}
