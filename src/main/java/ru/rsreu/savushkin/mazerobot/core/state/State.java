package ru.rsreu.savushkin.mazerobot.core.state;

/**
 * Интерфейс состояния системы.
 * Должен реализовывать equals/hashCode для корректного сравнения ситуаций.
 */
public interface State {
    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();
}