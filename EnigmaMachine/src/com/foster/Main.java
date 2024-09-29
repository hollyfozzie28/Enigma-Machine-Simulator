package com.foster;

import com.foster.config.EnigmaConfig;
import com.foster.gui.Home;

import javax.swing.*;
import java.awt.*;

public enum Main {
    ;

    public static void main(final String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
//        EnigmaMachine m = new EnigmaMachine();
//        m.Run("The Quick Brown Fox Jumps Over The Lazy Dog");
        EnigmaConfig.checkAndGenerate();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        EventQueue.invokeLater(() -> {
            try {
                final Home window = new Home();
                window.frame.setVisible(true);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });

    }
}