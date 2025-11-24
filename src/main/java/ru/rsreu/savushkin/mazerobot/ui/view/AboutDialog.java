package ru.rsreu.savushkin.mazerobot.ui.view;

import javax.swing.*;
import java.awt.*;

/**
 * Диалоговое окно, отображающее информацию о программе.
 * <p>Используется для показа приветственного сообщения и основных сведений перед началом работы.</p>
 */
public class AboutDialog extends JDialog {

    /**
     * Создает и отображает модальное диалоговое окно "О программе".
     *
     * @param owner Родительское окно (фрейм), относительно которого центрируется диалог.
     */
    public AboutDialog(Frame owner) {
        // Конструктор JDialog: родительское окно, название, модальность (блокировать родителя)
        super(owner, "About program:", true);

        // 1. Создание содержимого (информационной области)
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

        // 2. Создание кнопки "Start"
        JButton closeButton = new JButton("Start");
        // Обработчик: скрыть окно при нажатии
        closeButton.addActionListener(e -> setVisible(false));

        // 3. Сборка макета
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(infoArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 4. Настройка диалога
        setContentPane(contentPanel);
        pack(); // Установить минимальный размер, необходимый для содержимого
        setLocationRelativeTo(owner); // Центрировать относительно главного окна
        setResizable(false);
    }
}