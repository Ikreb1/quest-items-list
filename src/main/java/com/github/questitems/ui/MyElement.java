package com.github.questitems.ui;

import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;

@Getter
public class MyElement extends JPanel
{
    private String myString;
    private JTextArea textArea;

    public MyElement(String myString)
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(ColorScheme.DARK_GRAY_COLOR, 5));
        setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

        this.myString = myString;

        textArea = new JTextArea(myString);
        textArea.setEditable(false);
        textArea.setFont(FontManager.getRunescapeBoldFont());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        add(textArea);
    }
}
