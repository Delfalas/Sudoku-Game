package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Sudoku {
	class Tile extends JButton {
        int r;
        int c;
        Tile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }
	
	int boardWidth = 650;
    int boardHeight = 750;
    boolean gameWon = false;
     
    
    String[] puzzle = {
        "--74916-5",
        "2---6-3-9",
        "-----7-1-",
        "-586----4",
        "--3----9-",
        "--62--187",
        "9-4-7---2",
        "67-83----",
        "81--45---"
    };

    String[] solution = {
        "387491625",
        "241568379",
        "569327418",
        "758619234",
        "123784596",
        "496253187",
        "934176852",
        "675832941",
        "812945763"
    };
    
    JFrame frame = new JFrame("Sudoku");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    
    JButton numSelected = null;
    int errors = 0;
    
    Tile[][] tiles = new Tile[9][9]; //armazenar os tiles em uma matriz
    
    Sudoku() {
    	
    	//janela do programa
    	frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        
        //painel do titulo e contagem de erros
        textLabel.setFont(new Font("Arial", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Sudoku: 0");
        
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);
        
        //painel do board do jogo
        boardPanel.setLayout(new GridLayout(9, 9));
        setupTiles();
        frame.add(boardPanel, BorderLayout.CENTER);
        
        buttonsPanel.setLayout(new GridLayout(1, 9));
        setupButtons();
        frame.add(buttonsPanel, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }
    
    // criação do tabuleiro
    void setupTiles() {
    	if (gameWon) return;
    	
    	for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
               Tile tile = new Tile(r, c);
                char tileChar = puzzle[r].charAt(c);
                if (tileChar != '-') {
                	tile.setFont(new Font("Arial", Font.BOLD, 20));
                	tile.setText(String.valueOf(tileChar));
                	tile.setBackground(Color.lightGray);
                }
                else {
                	tile.setFont(new Font("Arial", Font.PLAIN, 20));
                    tile.setBackground(Color.white);
                }
                if ((r == 2 && c == 2) || (r == 2 && c == 5) || (r == 5 && c == 2) || (r == 5 && c == 5)) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.black));
                }
                else if (r == 2 || r == 5) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.black));
                }
                else if (c == 2 || c == 5) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.black));
                }
                else {
                    tile.setBorder(BorderFactory.createLineBorder(Color.black));
                }
                tile.setFocusable(false);
                boardPanel.add(tile);
                
                tiles[r][c] = tile; //salvar cada tile ao criar

                //colocar os números no tabuleiro
                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Tile tile = (Tile) e.getSource();
                        int r = tile.r;
                        int c = tile.c;
                        if (numSelected != null) {
                            if (tile.getText() != "") {
                                return;
                            }
                            String numSelectedText = numSelected.getText();
                            String tileSolution = String.valueOf(solution[r].charAt(c));
                            if (tileSolution.equals(numSelectedText)) {
                                tile.setText(numSelectedText);
                                if (checkWin()) {
                                	gameWon = true;
                                    JOptionPane.showMessageDialog(frame, "Parabéns! Você completou o Sudoku!");
                                }
                            }
                            else {
                                errors += 1;
                                textLabel.setText("Sudoku: " + String.valueOf(errors));
                            }
                        }
                    }
                });
            }
    	}
    }
    
    //botões na parte inferior
    void setupButtons() {
        // Painel para os botões de 1 a 9
        JPanel numberButtonsPanel = new JPanel();
        numberButtonsPanel.setLayout(new GridLayout(1, 9));

        for (int i = 1; i < 10; i++) {
            JButton button = new JButton();
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setText(String.valueOf(i));
            button.setFocusable(false);
            button.setBackground(Color.white);
            numberButtonsPanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    if (numSelected != null) {
                        numSelected.setBackground(Color.white);
                    }
                    numSelected = button;
                    numSelected.setBackground(Color.lightGray);
                }
            });
        }

        // Painel para o botão "Reiniciar"
        JPanel resetButtonPanel = new JPanel();
        ImageIcon icon = new ImageIcon("refresh.png");
        JButton resetButton = new JButton("Reiniciar", icon);
        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        resetButton.setIcon(new ImageIcon(img));
        resetButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetButton.setFocusable(false);
        resetButton.setBackground(new Color(220, 240, 255));
        resetButton.setPreferredSize(new Dimension(140, 40));
        resetButtonPanel.add(resetButton);

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        // Organiza os dois painéis (números em cima, reset embaixo)
        buttonsPanel.setLayout(new GridLayout(2, 1));
        buttonsPanel.add(numberButtonsPanel);
        buttonsPanel.add(resetButtonPanel);
    }
    
    //método para verificar se completou o jogo
    boolean checkWin() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String numSelectedText = tiles[r][c].getText();
                String tileSolution = String.valueOf(solution[r].charAt(c));
                if (!numSelectedText.equals(tileSolution)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    //método para resetar o jogo
    void resetGame() {
        errors = 0;
        textLabel.setText("Sudoku: 0");
        numSelected = null;
        gameWon = false;

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                char tileChar = puzzle[r].charAt(c);
                Tile tile = tiles[r][c];
                if (tileChar != '-') {
                    tile.setText(String.valueOf(tileChar));
                    tile.setBackground(Color.lightGray);
                } else {
                    tile.setText("");
                    tile.setBackground(Color.white);
                }
            }
        }
    }

}
