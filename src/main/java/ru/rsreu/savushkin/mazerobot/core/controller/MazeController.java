package ru.rsreu.savushkin.mazerobot.core.controller;

import ru.rsreu.savushkin.mazerobot.core.solver.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.model.RobotAgent;
import ru.rsreu.savushkin.mazerobot.core.state.Environment; // <-- ИМПОРТ
import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeState;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MoveAction;
import ru.rsreu.savushkin.mazerobot.ui.view.MazeView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeController {
    private final RobotAgent<?> agent;
    private final MazeView view;
    private final PathFindingManager pathMgr;
    private boolean gameRunning = true;

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

                int dx = 0, dy = 0;
                boolean isDouble = e.isShiftDown();

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: dy = -1; break;
                    case KeyEvent.VK_DOWN: dy = 1; break;
                    case KeyEvent.VK_LEFT: dx = -1; break;
                    case KeyEvent.VK_RIGHT: dx = 1; break;
                    default: return;
                }

                if (isDouble) {
                    dx *= 2;
                    dy *= 2;
                }

                MoveAction action = new MoveAction(dx, dy, isDouble);

                if (agent.applyAction(action)) {
                    if (agent.isAtGoal()) {
                        gameRunning = false;
                        view.showVictory();
                    }
                }
            }
        });
    }

    public void startGame() {
        gameRunning = true;
        view.enableGameControls(true);
        view.requestFocusForPanel();
    }

    public void findPath() {
        // --- ИСПРАВЛЕНИЕ ОШИБКИ GENERICS ---

        // 1. Явно приводим текущее состояние к конкретному типу MazeState
        MazeState startState = (MazeState) agent.getCurrentState();

        // 2. Явно приводим окружение к конкретному типу Environment<MazeState, ?>
        @SuppressWarnings("unchecked")
        Environment<MazeState, ?> environment = (Environment<MazeState, ?>) agent.getEnvironment();

        // 3. Теперь вызов метода PathFindingManager работает без проблем
        var path = pathMgr.findPath(environment, startState);

        view.showPath(path);
    }

    public void changeAlgorithm(String name) {
        pathMgr.setAlgorithm(name);
    }
}