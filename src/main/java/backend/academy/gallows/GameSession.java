package backend.academy.gallows;

import backend.academy.gallows.model.Categories;
import backend.academy.gallows.model.Levels;
import backend.academy.gallows.model.Word;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameSession {
    private final String playerName;

    private Categories category;
    private Levels level;
    private Word word;

    private int hitPoints;

    public GameSession(final String name) {
        this.playerName = name;
    }

    /**
     * Основной метод игровой сессии
     */
    public void session(){

    }

    /**
     * Метод определяет отгадываемое слово
     * @param level - какой уровень сложности хочет игрок
     * @param category - какую тему для слов выбрал игрок
     * @return
     */
    Word createWord(final Levels level, final Categories category){
        return null;
    }

    /**
     * Метод проверяет, есть ли в слове введенная буква и сколько раз он повторяется
     * @param letter
     * @return
     */
    int[] calculateLetters(final Character letter){
        return null;
    }

    /**
     * Метод-цикл задающий и определяющий меню, и устанваливает настройки игры
     */
    void menuCircle(){

    }
}
