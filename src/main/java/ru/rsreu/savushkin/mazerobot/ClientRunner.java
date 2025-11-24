package ru.rsreu.savushkin.mazerobot;

import ru.rsreu.savushkin.mazerobot.core.controller.MazeController;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.model.RobotAgent;
import ru.rsreu.savushkin.mazerobot.core.solver.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeEnvironment;
import ru.rsreu.savushkin.mazerobot.ui.view.AboutDialog;
import ru.rsreu.savushkin.mazerobot.ui.view.MazeView;

import javax.swing.*;

/**
 * Точка входа в приложение.
 * <p>
 * Выполняет инициализацию MVC компонентов и вывод приветственного сообщения.
 * * @author Савушкин
 * @version 2.0
 */
public class ClientRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // 1. Инициализация MVC
            MazeModel maze = new MazeModel(20, 15);
            var env = new MazeEnvironment(maze);
            var agent = new RobotAgent<>(env);
            var pathMgr = new PathFindingManager();

            var view = new MazeView(maze, agent, pathMgr);
            new MazeController(agent, view, pathMgr);

            // 2. Вызов окна 'О программе' (п. 5 требований)
            AboutDialog dialog = new AboutDialog(view);
            dialog.setVisible(true); // Блокирует поток, пока не закрыт

            // 3. Отображение главного окна
            view.setVisible(true);

            // !!! ФИКС ФОКУСА: Принудительный запрос фокуса после закрытия диалога
            view.requestFocusForPanel();
        });
    }
}