package ru.rsreu.savushkin.mazerobot.core.state.maze;

import ru.rsreu.savushkin.mazerobot.core.state.State;

/**
 * Класс-запись (Record Class), представляющий состояние робота в лабиринте.
 * <p>Состояние определяется координатами робота (x, y).</p>
 * <p>Имплементирует интерфейс {@code State}, делая его пригодным для алгоритмов поиска.</p>
 */
public record MazeState(int x, int y) implements State {
    /**
     * Возвращает строковое представление состояния в формате (x, y).
     * @return Координаты состояния в виде строки.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}