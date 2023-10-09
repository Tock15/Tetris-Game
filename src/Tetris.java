import javax.swing.*;

public class Tetris
{
    private static StartUpForm sf;
    private static LeaderboardForm lb;
    private static GameWindow gw;
    private static PuzzleForm pf;
    private static final AudioPlayer a = new AudioPlayer();

    public static void startGame()
    {
        gw.setVisible(true);
        gw.startGame();
    }
    public static void end(int score)
    {
        String playerName = JOptionPane.showInputDialog("Enter your name: ");
        lb.addPlayer(playerName, score);
        lb.setVisible(true);
        gw.setVisible(false);
    }
    public static void endSpecial(int score)
    {
        String playerName = JOptionPane.showInputDialog("You’ve defied the odds and beat the game\nEnter your name: ");
        lb.addPlayer(playerName, score);
        lb.setVisible(true);
        gw.setVisible(false);
    }

    public static void playClear()
    {
        a.playClearSound();
    }
    public static void playLose()
    {
        a.playLoseSound();
    }
    public static void playMove()
    {
        a.playMoveSound();
    }
    public static void playRotate()
    {
        a.playRotateSound();
    }
    public static void playHardDrop()
    {
        a.playHardDropSound();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run()
            {
                gw = new GameWindow();
                pf = new PuzzleForm();
                sf = new StartUpForm();
                lb = new LeaderboardForm();

                sf.setVisible(true);
            }
        });

    }

    public static void showLeaderboard()
    {
        lb.setVisible(true);
    }
    public static void showStartUp() {
        sf.setVisible(true);
    }
    public static void showPuzzleForm(){
        pf.setVisible(true);
    }
}
