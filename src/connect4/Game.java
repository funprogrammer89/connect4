package connect4;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * The Game.java program is a GUI application that allows two people to play Connect 4 and have oh so much fun!.
 *
 * @author Ken Elliott
 * @version 2021
 * @published
 */
public class Game {
    public enum Piece {
        RED,
        BLACK,
        EMPTY
    }
    final int ROW = 6;
    final int COLUMN =7;
    //2d Array to keep track of the current state of the game
    Piece[][] gameState = new Piece[ROW][COLUMN];
    // int variable to keep track of turn count which is used to check ties as well as help keep tack of whose turn it is
    int turnCount = 0;
    JLabel messageLabel = new JLabel("Player 1's turn");
    JButton[] button = new JButton[7];
    JLabel[][] slotLabel = new JLabel[ROW][COLUMN];
    // Loading in images to ImageIcon(s)
    ImageIcon slot = new ImageIcon("slot.png");
    ImageIcon black = new ImageIcon("black-play.png");
    ImageIcon black2 = new ImageIcon("black.png");
    ImageIcon red = new ImageIcon("red-play.png");
    ImageIcon red2 = new ImageIcon("red.png");
    ImageIcon redWin = new ImageIcon("red-win.png");
    ImageIcon blackWin = new ImageIcon("black-win.png");
    /**
     * Game method that is called by the main method.
     * This method creates the GUI and GUI components using JFrames and JPanels and more
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public Game() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        // Creation of the Connect 4 board and setting all squares to empty values
        for(int i=0; i<ROW;i++) {
            for(int j=0;j<COLUMN;j++) {
                gameState[i][j] = Piece.EMPTY;
            }
        }
        JFrame frame = new JFrame("Connect 4");
        JPanel panel = new JPanel();
        frame.add(messageLabel, BorderLayout.NORTH);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.setLayout(new GridLayout(ROW + 1, COLUMN));
        // Loop that creates an array of buttons that the user will click when making a move
        for(int i=0; i< ROW+1; i++) {
            button[i] = new JButton(black);
            button[i].setMargin(new Insets(-5,-5,-5,-5));
            final int column = i;
            button[i].addActionListener(e -> playColumn(column));
            panel.add(button[i]);
        }
        // Create board using slot images
        for(int i=0; i<ROW; i++) {
            for(int j=0; j<COLUMN;j++) {
                slotLabel[i][j] = new JLabel(slot);
                panel.add(slotLabel[i][j]);
            }
        }
        frame.pack();
        frame.setVisible(true);
    }
    // Method that handles the audio that is played when a user clicks a button to play a move.

    private void audioStuff() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        Clip clip = AudioSystem.getClip();
        File soundFile = new File("click.wav");
        clip.open(AudioSystem.getAudioInputStream(soundFile));
        clip.start();
    }

    // Method that is called when a user presses one of the top buttons to make a move. This method takes an int of the column being played
    private void playColumn(int column) {
        int i = ROW - 1;
        while(i >= 0 && gameState[i][column] != Piece.EMPTY) {
            --i;
        }
        if(i == 0) {
            button[column].setIcon(null);
            button[column].setEnabled(false);
        }
        if(turnCount % 2 == 1) {
            gameState[i][column] = Piece.RED;
            slotLabel[i][column].setIcon(red2);
        }
        else {
            gameState[i][column] = Piece.BLACK;
            slotLabel[i][column].setIcon(black2);
        }
        if(winCheck(i,column)) {

            for(int j = 0;j<COLUMN;j++) {
                button[j].setIcon(null);
                button[j].setEnabled(false);
            }
            return;
        }
        // Clever way to determine whose turn it is
        turnCount++;
        if(turnCount % 2 == 0) {
            messageLabel.setText("Player 1's Turn");
            for(int j = 0;j<COLUMN;j++) {
                button[j].setIcon(black);
            }
        }
        else {
            messageLabel.setText("Player 2's Turn");
            for(int j = 0;j<COLUMN;j++) {
                button[j].setIcon(red);
            }
        }
        // Exception handler for audio

        try {
            audioStuff();
        } catch (LineUnavailableException e1) {

            e1.printStackTrace();
        } catch (IOException e1) {

            e1.printStackTrace();
        } catch (UnsupportedAudioFileException e1) {

            e1.printStackTrace();
        }

        // A Tie! The turn count matches 42 which is when every slot is full and no one has won

        if(turnCount == (ROW * COLUMN)) {
            messageLabel.setText("Tied Game!");
            return;
        }
    }

    // Method that is called to check for wins after a piece is played by the user. This method takes a row and column that is currently being played

    private boolean winCheck( int row, int column) {
        Piece piece = gameState[row][column];
        ImageIcon icon;
        if(piece==Piece.BLACK) {
            icon = blackWin;
        }
        else{
            icon = redWin;
        }
        boolean won = false;

        // Loop to check the rows in the column played for wins

        int i = row;
        int count = 0;
        while(i < ROW && gameState[i][column]==piece) {
            ++i;
            ++count;

            // If connect 4 or more has been found, change the slotLabels to winning pieces image and also announce who won
        }
        if(count >= 4) {
            won = true;
            i = row;
            while(i < ROW && gameState[i][column]==piece) {
                slotLabel[i][column].setIcon(icon);
                if(turnCount % 2 == 1) {
                    messageLabel.setText("Player 2 wins!");
                }
                else {
                    messageLabel.setText("Player 1 wins!");
                }
                ++i;
            }
        }
        // Loop to check the rows to the right of the piece being played for a win
        int j  = column;
        count = 0;
        while(j < COLUMN && gameState[row][j]==piece) {
            ++j;
            ++count;
        }

        // Loop to check the rows to the left of the piece being played for a win
        j = column - 1;
        while(j>=0 && gameState[row][j]==piece) {
            --j;
            ++count;

            // If 4 or more of a kind in a row, highlight the pieces and announce winner.
        }
        if(count >= 4) {
            won = true;
            j  = column;
            while(j < COLUMN && gameState[row][j]==piece) {
                slotLabel[row][j].setIcon(icon);
                ++j;
            }
            j = column - 1;
            while(j>=0 && gameState[row][j]==piece) {
                slotLabel[row][j].setIcon(icon);
                --j;
            }
            if(turnCount % 2 == 1) {
                messageLabel.setText("Player 2 wins!");
            }
            else {
                messageLabel.setText("Player 1 wins!");
            }
        }
        // Loop to check for diagonal wins going from lower left to upper right going to the right
        count=0;
        int k = column;
        int m = row;
        while((m>=0&&k<COLUMN)&&gameState[m][k]==piece) {
            --m;
            ++k;
            ++count;
        }
        // Loop to check for diagonal wins going from lower left to upper right going to the left
        k = column-1;
        m = row+1;
        while((k>=0&&m<=ROW-1)&&gameState[m][k]==piece) {
            ++m;
            --k;
            ++count;
        }
        // If diagonal connect 4 from lower left to upper right, highlight pieces and announce winner.
        if(count >= 4) {
            won = true;
            // Highlight winning pieces one direction
            k = column-1;
            m = row+1;
            while((k>=0&&m<=ROW-1)&&gameState[m][k]==piece) {
                slotLabel[m][k].setIcon(icon);
                ++m;
                --k;
                ++count;
            }
            // Highlight winning pieces the other direction
            k = column;
            m = row;
            while((m>=0&&k<COLUMN)&&gameState[m][k]==piece) {
                slotLabel[m][k].setIcon(icon);
                --m;
                ++k;
                ++count;
            }
            if(turnCount % 2 == 1) {
                messageLabel.setText("Player 2 wins!");
            }
            else {
                messageLabel.setText("Player 1 wins!");
            }
        }
        // Loop to check for diagonal wins going from upper left to lower right going to the right

        count=0;
        k = column;
        m = row;
        while((m<=ROW-1&&k<COLUMN)&&gameState[m][k]==piece) {
            ++m;
            ++k;
            ++count;
        }
        // Loop to check for diagonal wins going from upper left to lower right going to the left
        k = column-1;
        m = row-1;
        while((m>=0&&k>=0)&&gameState[m][k]==piece) {
            --m;
            --k;
            ++count;
        }
        // If diagonal connect 4 from upper left to lower right, highlight pieces and announce winner.
        if(count >= 4) {
            won = true;
            k = column-1;
            m = row-1;
            // Highlight winning pieces one direction
            while((m>=0&&k>=0)&&gameState[m][k]==piece) {
                slotLabel[m][k].setIcon(icon);
                --m;
                --k;
                ++count;
            }
            k = column;
            m = row;
            // Highlight winning pieces the other direction
            while((m<=ROW-1&&k<COLUMN)&&gameState[m][k]==piece) {
                slotLabel[m][k].setIcon(icon);
                ++m;
                ++k;
                ++count;
            }
            if(turnCount % 2 == 1) {
                messageLabel.setText("Player 2 wins!");
            }
            else {
                messageLabel.setText("Player 1 wins!");
            }
        }
        return won;
    }
    public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        //Start the Fun!
        new Game();
    }
}