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
 * Выполняет инициализацию всех компонентов архитектуры MVC
 * (Model, View, Controller) и запускает графический интерфейс.
 * </p>
 *
 * @author Савушкин Д.А.
 * @version 2.0
 */
public class ClientRunner {
    /**
     * Основной метод приложения. Запускает инициализацию GUI в потоке AWT Event Dispatch Thread.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // 1. Инициализация MVC
            MazeModel maze = new MazeModel(20, 15);
            var env = new MazeEnvironment(maze);
            var agent = new RobotAgent<>(env);
            var pathMgr = new PathFindingManager();

            var view = new MazeView(maze, agent, pathMgr);
            new MazeController(agent, view, pathMgr);

            // 2. Вызов окна 'О программе'
            AboutDialog dialog = new AboutDialog(view);
            // Блокирует поток, пока не закрыт
            dialog.setVisible(true);

            // 3. Отображение главного окна
            view.setVisible(true);

            // Принудительный запрос фокуса для активации клавиатурного ввода
            view.requestFocusForPanel();
        });
    }
}