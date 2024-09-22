package backend.academy.gallows.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({"RegexpSinglelineJava"})
@Slf4j
public final class GameUIImpl implements GameUI {
    private static final String NODE_OF_JSON_FILE = "game_menus";
    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonNode rootNode;
    private static GameUI instance;

    private GameUIImpl() throws IOException {
        var file = new File("src/main/resources/ui.json");
        rootNode = mapper.readTree(file);
    }

    public static GameUI getInstance() {
        if (instance == null) {
            try {
                return new GameUIImpl();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    private void printFromNode(final JsonNode node) {
        try {
            Arrays.stream(mapper.treeToValue(node, String[].class))
                .forEach(System.out::println);
        } catch (JsonProcessingException e) {
            log.error("Возникла ошибка при чтениие json файла: {}", e.getMessage());
            System.err.println("Не удалось загрузить меню. Пожалуйста, перезапустите приложение.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для отрисовки любого из меню
     * @param menuType - принимает на вход строку
     *                 в который определяется узел в json файле с интерфейсом
     *                 если делать для каждого меню отдельный метод то
     *                 между ними отличие будет в одну строчку
     */
    public void printMenu(final String menuType) {
        JsonNode menuNode = rootNode.get(NODE_OF_JSON_FILE).get(menuType);
        printFromNode(menuNode); // Используйте printFromNode
    }

    /**
     * точно такой же как printMenu но для эмодзи
     */
    public void printEmoji(final String emoji) {
        JsonNode emojiNode = rootNode.get(emoji);
        printFromNode(emojiNode);
    }

    /**
     * Метод для отрисовки самого висельник, я его назвал Тони
     * какой конкретно из этап виселицы должен отрисоваться
     * определяется по формуле  (level.hitpoint() - hitPoints) * level.step()
     * для каждого уровня сложности определен свой step и hitpoint как кол-во жизней
     */
    @Override
    public void printTony(final int step) {
        JsonNode tonyNode = rootNode.get("hangman_stages").get(String.valueOf(step));
        printFromNode(tonyNode);
    }

    /**
     * метод для отрисовки слова красивым образом
     */
    @Override
    public void printWord(final List<Character> letters, final String word) {
        StringBuilder output = new StringBuilder();

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
        System.out.printf("""
            ||------------------------||
            ||  Решено:         {} ||
            ||  Осталось хп:    {} ||
            ||------------------------||
            \n""", solved, heats);

    }
}
