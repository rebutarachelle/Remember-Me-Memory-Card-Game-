import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; // to store the card in the game.
import javax.swing.*; // GUI

public class MemoryCard { 
    class Card { // represents the cards
        String cardName;
        ImageIcon cardImageIcon;

        Card (String cardName, ImageIcon cardImageIcon) { // constructor
            this.cardName = cardName;
            this.cardImageIcon= cardImageIcon;

        }

        public String toString() { // to print out the class, create a string representation... when create a card object and print it, the program will call this function.
            return cardName;
        }

    }    


    String[] cardList = { // track cardNames
        "eclipse",
        "java",
        "jvdroid",
        "minecraft",
        "netflix",
        "spotify",
        "vscode",
        "x"
    };

    int rows = 4;
    int columns = 4;
    int cardWidth = 100;
    int cardHeight = 148;

    ArrayList<Card> cardSet; // create a deck of cards with cardName and cardImageIcon
    ImageIcon cardBackImageIcon; 

    // Game Window
    int boardWidth = columns * cardWidth;  
    int boardHeight = rows * cardHeight;

    JFrame frame = new JFrame("Remember Me (Memory Card Game)");
    JLabel textLabel = new JLabel();
    JLabel textLabel2 = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardJPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    int errorCount = 0;
    int attemptCount = 0;
    ArrayList<JButton> board;
    Timer hideCardTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;


     

    MemoryCard() {
        setupCards();
        shuffleCards();

        //frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Attempt text
        textLabel2.setFont(new Font("Arial" , Font.PLAIN, 15));
        textLabel2.setHorizontalAlignment(JLabel.CENTER);
        textLabel2.setText("Attempts: " + Integer.toString(attemptCount));

        // Error text
        textLabel.setFont(new Font("Arial" , Font.PLAIN, 15));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: " + Integer.toString(errorCount));


        textPanel.setLayout(new GridLayout(1, 2)); // 1 row, 2 columns
        textPanel.add(textLabel); // Add error label
        textPanel.add(textLabel2); // Add attempt label

        textPanel.setPreferredSize(new Dimension(boardWidth, 20));
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);

        //card game
        board = new ArrayList<JButton>(); // keeping track all the buttons
        boardJPanel.setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque( true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameReady) {
                        return;
                    }
                    JButton tile = (JButton) e.getSource();
                    if (tile.getIcon() == cardBackImageIcon) {
                        if (card1Selected == null) {
                            card1Selected = tile;
                            int index = board.indexOf(card1Selected);
                            card1Selected.setIcon(cardSet.get(index).cardImageIcon);

                        }
                        else if (card2Selected == null) {
                            card2Selected = tile;
                            int index = board.indexOf(card2Selected);
                            card2Selected.setIcon(cardSet.get(index).cardImageIcon);

                            if (card1Selected.getIcon() != card2Selected.getIcon()) {
                                errorCount += 1;
                                textLabel.setText("Errors: " + Integer.toString(errorCount));
                                hideCardTimer.start();
                            }
                            else {
                                card1Selected = null;
                                card2Selected = null;
                            }
                        }
                    }
                }
            });
            board.add(tile);
            boardJPanel.add(tile);

        }
        frame.add(boardJPanel);

        //restart button
        restartButton.setFont(new Font("Arial", Font.PLAIN, 15));
        restartButton.setText("Restart the game.");
        restartButton.setPreferredSize(new Dimension(boardWidth, 20));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameReady) {
                    return;
                }

                gameReady = false;
                card1Selected = null;
                card2Selected = null;
                shuffleCards();

                //re assign buttons with new cards
                for (int i = 0; i < board.size(); i++) {
                    board.get(i).setIcon(cardSet.get(i).cardImageIcon);
                }
                errorCount = 0;
                textLabel.setText("Errors: " + Integer.toString(errorCount));
                
                attemptCount++;
                textLabel2.setText("Attempts: " + Integer.toString(attemptCount));
                hideCardTimer.start();

            }
            
        });
        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        //start game
        hideCardTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();

            }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();

    }

    void setupCards() {
        cardSet = new ArrayList<Card>();
        for (String cardName : cardList) { // iterate through array of cardName

            // load card Image

                            //getClass refers to MemoryCard Class
            Image cardImg = new ImageIcon(getClass().getResource("./Logo/" + cardName + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

            // create card object and add to CardSet
            Card card = new Card(cardName, cardImageIcon);
            cardSet.add(card);
        }
        // extends the cardSet
        cardSet.addAll(cardSet);

        // load the cover/ back card image
        Image cardBackImg = new ImageIcon(getClass().getResource("./Logo/cover.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

    }

    void shuffleCards() {
        System.out.println(cardSet);

        // shuffle
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int) (Math.random() * cardSet.size()); // get random index

            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
        System.out.println(cardSet);
    }

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null) {
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
        }
        else { // flip all cards face down
            for (int i = 0; i < board.size(); i++) {  
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);

        }
        
    }
    
}
