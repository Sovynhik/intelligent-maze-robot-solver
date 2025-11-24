package ru.rsreu.savushkin.mazerobot.core.state.maze;

import ru.rsreu.savushkin.mazerobot.core.state.Action;

/**
 * Класс-запись (Record Class), представляющий действие перемещения робота в лабиринте.
 * <p>Действие определяется смещением по X (dx), смещением по Y (dy) и флагом,
 * указывающим на двойной шаг (прыжок).</p>
 */
public record MoveAction(int dx, int dy, boolean isDouble) implements Action {
    /**
     * Возвращает удобочитаемое имя действия (например, "Up", "Jump Left").
     *
     * @return Имя действия на английском языке.
     */
    @Override
    public String getName() {
        String dir = (dx == 0 && dy == -1) ? "Up" :
                (dx == 0 && dy == 1)  ? "Down" :
                        (dx == -1 && dy == 0) ? "Left" :
                                (dx == 1 && dy == 0)  ? "Right" : "Unknown";
        return isDouble ? "Jump " + dir : dir;
    }
}