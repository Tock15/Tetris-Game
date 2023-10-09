import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.SortOrder;


public class LeaderboardForm extends JFrame
{


    private JPanel panel1;
    private JTable table1;
    private JButton menuButton;
    private JButton clearButton;
    private DefaultTableModel dtm;
    private String leaderboardFile = "leaderboard";
    private TableRowSorter<TableModel> sorter;

    public LeaderboardForm()
    {
        this.add(panel1);
        this.setSize(475, 600);
        this.setLocation(400, 100);
        this.setResizable(false);

        createTable();
        initSorter();
        menuButton.addActionListener(e -> {
            setVisible(false);
            Tetris.showStartUp();
        });
        clearButton.addActionListener(e -> {
            clearLeaderboard();

        });


    }
    private void createTable() {
        dtm = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return Integer.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table1.setModel(dtm);

        Vector<String> columns = new Vector<>();
        columns.add("Name");
        columns.add("Score");

        try {
            FileInputStream fs = new FileInputStream(leaderboardFile);
            ObjectInputStream is = new ObjectInputStream(fs);

            dtm.setDataVector((Vector) is.readObject(), columns);

            is.close();
            fs.close();

        } catch (Exception e) {}
    }

    private void initSorter()
    {
        sorter = new TableRowSorter<>(dtm);
        table1.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }
    public void addPlayer(String name, int score)
    {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.addRow(new Object[]{name, score});
        saveLeaderboard();
    }
    private void saveLeaderboard()
    {
        try {
            FileOutputStream fs = new FileOutputStream(leaderboardFile);
            ObjectOutputStream os = new ObjectOutputStream(fs);

            os.writeObject( dtm.getDataVector());
            os.close();
            fs.close();
        } catch (Exception e) {}
    }
    public void clearLeaderboard() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        saveLeaderboard();
    }
}