package com.baghira.ui;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DownloadedImageCellRenderer extends DefaultListCellRenderer {

    private final List<ImageIcon> iconList;
    Font font = new Font(Font.MONOSPACED, Font.BOLD, 18);

    public DownloadedImageCellRenderer(List<ImageIcon> iconList) {
        this.iconList = iconList;
    }

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        label.setForeground(JBColor.GREEN);
        label.setOpaque(true);
        label.setIcon(iconList.get(index));
        label.setIconTextGap(5);
        label.setHorizontalTextPosition(JLabel.RIGHT);
        label.setFont(font);
        return label;
    }
}