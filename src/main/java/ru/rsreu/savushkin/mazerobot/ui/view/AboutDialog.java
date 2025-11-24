package ru.rsreu.savushkin.mazerobot.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL; // Используем для потенциальной иконки

/**
 * Диалоговое окно, отображающее информацию о программе.
 * <p>Используется для показа приветственного сообщения и основных сведений перед началом работы.</p>
 */
public class AboutDialog extends JDialog {

    public AboutDialog(Frame owner) {
        super(owner, "About Intelligent Robot Solver", true);

        // --- 1. Создание информационного содержимого с HTML ---
        String infoHtml = "<html><body style='padding: 10px; font-family: sans-serif;'>" +
                "<h2>🤖 Intelligent Maze Robot Solver</h2>" +
                "<p><b>Version:</b> 2.0</p>" +
                "<p><b>Author:</b> Savushkin D.A.</p>" +
                "<hr style='border: 0; height: 1px; background: #ccc; margin: 8px 0;'>" +
                "<p>Demonstrates **state-space search algorithms** (DFS, BFS, A*) " +
                "to find the optimal path in a maze environment.</p>" +
                "<p style='margin-top: 15px;'><b>Controls:</b> Use Shift + Arrow Keys for double step (jump).</p>" +
                "</body></html>";

        JEditorPane infoPane = new JEditorPane("text/html", infoHtml);
        infoPane.setEditable(false);
        infoPane.setBackground(getBackground());
        infoPane.setBorder(new EmptyBorder(5, 15, 5, 15));

        // --- 2. Улучшенная кнопка "Start" ---
        JButton closeButton = new JButton("START");
        closeButton.addActionListener(e -> setVisible(false));

        // Стиль кнопки: жирный шрифт и синий/зеленый акцент
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        // Для macOS/Windows: используйте Color.GREEN или new Color(50, 150, 250) для синего
        closeButton.setBackground(new Color(60, 180, 75)); // Темно-зеленый
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false); // Убираем рамку фокуса

        // --- 3. Сборка макета ---
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Добавление небольшой иконки слева, если нужно
        JPanel headerPanel = new JPanel(new BorderLayout());
        // Можно добавить JLabel с иконкой здесь, например:
        // headerPanel.add(new JLabel(new ImageIcon("path/to/icon.png")), BorderLayout.WEST);

        contentPanel.add(infoPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(5, 10, 10, 10)); // Отступы снизу
        buttonPanel.add(closeButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 4. Настройка диалога
        setContentPane(contentPanel);
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }
}