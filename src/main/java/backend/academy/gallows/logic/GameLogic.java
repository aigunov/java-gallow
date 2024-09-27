package backend.academy.gallows.logic;

import backend.academy.gallows.model.GamePlayParameters;
import backend.academy.gallows.model.GameResults;
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
 * Singleton класс который ответственнен
 * за всю игровую логику каждой сессии
 * он валидирует введеные символы
 * генерирует слова и проверяет
 * отгадана ли буква из слова
 */
@SuppressWarnings({"RegexpSinglelineJava"})
@Getter
@Setter
@Slf4j
public class GameLogic {
    private static GameLogic instance;
    private WordGenerator generator = new WordGenerator();
    private List<Character> characters = new ArrayList<>();
    private final Scanner sc = new Scanner(System.in);
    private GamePlayParameters params = new GamePlayParameters();
    private Word word;
    private int guessed;
    private int mistakes;
    private int gallowsStage;

    private GameLogic() {
    }

    public static GameLogic getInstance() {
        if (instance == null) {
            instance = new GameLogic();
        }
        return instance;
    }

    /**
     * в середине каждого раунда повторяется
     * одна и та же логика по введению символа
     * проверки его на валидность
     * и проверки на совпадения
     * по сути это и есть главный метод класса
     */
    public GamePlayParameters roundMiddle() {
        System.out.println("Введите символ. ");
        if (!word.didUseHintAlready()) {
            System.out.println("Если вы хотите воспользоваться посказкой введите \"1\":");
        }
        var letter = sc.next().toLowerCase();
        while (!validateLetter(letter)) {
            letter = sc.next().toLowerCase();
        }
        calculateLettersConstraint(letter.charAt(0));
        characters.add(letter.charAt(0));
        params.characters(characters).step(gallowsStage);
        return params;
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
            char charLetter = letter.charAt(0);
            if (!Character.isLetter(charLetter)) {
                System.out.println("Вы должны ввести именно букву");
            } else if (characters.contains(charLetter)) {
                System.out.println("Вы уже вводили эту букву");
            } else if (!(charLetter >= 'а' && charLetter <= 'я')) {
                System.out.println("Вы можете вводить исключительно русские буквы");
            } else {
                log.info("the Letter is absolutely valid and correct");
                return true;
            }
        }
        return false;
    }

    /**
     * Метод проверяет, есть ли в слове введенная буква и сколько раз он повторяется
     */
    public void calculateLettersConstraint(final Character letter) {
        if (word.word().contains(letter.toString())) {
            System.out.println("Вы угадали символ! \uD83D\uDC4D");
            Matcher matcher = Pattern.compile(String.valueOf(letter)).matcher(word.word());
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            System.out.println("Вы угадали " + count + " симоволов");
            log.info("Игрок отгадал {} символов ✨", count);
            guessed += count;
        } else {
            System.out.println("Увы и ах, вы ошиблись \uD83D\uDE2D");
            mistakes--;
            gallowsStage = (word().levels().hitpoint() - mistakes) * word().levels().step();
            log.info("Игрок ошибся, mistakes: {}, gallowsStage: {}", mistakes, gallowsStage);
        }
    }

    public GameResults checkResult() {
        if (guessed == word.word().length()) {
            return GameResults.WIN;
        } else if (mistakes == 0) {
            return GameResults.LOSE;
        }
        return GameResults.IN_PROGRESS;
    }

    /**
     * Метод определяет отгадываемое слово
     * и проверяет что подобранное слово - валидно
     */
    public GamePlayParameters createWord(final GamePlayParameters parameters) {
        // TODO добавить проверку на валидность слов
        var generated = generator.generateWord(parameters.level(), parameters.category());
        if (generated.word().isBlank() || generated.word().isEmpty()
            || !Pattern.matches("[а-яА-Я]+", generated.word())
            || !generated.word().toLowerCase().equals(generated.word())) {
            throw new WordFromDictionaryNotValid("кто-то умный добавил неправильные слова в словарь");
        }
        word = generated;
        mistakes = word.levels().hitpoint();
        log.info("Params: {}", params);
        params = GamePlayParameters.builder()
            .word(word)
            .level(generated.levels())
            .category(generated.category())
            .characters(List.of())
            .build();
        return params;
    }
}
