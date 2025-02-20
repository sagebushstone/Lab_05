import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RockPaperScissorsFrame extends JFrame implements Strategy {
    JPanel mainPnl;
    JPanel playPnl, buttonPnl;
    JButton rockBtn, paperBtn, scissorsBtn, quitBtn;
    ImageIcon rockIcon, paperIcon, scissorsIcon, quitIcon;

    JPanel statsPnl;
    JTextField userWinFld, compWinFld, tieFld, totalFld;
    int userWins, compWins, ties = 0;

    // String list with rock index 0, paper index 1, scissors index 2
    String[] playOptions = {"Rock", "Paper", "Scissors"};
    String[] defeatOptions = {" crushes ", " covers ", " cuts "};
    int[] userPlayFreqs = {0, 0, 0};
    ArrayList<Integer> userPlays = new ArrayList<>();

    JPanel outputPnl;
    JTextArea outputTA;
    JScrollPane scroller;
    JScrollPane outerScroller;

    /**
     * creates the main panel and subpanels for the JFrame
     */
    public RockPaperScissorsFrame(){
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());
        createPlayPanel();
        createStatsPanel();
        createOutputPanel();

        mainPnl.add(playPnl, BorderLayout.NORTH);
        mainPnl.add(statsPnl, BorderLayout.CENTER);
        mainPnl.add(outputPnl, BorderLayout.SOUTH);

        outerScroller = new JScrollPane(mainPnl);

        setSize(800, 550);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);

        setTitle("Sage Bushstone Rock Paper Scissors");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(outerScroller);
        setVisible(true);

    }

    /**
     * Create the play panel (contains the buttons with rock/paper/scissors and the
     * actionlisteners for those buttons)
     */
    public void createPlayPanel(){
        playPnl = new JPanel();
        buttonPnl = new JPanel();
        playPnl.setLayout(new BorderLayout());
        buttonPnl.setLayout(new GridLayout(1, 4, 5, 5));
        playPnl.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10),  new EtchedBorder()));


        rockIcon = new ImageIcon("src/fist_hand.png");
        paperIcon = new ImageIcon("src/paper_hand.png");
        scissorsIcon = new ImageIcon("src/scissors_hand.png");
        quitIcon = new ImageIcon("src/quit_icon.png");
        rockBtn = new JButton(rockIcon);
        paperBtn = new JButton(paperIcon);
        scissorsBtn = new JButton(scissorsIcon);
        quitBtn = new JButton("Quit", quitIcon);
        quitBtn.addActionListener((ActionEvent ae) -> System.exit(0));

        rockBtn.addActionListener((ActionEvent ae) -> playRockPaperScissors(0));
        paperBtn.addActionListener((ActionEvent ae) -> playRockPaperScissors(1));
        scissorsBtn.addActionListener((ActionEvent ae) -> playRockPaperScissors(2));

        JLabel titleLbl = new JLabel("Rock Paper Scissors", JLabel.CENTER);
        titleLbl.setFont(new Font("Serif", Font.PLAIN, 30));
        titleLbl.setBorder(new EmptyBorder(0, 0, 20, 0));

        playPnl.add(titleLbl, BorderLayout.NORTH);
        buttonPnl.add(rockBtn);
        buttonPnl.add(paperBtn);
        buttonPnl.add(scissorsBtn);
        buttonPnl.add(quitBtn);
        playPnl.add(buttonPnl, BorderLayout.SOUTH);

    }

    /**
     * Create the stats panel (how many ties, wins per Player and Computer)
     */
    public void createStatsPanel(){
        statsPnl = new JPanel();
        statsPnl.setLayout(new GridLayout(2, 4));

        JLabel lbl1 = new JLabel("Player Wins:");
        JLabel lbl2 = new JLabel("Computer Wins:");
        JLabel lbl3 = new JLabel("Ties:");
        JLabel lbl4 = new JLabel("Total games:");

        userWinFld = new JTextField();
        compWinFld = new JTextField();
        tieFld = new JTextField();
        totalFld = new JTextField();

        userWinFld.setEditable(false);
        compWinFld.setEditable(false);
        tieFld.setEditable(false);
        totalFld.setEditable(false);

        userWinFld.setPreferredSize(new Dimension(120,20));
        compWinFld.setPreferredSize(new Dimension(120,20));
        tieFld.setPreferredSize(new Dimension(120,20));
        totalFld.setPreferredSize(new Dimension(120,20));


        statsPnl.add(lbl1);
        statsPnl.add(lbl2);
        statsPnl.add(lbl3);
        statsPnl.add(lbl4);
        statsPnl.add(userWinFld);
        statsPnl.add(compWinFld);
        statsPnl.add(tieFld);
        statsPnl.add(totalFld);
    }

    /**
     * Create the scrolling display box to show results of all games
     */
    public void createOutputPanel(){
        outputPnl = new JPanel();
        outputTA = new JTextArea(15, 40);
        outputTA.setEditable(false);
        scroller = new JScrollPane(outputTA);

        outputPnl.add(scroller);
    }

    /**
     * Logic for the rock paper scissors game and who should win a game.
     * @param userPlay - the hand shape the user plays, as an int (rock is 0, paper is 1, scissors is 2)
     *                 - using playOptions field as the guide for that int conversion
     */
    public void playRockPaperScissors(int userPlay) {
        // incrementing frequencies for user plays
        userPlayFreqs[userPlay]++;
        userPlays.add(userPlay);

        ArrayList<Object> computerActions = determineMove(userPlay);
        int compPlay = (Integer) computerActions.get(0);
        String strategy = (String) computerActions.get(1);

        // attempted a more efficient way of finding the winner than simply going through each possible win scenario
        // tie scenario
        if(userPlay == compPlay){
            ties++;
            tieFld.setText(Integer.toString(ties));
            outputTA.append("Tie Game " + strategy + "\n");
        }
        // rock and scissors scenario
        else if((userPlay == 2 && compPlay == 0) || (userPlay == 0 && compPlay == 2)){
            if(userPlay - compPlay > 0) {
                compWins++; // userPlay is scissors and compPlay is rock
                compWinFld.setText(Integer.toString(compWins));
                outputTA.append("Rock breaks Scissors (Computer Wins " + strategy + ")\n");
            }
            else {
                userWins++; // compPlay is scissors and userPlay is rock
                userWinFld.setText(Integer.toString(userWins));
                outputTA.append("Rock breaks Scissors (Player Wins " + strategy + ")\n");
            }
        }
        // user win scenario
        else if(userPlay - compPlay == 1){
            userWins++;
            userWinFld.setText(Integer.toString(userWins));
            outputTA.append(playOptions[userPlay] + defeatOptions[userPlay] + playOptions[compPlay] + " (Player Wins " + strategy + ")\n");
        }
        // computer win scenario
        else if(userPlay - compPlay == -1){
            compWins++;
            compWinFld.setText(Integer.toString(compWins));
            outputTA.append(playOptions[compPlay] + defeatOptions[compPlay] + playOptions[userPlay] + " (Computer Wins " + strategy + ")\n");
        }
        totalFld.setText(String.valueOf(ties+compWins+userWins));
    }

    public ArrayList<Object> determineMove(int userPlay){
        ArrayList<Integer> compPlayOptions = new ArrayList<>();

        // random
        Random rand = new Random();
        compPlayOptions.add(rand.nextInt(playOptions.length));

        // least used
        int minVal = userPlayFreqs[0];
        int minIndex = 0;
        for(int i=0; i<userPlayFreqs.length; i++){
            if(userPlayFreqs[i] <= minVal){
                minVal = userPlayFreqs[i];
                minIndex = i;
            }
        }
        compPlayOptions.add(setCompPlay(minIndex));

        // most used
        int maxVal = userPlayFreqs[0];
        int maxIndex = 0;
        for(int i=0; i<userPlayFreqs.length; i++){
            if(userPlayFreqs[i] >= maxVal){
                maxVal = userPlayFreqs[i];
                maxIndex = i;
            }
        }
        compPlayOptions.add(setCompPlay(maxIndex));

        // last used
        if(userPlays.size() > 1)
            compPlayOptions.add(setCompPlay(userPlays.get(userPlays.size()-2)));
        else // for the first game, the computer won't have any data, so use rock by default
            compPlayOptions.add(setCompPlay(0));

        // cheat
        Random percent = new Random();
        int cheat = percent.nextInt(100);
        compPlayOptions.add(setCompPlay(userPlay));

        int compPlay;
        String strategy;
        Random strategyPicker = new Random();
        int strategyWeight = strategyPicker.nextInt(1000);
        if(strategyWeight > 900) {
            compPlay = compPlayOptions.get(4); // cheat
            strategy = "Cheat";
        }
        else if (strategyWeight > 675){
            compPlay = compPlayOptions.get(3); // last used
            strategy = "Last Used";
        }
        else if (strategyWeight > 450){
            compPlay = compPlayOptions.get(2); // most used
            strategy = "Most Used";
        }
        else if (strategyWeight > 225){
            compPlay = compPlayOptions.get(1); // least used
            strategy = "Least Used";
        }
        else {
            compPlay = compPlayOptions.get(0); // random
            strategy = "Random";
        }

        ArrayList<Object> returnData = new ArrayList<>();
        returnData.add(compPlay);
        returnData.add(strategy);
        return returnData;
    }

    /**
     * @param comparator the index value that is being compared to the computer value
     * @return - the shape the computer should play, as an int
     */
    public int setCompPlay(int comparator){
        if(comparator == 0)
            return 1;
        else if(comparator == 1)
            return 2;
        else
            return 0;
    }
}
