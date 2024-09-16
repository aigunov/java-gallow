package backend.academy.gallows;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameUIImpl implements GameUI {
    private final ObjectMapper mapper = new ObjectMapper();
    private final File file = new File("src/main/resources/ui.json");
    private JsonNode jsonNode;

    public void printMenu(final String menuType) {
        try {
            jsonNode = mapper.readTree(file).get("game_menus").get(menuType);
            Arrays.stream(mapper.treeToValue(jsonNode, String[].class))
                .forEach(System.out::println);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void printEmoji(final String emoji) {
        try {
            jsonNode = mapper.readTree(file).get(emoji);
            Arrays.stream(mapper.treeToValue(jsonNode, String[].class))
                .forEach(System.out::println);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void printTony(final int step) {
        try {
            jsonNode = mapper.readTree(file).get("hangman_stages").get(String.valueOf(step));
            Arrays.stream(mapper.treeToValue(jsonNode, String[].class))
                .forEach(System.out::println);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void printWord(final List<Character> letters, final String word) {
        StringBuilder output = new StringBuilder(); // Используем StringBuilder для построения строки результата

        // Перебираем буквы в слове
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i); // Получаем текущую букву из слова
            boolean found = false; // Флаг для проверки наличия буквы в letters

            // Проверяем, есть ли текущая буква в массиве letters
            for (Character letter : letters) {
                if (currentChar == letter) {
                    found = true; // Если текущая буква найдена в letters
                    break; // Прерываем внутренний цикл, так как буква найдена
                }
            }

            // Если буква найдена, добавляем её в выходную строку, иначе добавляем "_"
            if (found) {
                output.append(Character.toUpperCase(currentChar))
                    .append(" "); // Добавляем текущую букву в верхнем регистре
            } else {
                output.append("_ "); // Добавляем подчеркивание
            }
        }

        System.out.println(output.toString().trim()); // Удаляем последний пробел и выводим
    }

    @Override
    public void printIntermediateResults(final int solved, final int heats) {
        System.out.println("||------------------------||");
        System.out.println("||  Решено:         " + solved + " ||");
        System.out.println("||  Осталось хп:    " + heats + " ||");
        System.out.println("||------------------------||");

    }

    @Override
    public void printGameWin(final String name) {
        try {
            jsonNode = mapper.readTree(file).get("game_menus").get("game_menu_win");
            Arrays.stream(mapper.treeToValue(jsonNode, String[].class))
                .forEach(System.out::println);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        printEmoji("happy_smiley");
    }

    @Override
    public void printGameLost() {
        try {
            jsonNode = mapper.readTree(file).get("game_menus").get("game_menu_loose");
            Arrays.stream(mapper.treeToValue(jsonNode, String[].class))
                .forEach(System.out::println);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        printEmoji("goodbye_smiley");
    }
}
