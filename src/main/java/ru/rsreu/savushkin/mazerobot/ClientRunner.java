package ru.rsreu.savushkin.mazerobot;

import ru.rsreu.savushkin.mazerobot.core.controller.MazeController;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.model.RobotAgent;
import ru.rsreu.savushkin.mazerobot.core.solver.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeEnvironment;
import ru.rsreu.savushkin.mazerobot.ui.view.MazeView;

import javax.swing.*;

public class ClientRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null,
                    "Программа: Интеллектуальный решатель задач.\n" +
                            "Автор: Савушкин. Версия 2.0\n" +
                            "Shift+Стрелки для прыжка.",
                    "О программе", JOptionPane.INFORMATION_MESSAGE);

            MazeModel maze = new MazeModel(20, 15);
            var env = new MazeEnvironment(maze);
            var agent = new RobotAgent<>(env);
            var pathMgr = new PathFindingManager();

            var view = new MazeView(maze, agent, pathMgr);
            new MazeController(agent, view, pathMgr);
            view.setVisible(true);
        });
    }
}