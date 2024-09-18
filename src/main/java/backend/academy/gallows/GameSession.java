package backend.academy.gallows;

import backend.academy.gallows.model.Categories;
import backend.academy.gallows.model.Levels;
import backend.academy.gallows.model.Word;
import backend.academy.gallows.model.WordFromDictionaryNotValid;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Главный класс для всего проекта в котором и происходит весь игровой процесс
 * Этот класс вызывает функционал всех остальных и запускает игру
 * начиная с метода session()
 */
@SuppressWarnings({"RegexpSinglelineJava", "MagicNumber"})
@Getter
@Setter
@Slf4j
public final class GameSession {
    //статические поля константы для того чтобы избежать "непонятных значений" в коде и сделать его более читаемым
    private static final String START_MENU = "game_menu_start";
    private static final String CATEGORIES_MENU = "game_menu_choose_category";
    private static final String LEVELS_MENU = "game_menu_choose_level";
    private static final String WIN_MENU = "game_menu_win";
    private static final String LOSE_MENU = "game_menu_lose";

    private static final String HAPPY_EMOJI = "happy_smiley";
    private static final String SAD_EMOJI = "goodbye_smiley";
    private static final String DEAD_EMOJI = "dead_smiley";

    private final GameUI ui = new GameUIImpl();
    private final String playerName;
    private WordGenerator generator = new WordGenerator();
    private Scanner sc;
    private Categories category;
    private Levels level;
    private Word word;
    private List<Character> characters = new ArrayList<>();
    private int hitPoints;
    private int step = 0;
    private int hits = 0;
    private boolean didWin = false;

    public GameSession(final String name) {
        this.playerName = name;
        this.sc = new Scanner(System.in);
    }

    /**
     * Основной метод игровой сессии
     * тут происходит вся игра
     */
    public void session() {
        menuCircle();
        word = createWord(level, category);
        level = word.levels();
        category = word.category();
        hitPoints = level.hitpoint();
        System.out.println("ИГРА НАЧАЛАСЬ\n");
        while (hitPoints > 0 && !didWin) {
            ui.printTony(step + 1);
            System.out.print("Тематика слова: " + category.name() + ".      ");
            ui.printWord(characters, word.word());
            System.out.println("Введите символ. ");
            if (!word.didUseHintAlready()) {
                System.out.println("Если вы хотите воспользоваться посказкой введите \"1\":");
            }
            var letter = sc.next().toLowerCase();
            log.info("user input: {}", letter);
            if (!validateLetter(letter)) {
                continue;
            }
            calculateLettersConstraint(letter.charAt(0));
            characters.add(letter.charAt(0));
            ui.printWord(characters, word.word());
            System.out.println();
        }
        if (didWin) {
            System.out.println("ПОЗДРАВЛЯЕМ ВАС " + playerName + " ВЫ ПОБЕДИЛИ!!!");
            System.out.println("\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89");
            ui.printGameWin();
        } else {
            ui.printEmoji(DEAD_EMOJI);
            System.out.println("К СОЖАЛЕНИЕ ВЫ " + playerName + " ПРОИГРАЛИ");
            System.out.println("\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D");
            ui.printGameLost();
        }
    }

    /**
     * Метод для валидации введенной строки
     *
     * @return true или false в зависимости от строки
     */
    boolean validateLetter(final String letter) {
        if (letter.length() != 1) {
            System.out.println("Вы должны ввести только один символ а не строку");
            return false;
        }

        if (letter.charAt(0) == '1') {
            if (!word.didUseHintAlready()) {
                System.out.println("Подсказка: " + word.hint() + "\n");
                word.didUseHintAlready(true);
            } else {
                System.out.println("Вы уже использовали подсказку");
            }
        } else {
            if (!Character.isLetter(letter.charAt(0))) {
                System.out.println("Вы должны ввести именно букву");
            } else if (characters.contains(letter.charAt(0))) {
                System.out.println("Вы уже вводили эту букву");
            } else if (letter.charAt(0) >= 'a' && letter.charAt(0) <= 'z') {
                System.out.println("Вы можете вводить исключительно русские буквы");
            } else {
                log.info("the Letter is absolutely valid and correct");
                return true;
            }
        }
        return false;
    }

    /**
     * Метод определяет отгадываемое слово
     * и проверяет что подобранное слово - валидно
     * @param level    - какой уровень сложности хочет игрок
     * @param category - какую тему для слов выбрал игрок
     */
    Word createWord(final Levels level, final Categories category) {
        // TODO добавить проверку на валидность слов
        var generated = generator.generateWord(level, category);
        if (generated.word().isBlank() || generated.word().isEmpty()
            || !Pattern.matches("[а-яА-Я]+", generated.word())
            || !generated.word().toLowerCase().equals(generated.word())) {
            throw new WordFromDictionaryNotValid("кто-то умный добавил неправильные слова в словарь");
        }
        return generated;
    }

    /**
     * Метод проверяет, есть ли в слове введенная буква и сколько раз он повторяется
     */
    void calculateLettersConstraint(final Character letter) {
        if (word.word().contains(letter.toString())) {
            System.out.println("Вы угадали символ! \uD83D\uDC4D");
            Matcher matcher = Pattern.compile(String.valueOf(letter)).matcher(word.word());
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            System.out.println("Вы угадали " + count + " симоволов");
            log.info("Игрок отгадал {} символов ✨", count);
            hits += count;
            didWin = hits == word.word().length();
        } else {
            System.out.println("Увы и ах, вы ошиблись \uD83D\uDE2D");
            hitPoints--;
            step = (level.hitpoint() - hitPoints) * level.step();
            log.info("Игрок ошибся, hitpoints: {}, step: {}", hitPoints, step);
        }
    }

    /**
     * Метод-цикл задающий и определяющий меню, и устанваливает настройки игры
     */
    void menuCircle() {
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
                System.out.println("Отличный выбор сэр " + playerName);
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
                System.out.println("Вы запроста справитесь сэр " + playerName);
                break;
            }
        }
    }
}
