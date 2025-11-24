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
        super(owner, "О программе: AI Solver", true);

        // 1. Создание содержимого
        JTextArea infoArea = new JTextArea(7, 30);
        infoArea.setText(
                        "📜 Программа: Интеллектуальный решатель задач Робот в лабиринте\n" +
                        "---------------------------------------------------\n" +
                        "📚 Назначение: Демонстрация работы алгоритмов поиска\n" +
                        "   (DFS, BFS) в пространстве состояний.\n" +
                        "💡 Универсальная архитектура 'Situation'.\n\n" +
                        "👤 Автор: Савушкин Д.А.\n" +
                        "⚙️ Версия: 2.0\n" +
                        "⌨️ Управление: Shift + Стрелки для двойного шага (прыжка)."
        );
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setEditable(false);
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton closeButton = new JButton("Начать");
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