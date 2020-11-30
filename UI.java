import java.awt.EventQueue;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import java.util.Random;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle;
import java.awt.Component;
import javax.swing.GroupLayout;
import java.awt.LayoutManager;
import java.awt.GridLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JFrame;

// 
// Decompiled by Procyon v0.5.36
// 

public class UI extends JFrame
{
    Insets Margin;
    ElapsedTime time;
    JButton[][] button;
    int coorX;
    int coorY;
    int size;
    int move;
    private JButton btnNew;
    private JComboBox<String> cbSize;
    private JLabel jLabel3;
    private JLabel lbElapsedTime;
    private JLabel lbMoveCount;
    private JPanel myPanel;
    
    public UI() {
        this.Margin = new Insets(0, 0, 0, 0);
        this.time = null;
        this.initComponents();
        this.setLocation(50, 50);
        this.setMinimumSize(new Dimension(200, 600));
        this.setTitle("Number Puzzle");
    }
    
    private void initComponents() {
        this.btnNew = new JButton();
        this.lbMoveCount = new JLabel();
        this.lbElapsedTime = new JLabel();
        this.jLabel3 = new JLabel();
        this.cbSize = new JComboBox<String>();
        this.myPanel = new JPanel();
        this.setDefaultCloseOperation(3);
        this.btnNew.setText("New Game");
        this.btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                UI.this.btnNewActionPerformed(evt);
            }
        });
        this.lbMoveCount.setText("Move Count: ");
        this.lbElapsedTime.setText("Elapsed Time:");
        this.jLabel3.setText("Size:");
        this.cbSize.setModel(new DefaultComboBoxModel<String>(new String[] { "3x3", "4x4" }));
        this.myPanel.setLayout(new GridLayout(1, 0));
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(31, 31, 31).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jLabel3).addGap(26, 26, 26).addComponent(this.cbSize, -2, -1, -2)).addComponent(this.lbElapsedTime).addComponent(this.lbMoveCount).addComponent(this.btnNew)).addContainerGap(348, 32767)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.myPanel, -1, -1, 32767).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(this.myPanel, -1, 354, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.lbMoveCount).addGap(18, 18, 18).addComponent(this.lbElapsedTime).addGap(18, 18, 18).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel3).addComponent(this.cbSize, -2, -1, -2)).addGap(41, 41, 41).addComponent(this.btnNew).addGap(32, 32, 32)));
        this.pack();
    }
    
    private void btnNewActionPerformed(final ActionEvent evt) {
        String[] arr = null;
        this.move = 0;
        this.lbMoveCount.setText("Move Count: " + this.move);
        this.timeCounting();
        this.size = this.cbSize.getSelectedIndex() + 3;
        this.button = new JButton[this.size][this.size];
        arr = this.createArray();
        do {
            this.shuffle(arr);
        } while (!this.isSolvable(arr));
        this.drawBoard(arr);
    }
    
    public boolean isSolvable(final String[] arr) {
        int inversion = 0;
        boolean blankRow = false;
        for (int i = 0; i < this.size * this.size - 1; ++i) {
            if (!arr[i].equals("")) {
                final int iCell = Integer.parseInt(arr[i]);
                for (int j = i + 1; j < this.size * this.size; ++j) {
                    if (!arr[j].equals("")) {
                        final int jCell = Integer.parseInt(arr[j]);
                        if (iCell > jCell) {
                            ++inversion;
                        }
                    }
                }
            }
            else {
                final int floorValue = i / this.size;
                blankRow = (floorValue % 2 == 0);
            }
        }
        return (this.size == 3 && inversion % 2 == 0) || (this.size == 4 && blankRow && inversion % 2 != 0) || (this.size == 4 && !blankRow && inversion % 2 == 0);
    }
    
    public String[] createArray() {
        final String[] arr = new String[this.size * this.size];
        for (int i = 0; i < this.size * this.size - 1; ++i) {
            arr[i] = String.valueOf(i + 1);
        }
        arr[this.size * this.size - 1] = "";
        return arr;
    }
    
    public void timeCounting() {
        if (this.time != null) {
            this.time.stopThread();
        }
        (this.time = new ElapsedTime()).start();
    }
    
    public void drawBoard(final String[] arr) {
        this.myPanel.removeAll();
        this.myPanel.setLayout(new GridLayout(this.size, this.size));
        for (int i = 0; i < this.size; ++i) {
            for (int j = 0; j < this.size; ++j) {
                if (this.button[i][j] == null) {
                    this.button[i][j] = new JButton();
                }
                this.button[i][j].setSize(500, 500);
                this.button[i][j].setMargin(this.Margin);
                this.button[i][j].setText(arr[i * this.size + j]);
                if (this.button[i][j].getText().equals("")) {
                    this.coorX = i;
                    this.coorY = j;
                }
                this.button[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        final JButton clickedButton = (JButton)e.getSource();
                        if (!clickedButton.getText().equals("") && !UI.this.button[UI.this.coorX][UI.this.coorY].getText().equals("WIN")) {
                            for (int i = 0; i < UI.this.size; ++i) {
                                for (int j = 0; j < UI.this.size; ++j) {
                                    if (clickedButton == UI.this.button[i][j] && UI.this.checkMove(i, j)) {
                                        UI.this.moving(i, j);
                                        if (UI.this.checkWinCon()) {
                                            UI.this.gameFinish();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                this.myPanel.add(this.button[i][j]);
            }
        }
    }
    
    public boolean checkMove(final int i, final int j) {
        final int result = Math.abs(this.coorX - i) + Math.abs(this.coorY - j);
        return result == 1;
    }
    
    public void moving(final int i, final int j) {
        final String tmp = this.button[i][j].getText();
        this.button[i][j].setText(this.button[this.coorX][this.coorY].getText());
        this.button[this.coorX][this.coorY].setText(tmp);
        this.coorX = i;
        this.coorY = j;
        ++this.move;
        this.lbMoveCount.setText("Move Count: " + this.move);
    }
    
    public boolean checkWinCon() {
        int count = 0;
        for (int i = 0; i < this.size; ++i) {
            for (int j = 0; j < this.size; ++j) {
                ++count;
                if (!this.button[i][j].getText().equals("") && !this.button[i][j].getText().equals("WIN")) {
                    final int currentNum = Integer.parseInt(this.button[i][j].getText());
                    if (count != currentNum) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public void gameFinish() {
        if (this.time != null) {
            this.time.stopThread();
            final int finishTime = this.time.getFinishTime() - 1;
            JOptionPane.showMessageDialog(null, "You have finished the game in " + finishTime + " seconds with " + this.move + " moves!!!");
        }
        this.button[this.coorX][this.coorY].setText("WIN");
        this.button[this.coorX][this.coorY].setBackground(Color.green);
    }
    
    public void shuffle(final String[] arr) {
        final Random rd = new Random();
        for (int i = 0; i < arr.length; ++i) {
            final int index = rd.nextInt(this.size * this.size);
            final String temp = arr[i];
            arr[i] = arr[index];
            arr[index] = temp;
        }
    }
    
    public static void main(final String[] args) {
        try {
            for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex2) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (IllegalAccessException ex3) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex3);
        }
        catch (UnsupportedLookAndFeelException ex4) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex4);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UI().setVisible(true);
            }
        });
    }
    
    public class ElapsedTime extends Thread
    {
        int count;
        boolean exit;
        
        public ElapsedTime() {
            this.exit = false;
        }
        
        @Override
        public void run() {
            while (!this.exit) {
                UI.this.lbElapsedTime.setText("Elapsed Time: " + this.count + " s");
                ++this.count;
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException ex) {
                    Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        public void stopThread() {
            this.exit = true;
        }
        
        public int getFinishTime() {
            return this.count;
        }
    }
}
