package com.foster.gui;

import com.foster.EnigmaMachine;
import com.foster.PlugboardPairs;
import com.foster.Reflector;
import com.foster.Rotor;
import com.foster.config.EnigmaConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ComponentAdapter;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class provides methods for the front end display, and connecting the UI to the backend logic
 */
public class Home {
    public JFrame frame;
    private EnigmaMachine enigmaMachine;
    private JPanel mainPanel;
    private JLabel lblTitle;
    private JPanel pnlLabel;
    private JLabel lblRot1;
    private JLabel lblRot2;
    private JLabel lblRot3;
    private JTextField tfInput;
    private JTextField tfResult;
    private JComboBox<Rotor> cbRotorSelection1;
    private JComboBox<Rotor> cbRotorSelection2;
    private JComboBox<Rotor> cbRotorSelection3;
    private JTextField tfRotVal1;
    private JTextField tfRotVal2;
    private JTextField tfRotVal3;
    private JSlider sdRot1;
    private JSlider sdRot2;
    private JSlider sdRot3;
    private JComboBox refBox;
    private JScrollPane spWirText;
    private JTextArea taWiring;
    private JList<String> lstPlugboard;
    private JButton btnAdd;
    private JButton btnDelete;

    public Home() {
        initPanels();
        initComponents();
        initListeners();
        initBackend();
        mainPanel.setFocusable(true);

        /**
         * Use the keyboard to input text to encrypt.
         */
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        String originalText = tfInput.getText();
                        //System.out.println(e.getKeyCode());
                        if (e.getID() != KeyEvent.KEY_PRESSED) return false;
                        if (e.isControlDown() && e.getKeyCode() == 67) {
                            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                            if (e.isAltDown()) {
                                clip.setContents(new StringSelection(tfResult.getText()), null);
                                return false;
                            }
                            clip.setContents(new StringSelection(tfInput.getText()), null);
                            return false;
                        }
                        if (e.isControlDown() && e.getKeyCode() == 86) {
                            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                            try {
                                tfInput.setText(tfInput.getText() + (String) clip.getData(DataFlavor.stringFlavor));
                                String newContent = tfInput.getText();
                                String additionalText = newContent.substring(originalText.length());
                                char[] chars = additionalText.toCharArray();
                                for (char c : chars) {
                                    EncryptAndDisplay(e, c, true);
                                }

                            } catch (UnsupportedFlavorException | IOException ex) {
                                return false;
                            }
                        }
                        if (e.getModifiersEx() != InputEvent.SHIFT_DOWN_MASK && e.getModifiersEx() != 0) return false;
                        if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                            if (tfInput.getText().length() == 0) return false;
                            tfInput.setText(tfInput.getText().substring(0, tfInput.getText().length() - 1));
                            tfResult.setText(tfResult.getText().substring(0, tfResult.getText().length() - 1));
                            return false;
                        }
                        EncryptAndDisplay(e, e.getKeyChar(), false);
                        return false;
                    }

                });

        tfRotVal1.addComponentListener(new ComponentAdapter() {
        });
    }

    /**
     * Encrypt typed character and display results, with each step of the machine shown in the JScroll console.
     *
     * @param e keyboard input
     * @param c character to be encrypted
     * @param fromArr flag to check if key is a letter
     */
    private void EncryptAndDisplay(KeyEvent e, char c, boolean fromArr) {
        String resultChar = String.valueOf(e.getKeyChar());
        if ((e.getKeyCode() > 64 && e.getKeyCode() < 91) || (e.getKeyCode() > 96 && e.getKeyCode() < 123) || e.getKeyCode() == 8) {
            var result = enigmaMachine.encrypt(c);
            resultChar = result.getResult();
            for (String s : result.getSteps()) {
                taWiring.append(s + "\n");
            }
            taWiring.append("\n");
            Rotor[] rotors = enigmaMachine.getRotors();
            setRotorLabelText(rotors);
        }
        if ((e.getKeyCode() > 64 && e.getKeyCode() < 91) || (e.getKeyCode() > 96 && e.getKeyCode() < 123) || e.getKeyCode() == 8 || e.getKeyCode() == 32) {
            if (!fromArr) {
                tfInput.setText(tfInput.getText() + e.getKeyChar());
            }
            tfResult.setText(tfResult.getText() + resultChar);
            return;
        }
        ;
    }

    /**
     * Use the config file to set initial conditions of the machine.
     */
    private void initBackend() {
        enigmaMachine = new EnigmaMachine(3);
        Rotor[] rotors = enigmaMachine.getRotors();
        rotors[2].setToChar(EnigmaConfig.Rotor1Initial);
        rotors[1].setToChar(EnigmaConfig.Rotor2Initial);
        rotors[0].setToChar(EnigmaConfig.Rotor3Initial);

        enigmaMachine.setReflector(EnigmaConfig.SelectedReflector);
        tfRotVal1.setText(String.valueOf(EnigmaConfig.Rotor1Initial));
        tfRotVal2.setText(String.valueOf(EnigmaConfig.Rotor2Initial));
        tfRotVal3.setText(String.valueOf(EnigmaConfig.Rotor3Initial));
        setRotorLabelText(rotors);
    }

    /**
     * Initialise setting options for the rotors, reflector and plugboard.
     */
    private void initComponents() {
        for (Rotor r : Rotor.PossibleRotors) {
            cbRotorSelection1.addItem(r);
            cbRotorSelection2.addItem(r);
            cbRotorSelection3.addItem(r);
        }
        cbRotorSelection1.setSelectedItem(EnigmaConfig.Rotor1);
        cbRotorSelection2.setSelectedItem(EnigmaConfig.Rotor2);
        cbRotorSelection3.setSelectedItem(EnigmaConfig.Rotor3);

        for (Reflector ref : Reflector.PossibleReflectors) {
            refBox.addItem(ref);
        }
        refBox.setSelectedItem(EnigmaConfig.SelectedReflector);

        DefaultListModel<String> lstPlugboardModel = new DefaultListModel<>();
        lstPlugboard.setModel(lstPlugboardModel);
    }

    private void initPanels() {
        frame = new JFrame();
        frame.setBounds(100, 100, 900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainPanel);
    }


    /**
     * Upon action, change values of the rotors/initial positions/plugboard pairs.
     */
    private void initListeners() {
        sdRot1.addChangeListener(changeEvent -> {
            enigmaMachine.getRotors()[2].setPosition(sdRot1.getValue());
            String alphaValue = String.valueOf(Rotor.toChar(sdRot1.getValue()));
            tfRotVal1.setText(String.valueOf(alphaValue));
            //System.out.println(sdRot1.getValue());

            Rotor r = enigmaMachine.getRotors()[2];
            r.setToChar(Rotor.toChar(sdRot1.getValue()));
            setRotorLabelText(enigmaMachine.getRotors());
        });
        sdRot2.addChangeListener(changeEvent -> {
            enigmaMachine.getRotors()[1].setPosition(sdRot2.getValue());
            String alphaValue = String.valueOf(Rotor.toChar(sdRot2.getValue()));
            tfRotVal2.setText(String.valueOf(alphaValue));

            Rotor r = enigmaMachine.getRotors()[1];
            r.setToChar(Rotor.toChar(sdRot2.getValue()));
            setRotorLabelText(enigmaMachine.getRotors());
        });
        sdRot3.addChangeListener(changeEvent -> {
            enigmaMachine.getRotors()[0].setPosition(sdRot3.getValue());
            String alphaValue = String.valueOf(Rotor.toChar(sdRot3.getValue()));
            tfRotVal3.setText(String.valueOf(alphaValue));

            Rotor r = enigmaMachine.getRotors()[0];
            r.setToChar(Rotor.toChar(sdRot3.getValue()));
            setRotorLabelText(enigmaMachine.getRotors());
        });
        cbRotorSelection1.addActionListener(actionEvent -> {
            enigmaMachine.setRotorAtPosition(2, (Rotor) cbRotorSelection1.getSelectedItem());
            enigmaMachine.getRotors()[2].setPosition(sdRot1.getValue());
            setRotorLabelText(enigmaMachine.getRotors());
        });
        cbRotorSelection2.addActionListener(actionEvent -> {
            enigmaMachine.setRotorAtPosition(1, (Rotor) cbRotorSelection2.getSelectedItem());
            enigmaMachine.getRotors()[1].setPosition(sdRot2.getValue());
            setRotorLabelText(enigmaMachine.getRotors());
        });
        cbRotorSelection3.addActionListener(actionEvent -> {
            enigmaMachine.setRotorAtPosition(0, (Rotor) cbRotorSelection3.getSelectedItem());
            enigmaMachine.getRotors()[0].setPosition(sdRot3.getValue());
            setRotorLabelText(enigmaMachine.getRotors());
        });
        refBox.addActionListener(e -> {
            enigmaMachine.setReflector((Reflector) refBox.getSelectedItem());
        });
        btnAdd.addActionListener(actionEvent -> {
            DefaultListModel<String> lstPlugboardModel = (DefaultListModel<String>) lstPlugboard.getModel();
            if (lstPlugboardModel.getSize() == 10) {
                JOptionPane.showMessageDialog(null, "You cannot have more than 10 plugboard pairs. Please remove one of the existing pairs to add a new one.");
                return;
            }
            ArrayList<Character> unmappedChars = PlugboardPairs.getRemainingAlphabetChars();
            Character[] unmappedCharsArray = new Character[unmappedChars.size()];
            unmappedCharsArray = unmappedChars.toArray(unmappedCharsArray);

            Character c1 = (Character) JOptionPane.showInputDialog(null, "Select the first letter of your plugboard pair!", "Plugboard Pairs",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    unmappedCharsArray,
                    unmappedCharsArray[0]);
            if (null == c1) return;

            unmappedChars.remove(c1);
            unmappedCharsArray = new Character[unmappedChars.size()];
            unmappedCharsArray = unmappedChars.toArray(unmappedCharsArray);
            Character c2 = (Character) JOptionPane.showInputDialog(null, "Select the second letter of your plugboard pair!", "Plugboard Pairs",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    unmappedCharsArray,
                    unmappedCharsArray[0]);
            if (null == c2) return;

            System.out.println(c1 + " " + c2);
            PlugboardPairs.BuildPairs(c1, c2);

            lstPlugboardModel.add(lstPlugboardModel.getSize(), c1 + " : " + c2);

        });
        btnDelete.addActionListener(actionEvent -> {
            int selectedIndex = lstPlugboard.getSelectedIndex();
            DefaultListModel<String> lstPlugboardModel = (DefaultListModel<String>) lstPlugboard.getModel();
            char key = lstPlugboardModel.get(selectedIndex).split(":")[0].toCharArray()[0];
            PlugboardPairs.RemovePairs(key);
            lstPlugboardModel.remove(selectedIndex);
        });
    }

    /**
     * Map and display rotor positions to corresponding current positions.
     *
     * @param rotors array of the 3 selected rotors
     */
    private void setRotorLabelText(Rotor[] rotors) {
        lblRot1.setText(String.valueOf(rotors[2].getOutput().charAt((rotors[2].getCurrentPosition()) % Rotor.NUM_LETTERS)));
        lblRot2.setText(String.valueOf(rotors[1].getOutput().charAt((rotors[1].getCurrentPosition()) % Rotor.NUM_LETTERS)));
        lblRot3.setText(String.valueOf(rotors[0].getOutput().charAt((rotors[0].getCurrentPosition()) % Rotor.NUM_LETTERS)));
    }
}

