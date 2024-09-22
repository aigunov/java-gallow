package backend.academy.gallows;

import backend.academy.gallows.model.Categories;
import backend.academy.gallows.model.Levels;
import backend.academy.gallows.model.Word;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Класс для извлечения из dictionary.json нужного слова по переданнам конфигурациям
 * если конфигурации не заданы, то слово подбирается случайным образом
 */
@SuppressWarnings({"RegexpSinglelineJava", "MagicNumber", "ParameterAssignment"})
@Getter
@Setter
@Slf4j
public final class WordGenerator {
    private ObjectMapper mapper = new ObjectMapper();
    private Word word;

    /**
     * главная задача метода - вернуть word для отгадывания
     * @param level - какой уровень сложности хочет игрок
     * @param category - какую категорию выбрал игрок
     * @return word
     */
    public Word generateWord(Levels level, Categories category) {
        var random = new Random();
        int num = random.nextInt(3);
        int wordNum = random.nextInt(5);
        level = level == null ? Levels.values()[num] : level;
        category = category == null ? Categories.values()[num] : category;

        var file = new File("src/main/resources/dictionary.json");
        try {
            var jsonNode = mapper.readTree(file);
            jsonNode = jsonNode.get(category.name().toLowerCase()).get(level.name().toLowerCase()).get(wordNum);
            word = mapper.treeToValue(jsonNode, Word.class);
            word.levels(level);
            word.category(category);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("Generated word: {}", word);
        return word;
    }

}
