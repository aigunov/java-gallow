package backend.academy.gallows.logic;

import backend.academy.gallows.model.Categories;
import backend.academy.gallows.model.Levels;
import backend.academy.gallows.model.Word;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Класс для извлечения из dictionary.json нужного слова по переданнам конфигурациям
 * если конфигурации не заданы, то слово подбирается случайным образом
 */
@SuppressWarnings({"RegexpSinglelineJava", "MagicNumber"})
@Getter
@Setter
@Slf4j
public final class WordGenerator {
    private final static Integer NUMBER_FOR_CONFIGS = 3;
    private final static Integer NUMBER_FOR_WORDS = 5;

    private ObjectMapper mapper = new ObjectMapper();
    private Word word;

    /**
     * главная задача метода - вернуть word для отгадывания
     *
     * @param levelArg    - какой уровень сложности хочет игрок
     * @param categoryArg - какую категорию выбрал игрок
     * @return word
     */
    public Word generateWord(Levels levelArg, Categories categoryArg) throws IOException {
        var random = new Random();
        int num = random.nextInt(NUMBER_FOR_CONFIGS);
        int wordNum = random.nextInt(NUMBER_FOR_WORDS);
        Levels level = (levelArg == null || levelArg == Levels.RANDOM)
            ? Levels.values()[num] : levelArg;
        Categories category = (categoryArg == null || categoryArg == Categories.RANDOM)
            ? Categories.values()[num] : categoryArg;

        try (var inputStream = WordGenerator.class.getResourceAsStream("/dictionary.json")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Файл dictionary.json не найден");
            }
            try {
                var jsonNode = mapper.readTree(inputStream);
                jsonNode = jsonNode.get(category.name().toLowerCase()).get(level.name().toLowerCase()).get(wordNum);
                word = mapper.treeToValue(jsonNode, Word.class);
                word.levels(level);
                word.category(category);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Generated word: {}", word);
        return word;
    }

}
