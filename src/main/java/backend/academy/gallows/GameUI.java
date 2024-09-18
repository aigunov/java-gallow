package backend.academy.gallows;

import java.util.List;

/**
 * Интерфейс определяющий методы отрисовки игрового интерфейса
 * сделано через интерфейс для дальнейшего расширения
 * вдруг я захочу сделать вывод через графику
 * ну и меня учили что для такого рода логики надо делать интерфейсы
 */
public interface GameUI {
    void printTony(int step);

    void printWord(List<Character> letters, String word);

    void printIntermediateResults(int solved, int heats);

    void printGameWin();

    void printGameLost();

    void printEmoji(String emojiType);

    void printMenu(String menuType);
}
