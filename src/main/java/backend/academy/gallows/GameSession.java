package backend.academy.gallows;

import backend.academy.gallows.model.Categories;
import backend.academy.gallows.model.Levels;
import backend.academy.gallows.model.Word;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameSession {
    //статические поля константы для того чтобы избежать "непонятных значений" в коде и сделать его более читаемым
    private static final String START_MENU = "game_menu_start";
    private static final String CATEGORIES_MENU = "game_menu_choose_category";
    private static final String LEVELS_MENU = "game_menu_choose_level";
    private static final String WIN_MENU = "game_menu_win";
    private static final String LOSE_MENU = "game_menu_lose";

    private final WordGenerator generator = new WordGenerator();
    private final GameUI ui = new GameUIImpl();

    private final String playerName;

    private Categories category;
    private Levels level;
    private Word word;
    private List<Character> characters = new ArrayList<>();
    private int hitPoints;
    private int step;

    public GameSession(final String name) {
        this.playerName = name;
    }

    /**
     * Основной метод игровой сессии
     */
    public void session() {

    }

    /**
     * Метод определяет отгадываемое слово
     *
     * @param level    - какой уровень сложности хочет игрок
     * @param category - какую тему для слов выбрал игрок
     */
    Word createWord(final Levels level, final Categories category) {
        return null;
    }

    /**
     * Метод проверяет, есть ли в слове введенная буква и сколько раз он повторяется
     */
    int[] calculateLetters(final Character letter) {
        return null;
    }

    /**
     * Метод-цикл задающий и определяющий меню, и устанваливает настройки игры
     */
    void menuCircle() {

    }
}
