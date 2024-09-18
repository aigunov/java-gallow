package backend.academy.gallows;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({ "RegexpSinglelineJava", "MultipleStringLiterals"})
@Slf4j
public class GameUIImpl implements GameUI {
    private static final String NODE_OF_JSON_FILE = "game_menus";
    private final ObjectMapper mapper = new ObjectMapper();
    private final File file = new File("src/main/resources/ui.json");
    private JsonNode jsonNode;

    public void printMenu(final String menuType) {
        try {
            jsonNode = mapper.readTree(file).get(NODE_OF_JSON_FILE).get(menuType);
            Arrays.stream(mapper.treeToValue(jsonNode, String[].class))
                .forEach(System.out::println);
        } catch (IOException e) {
            var mes = e.getMessage();
            log.error(mes);
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

        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            boolean found = false;

            for (Character letter : letters) {
                if (currentChar == letter) {
                    found = true;
                    break;
                }
            }

            if (found) {
                output.append(Character.toUpperCase(currentChar))
                    .append(" ");
            } else {
                output.append("_ ");
            }
        }

        System.out.println(output.toString().trim());
    }

    @Override
    public void printIntermediateResults(final int solved, final int heats) {
        System.out.println("||------------------------||");
        System.out.println("||  Решено:         " + solved + " ||");
        System.out.println("||  Осталось хп:    " + heats + " ||");
        System.out.println("||------------------------||");

    }

    @Override
    public void printGameWin() {
        try {
            jsonNode = mapper.readTree(file).get(NODE_OF_JSON_FILE).get("game_menu_win");
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
            jsonNode = mapper.readTree(file).get(NODE_OF_JSON_FILE).get("game_menu_loose");
            Arrays.stream(mapper.treeToValue(jsonNode, String[].class))
                .forEach(System.out::println);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        printEmoji("goodbye_smiley");
    }
}
