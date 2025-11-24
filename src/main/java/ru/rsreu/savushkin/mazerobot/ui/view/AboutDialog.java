package ru.rsreu.savushkin.mazerobot.ui.view;

import javax.swing.*;
import java.awt.*;

/**
 * Окно приветствия и информации о программе.
 * Заменяет стандартный JOptionPane на настраиваемое окно.
 */
public class AboutDialog extends JDialog {

    public AboutDialog(Frame owner) {
        // Конструктор JDialog: родительское окно, название, модальность (блокировать родителя)
        super(owner, "About program:", true);

        // 1. Создание содержимого
        JTextArea infoArea = new JTextArea(7, 30);
        infoArea.setText(
                "Program: Intelligent Maze Robot Solver\n" +
                        "Author: Savushkin D.A.\n" +
                        "Version: 2.0\n\n" +
                        "Description: Demonstrates state-space search algorithms (DFS, BFS, A*) to find the optimal path in a maze.\n" +
                        "Controls: Use Shift + Arrow Keys for double step (jump)."
        );

        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setEditable(false);
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton closeButton = new JButton("Start");
        closeButton.addActionListener(e -> setVisible(false)); // Скрыть окно при нажатии

        // 2. Сборка макета
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(infoArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(contentPanel);
        pack(); // Установить размер окна по содержимому
        setLocationRelativeTo(owner); // Центрировать относительно главного окна или экрана
        setResizable(false);
    }
}