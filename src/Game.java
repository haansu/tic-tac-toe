import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game {
    // Class global variables
    static private JFrame frame;
    static private JLabel label;
    static private JButton[] tiles;

    private boolean loop;
    private boolean isXTurn;
    private byte counter;
    static private ValidEntries[] entries;

    private enum ValidEntries {
        x, o, no
    }

    public Game() {
        System.out.println("Initialized");

        // Initialisation
        isXTurn = true;
        loop = true;
        counter = 0;

        entries = InitialiseEntries();

        // Label configuration
        short labelFontSize = 50;
        Font labelFont = new Font("Courier", Font.BOLD, labelFontSize);

        label = new JLabel("Tic Tac Toe");
        label.setBounds(100, 30, 300, 50);
        label.setFont(labelFont);
        label.setForeground(Color.WHITE);

        frame = new JFrame();
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.BLACK);

        // Button configuration
        tiles = SetButtons();

        for (byte i = 0; i < 9; i++) {
            OnButtonsClick(tiles, i);
        }

        // Frame configuration
        frame.add(label);

        frame.setSize(500,650);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private ValidEntries[] InitialiseEntries() {
        ValidEntries[] entry = new ValidEntries[9];

        // Initialised with no - this being used as a null like element
        for(byte i = 0; i < 9; i++)
            entry[i] = ValidEntries.no;

        return entry;
    }

    private JButton[] SetButtons() {
        JButton[] buttons = new JButton[9];

        // This is the debug only text
        for(byte i = 0; i < 9; i++)
            if(entries[i] != ValidEntries.no)
                buttons[i] = new JButton(entries[i].toString() + i);
            else
                buttons[i] = new JButton("");

        short posX = 10;
        short posY = 100;

        short width = 150;
        short height = 150;

        for(byte i = 0; i < 9; i++) {
            buttons[i].setBounds(posX, posY, width, height);

            buttons[i].setBackground(Color.darkGray);
            buttons[i].setForeground(Color.white);

            short size = 100;
            Font buttonFont = new Font("Courier", Font.BOLD, size);
            buttons[i].setFont(buttonFont);

            posX += 155;
            if((i + 1) % 3 == 0) {
                posX = 10;
                posY += 155;
            }

            frame.add(buttons[i]);
        }

        return buttons;
    }

    // Handles the event for every button in the array
    private void OnButtonsClick(JButton[] _buttonArray, byte _position) {
        _buttonArray[_position].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent _e) {
                if (loop) {
                    if (entries[_position] == ValidEntries.no) {
                        // Setts button label to the correct entry;
                        counter++;
                        if (isXTurn) {
                            _buttonArray[_position].setText("X");
                            entries[_position] = ValidEntries.x;
                            isXTurn = false;
                        } else {
                            _buttonArray[_position].setText("O");
                            entries[_position] = ValidEntries.o;
                            isXTurn = true;
                        }

                        // Checks winning conditions
                        ValidEntries verify = Verify(_position);

                        // Winning and draw actions
                        if (verify != ValidEntries.no) {
                            System.out.println("The winner is " + verify.toString());

                            loop = false;

                            label.setText(verify.toString().toUpperCase() + " won!");
                            label.setBounds(160, 30, 300, 50);
                        } else if (counter == 9) {
                            System.out.println("Draw");

                            loop = false;

                            label.setText("Draw!");
                            label.setBounds(170, 30, 300, 50);
                        }
                    }
                }
            }
        });
    }

    private ValidEntries Verify(byte _position) {
        boolean win = true;

        ValidEntries current;
        current = entries[_position];

        // Vertical check
        for(byte i = (byte) (_position % 3); i < 9; i += 3)
            if(current != entries[i]) {
                win = false;
                break;
            }

        // Debug only
        System.out.println("win-vertical: \t\t\t" + win);

        if(win)
            return current;

        // Horizontal check
        win = true;
        for(byte i = (byte) (_position - _position % 3); i <= _position - _position % 3 + 2; i++)
            if(current != entries[i]) {
                win = false;
                break;
            }

        // Debug only
        System.out.println("win-horizontal: \t\t" + win);

        if(win)
            return current;

        // Diagonal check
        if(_position % 2 == 0) {

            // Check First Diagonal
            win = true;
            for(byte i = 0; i < 9; i += 4)
                if (current != entries[i]) {
                    win = false;
                    break;
                }

            // Debug only
            System.out.println("win-first-diagonal: \t" + win);

            if(win)
                return current;

            // Check Second Diagonal
            win = true;
            for(byte i = 2; i < 7; i += 2)
                if (current != entries[i]) {
                    win = false;
                    break;
                }

            // Debug only
            System.out.println("win-second-diagonal: \t" + win);

            if(win)
                return current;
        }

        // Returns no (enum arbitrary null-like value)
        return ValidEntries.no;
    }
}
