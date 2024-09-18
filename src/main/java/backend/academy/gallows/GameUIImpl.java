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

    /**
     * Метод для отрисовки любого из меню
     * @param menuType - принимает на вход строку
     *                 в который определяется узел в json файле с интерфейсом
     *                 если делать для каждого меню отдельный метод то
     *                 между ними отличие будет в одну строчку
     */
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

    /**
     * точно такой же как printMenu но для эмодзи
     */
    public void printEmoji(final String emoji) {
        try {
            jsonNode = mapper.readTree(file).get(emoji);
            Arrays.stream(mapper.treeToValue(jsonNode, String[].class))
                .forEach(System.out::println);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Метод для отрисовки самого висельник, я его назвал Тони
     * какой конкретно из этап виселицы должен отрисоваться
     * определяется по формуле  (level.hitpoint() - hitPoints) * level.step()
     * для каждого уровня сложности определен свой step и hitpoint как кол-во жизней
     * @param step
     */
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

    /**
     * метод для отрисовки слова красивым образом
     */
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

    /**
     * отрисовка промежуточного результата между шагами
     */
    @Override
    public void printIntermediateResults(final int solved, final int heats) {
        System.out.println("||------------------------||");
        System.out.println("||  Решено:         " + solved + " ||");
        System.out.println("||  Осталось хп:    " + heats + " ||");
        System.out.println("||------------------------||");

    }

    /**
     * отрисовка меню выиграша
     */
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

    /**
     * отрисовка меню проигрыша
     */
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
