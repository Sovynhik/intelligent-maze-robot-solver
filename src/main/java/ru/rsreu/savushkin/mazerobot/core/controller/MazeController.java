package ru.rsreu.savushkin.mazerobot.core.controller;

import ru.rsreu.savushkin.mazerobot.core.solver.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.model.RobotAgent;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeState;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MoveAction;
import ru.rsreu.savushkin.mazerobot.ui.view.MazeView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * <p>Контроллер, управляющий взаимодействием между агентом-роботом, средой (лабиринтом) и пользовательским интерфейсом (представлением).</p>
 * <p>Обрабатывает ввод пользователя (нажатия клавиш) и инициирует поиск пути с помощью PathFindingManager.</p>
 */
public class MazeController {
    private final RobotAgent<?> agent;
    private final MazeView view;
    private final PathFindingManager pathMgr;
    private boolean gameRunning = true;

    /**
     * Создает новый контроллер лабиринта.
     *
     * @param agent Агент-робот, управляющий состоянием.
     * @param view Представление, отображающее лабиринт.
     * @param pathMgr Менеджер, управляющий алгоритмами поиска пути.
     */
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

    /**
     * Начинает игру, устанавливая флаг {@code gameRunning} в true и активируя элементы управления.
     */
    public void startGame() {
        gameRunning = true;
        view.enableGameControls(true);
        view.requestFocusForPanel();
    }

    /**
     * Инициирует поиск пути от текущего состояния агента до цели,
     * используя текущий выбранный алгоритм в {@code PathFindingManager}.
     */
    public void findPath() {
        // Явное приведение типов для решения проблем с Generics
        MazeState startState = (MazeState) agent.getCurrentState();

        @SuppressWarnings("unchecked")
        Environment<MazeState, ?> environment = (Environment<MazeState, ?>) agent.getEnvironment();

        var path = pathMgr.findPath(environment, startState);

        view.showPath(path);
    }

    /**
     * Изменяет текущий алгоритм поиска пути в {@code PathFindingManager}.
     *
     * @param name Название алгоритма.
     */
    public void changeAlgorithm(String name) {
        pathMgr.setAlgorithm(name);
    }
}