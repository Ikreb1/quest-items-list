package com.github.questitems.ui;

import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MyPanel extends PluginPanel {
    private final static Color BACKGROUND_COLOR = ColorScheme.DARK_GRAY_COLOR;
    private final static Color BUTTON_COLOR = ColorScheme.DARKER_GRAY_COLOR;
    private final static Color BUTTON_HOVER_COLOR = ColorScheme.DARKER_GRAY_HOVER_COLOR;

    private final IconTextField searchBar = new IconTextField();
    private MyElement element;
    public MyPanel(MyElement element)
    {
        super();
        this.element = element;

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;

        add(element, c);
    }
}
