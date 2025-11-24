package ru.rsreu.savushkin.mazerobot.core.controller;

import ru.rsreu.savushkin.mazerobot.core.solver.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.model.RobotAgent;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MoveAction;
import ru.rsreu.savushkin.mazerobot.ui.view.MazeView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeController {
    private final RobotAgent<?> agent;
    private final MazeView view;
    private final PathFindingManager pathMgr;
    private boolean gameRunning = false;

    public MazeController(RobotAgent<?> agent, MazeView view, PathFindingManager pathMgr) {
        this.agent = agent;
        this.view = view;
        this.pathMgr = pathMgr;
        view.setController(this);
        agent.addListener(view);
        setupKeyControls();
    }

    private void setupKeyControls() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameRunning) return;

                MoveAction action = switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> new MoveAction(0, -1, e.isShiftDown());
                    case KeyEvent.VK_DOWN -> new MoveAction(0, 1, e.isShiftDown());
                    case KeyEvent.VK_LEFT -> new MoveAction(-1, 0, e.isShiftDown());
                    case KeyEvent.VK_RIGHT -> new MoveAction(1, 0, e.isShiftDown());
                    default -> null;
                };

                if (action != null && agent.applyAction(action) && agent.isAtGoal()) {
                    gameRunning = false;
                    view.showVictory();
                }
            }
        });
    }

    public void startGame() {
        gameRunning = true;
        view.enableGameControls(true);
    }

    public void findPath() {
        var path = pathMgr.findPath(agent.getEnvironment());
        view.showPath(path);
    }

    public void changeAlgorithm(String name) {
        pathMgr.setAlgorithm(name);
    }
}