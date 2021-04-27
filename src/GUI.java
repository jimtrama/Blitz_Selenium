import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JFrame menuFrame;
    private boolean rankedGame;
    private String friend;
    public GUI(){
        rankedGame=true;
    }
    public void run(){
        menuFrame = new JFrame();
        menuFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
        menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuFrame.setTitle("Word Blitz Solver");
        menuFrame.setSize(300, 300);
        JButton startGame = new JButton("Start Game");
        JTextField username = new JTextField("Friends Biltz Name");
        JRadioButton radioButton1 = new JRadioButton("Ranked Game",true);
        JRadioButton radioButton2 = new JRadioButton("With a Friend",false);
        radioButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rankedGame=false;

            }
        });
        radioButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rankedGame=true;

            }
        });
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Logic(username.getText(),rankedGame);
                startGame.removeActionListener(this);
            }
        });
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton1);
        group.add(radioButton2);

        menuFrame.add(radioButton1);
        menuFrame.add(radioButton2);
        menuFrame.add(username);
        menuFrame.add(startGame);
        menuFrame.setVisible(true);
    }
}
