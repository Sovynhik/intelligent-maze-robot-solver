package ru.rsreu.savushkin.mazerobot.core.model;

import ru.rsreu.savushkin.mazerobot.core.entity.Event;

/**
 * Интерфейс слушателя (Listener) в паттерне Наблюдатель (Observer).
 * <p>Предназначен для объектов, которые должны быть уведомлены о событиях (изменениях)
 * в модели (например, в состоянии робота или лабиринта).</p>
 */
public interface Listener {
    /**
     * Обрабатывает полученное событие.
     *
     * @param event Объект события, содержащий информацию о произошедшем изменении.
     */
    void handle(Event event);
}