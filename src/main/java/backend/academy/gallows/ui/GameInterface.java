package backend.academy.gallows.ui;

import backend.academy.gallows.model.Categories;
import backend.academy.gallows.model.GamePlayParameters;
import backend.academy.gallows.model.Levels;
import java.io.IOException;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

/**
 * Singleton класс ответсвенный за все
 * взаимодействие с пользователем
 * он вызывает нужные методы класса GameUIImpl
 */
@SuppressWarnings({"RegexpSinglelineJava", "MagicNumber"})
@Slf4j
public class GameInterface {
    private static final String START_MENU = "game_menu_start";
    private static final String CATEGORIES_MENU = "game_menu_choose_category";
    private static final String LEVELS_MENU = "game_menu_choose_level";
    private static final String WIN_MENU = "game_menu_win";
    private static final String LOSE_MENU = "game_menu_loose";
    private static final String HAPPY_EMOJI = "happy_smiley";
    private static final String SAD_EMOJI = "goodbye_smiley";
    private static final String DEAD_EMOJI = "dead_smiley";
    private static GameInterface instance;
    private final GameUI ui = GameUIImpl.getInstance();
    private final Scanner sc = new Scanner(System.in);

    private GameInterface() throws IOException {
    }

    public static GameInterface getInstance() throws IOException {
        if (instance == null) {
            instance = new GameInterface();
        }
        return instance;
    }

    /**
     * Метод-цикл задающий и определяющий меню, и устанваливает настройки игры
     */
    public GamePlayParameters menuCircle(final String playerName) {
        Categories category;
        Levels level;

        var wrongVariantInput = "Пожалуйста введите один из представленных вариантов";
        ui.printEmoji(HAPPY_EMOJI);
        while (true) {
            ui.printMenu(START_MENU);
            int input = sc.nextInt();
            if (input == 1) {
                break;
            } else if (input == 2) {
                ui.printEmoji(SAD_EMOJI);
                System.exit(0);
            } else {
                System.out.println("Такого варианта ответа нет, пожалуйста введите подходящий");
            }
        }
        while (true) {
            ui.printMenu(CATEGORIES_MENU);
            int input = sc.nextInt();
            category = switch (input) {
                case 1 -> Categories.FRUITS;
                case 2 -> Categories.ANIMALS;
                case 3 -> Categories.PROFESSIONS;
                case 4 -> Categories.RANDOM;
                default -> {
                    System.out.println(wrongVariantInput);
                    yield null;
                }
            };
            if (category != null) {
                System.out.println("Отличный выбор " + playerName);
                break;
            }
        }

        while (true) {
            ui.printMenu(LEVELS_MENU);
            int input = sc.nextInt();
            level = switch (input) {
                case 1 -> Levels.EASY;
                case 2 -> Levels.MIDDLE;
                case 3 -> Levels.HARD;
                case 4 -> Levels.RANDOM;
                default -> {
                    System.out.println(wrongVariantInput);
                    yield null;
                }
            };
            if (level != null) {
                System.out.println("Вы запроста справитесь " + playerName);
                break;
            }
        }
        log.info("Category: {}; Level: {}", category, level);

        return GamePlayParameters.builder()
            .level(level)
            .category(category)
            .build();

    }

    /**
     * Метод для вывода в консоль первого этапа
     * который повторяется в каждом раунде
     */
    public void roundBeginning(final GamePlayParameters parameters) {
        System.out.println("ИГРА НАЧАЛАСЬ\n");
        ui.printTony(parameters.step() + 1);
        System.out.print("Тематика слова: " + parameters.category().name() + ".      ");
        ui.printWord(parameters.characters(), parameters.word().word());

    }

    /**
     * Метод для вывод в консоль последнего этапа
     * который повторяется в конце каждого раунда
     */
    public void roundEnding(final GamePlayParameters parameters) {
        ui.printWord(parameters.characters(), parameters.word().word());
        ui.printIntermediateResults(parameters.solved(), parameters.hits());
        System.out.println();
    }

    /**
     * Отрисовка меню Победы
     */
    public void winMenu(final String playerName) {
        System.out.println("ПОЗДРАВЛЯЕМ ВАС " + playerName + " ВЫ ПОБЕДИЛИ!!!");
        System.out.println("\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89");
        ui.printMenu(WIN_MENU);
    }

    /**
     * Отрисовка меню поражения
     */
    public void loseMenu(final String playerName) {
        ui.printEmoji(DEAD_EMOJI);
        System.out.println("К СОЖАЛЕНИЕ ВЫ " + playerName + " ПРОИГРАЛИ");
        System.out.println("\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D");
        ui.printMenu(LOSE_MENU);
    }

}
