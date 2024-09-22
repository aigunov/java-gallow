package backend.academy.gallows;

import backend.academy.gallows.logic.WordGenerator;
import backend.academy.gallows.model.Categories;
import backend.academy.gallows.model.Levels;
import backend.academy.gallows.model.Word;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

class WordGeneratorTest {
    private WordGenerator wordGenerator;
    private ObjectMapper mapper;
    private JsonNode jsonNodeMock;

    @BeforeEach
    void setUp() {
        mapper = Mockito.mock(ObjectMapper.class);
        wordGenerator = new WordGenerator();
        wordGenerator.mapper(mapper);

        jsonNodeMock = Mockito.mock(JsonNode.class);
    }

    @Test
    void testGenerateWord_ValidParameters() throws Exception {
        when(mapper.readTree(any(File.class))).thenReturn(jsonNodeMock);
        when(jsonNodeMock.get(any(String.class))).thenReturn(jsonNodeMock);
        when(jsonNodeMock.get(any(String.class))).thenReturn(jsonNodeMock);
        when(jsonNodeMock.get(anyInt())).thenReturn(jsonNodeMock);

        Word mockWord = new Word();
        mockWord.word("тестовое слово");
        when(mapper.treeToValue(any(JsonNode.class), eq(Word.class))).thenReturn(mockWord);

        Word generatedWord = wordGenerator.generateWord(Levels.EASY, Categories.FRUITS);

        assertNotNull(generatedWord);
        assertEquals("тестовое слово", generatedWord.word());
    }

    @Test
    void testGenerateWord_WhenCategoryOrLevelIsNull() throws Exception {
        when(mapper.readTree(any(File.class))).thenReturn(jsonNodeMock);
        when(jsonNodeMock.get(any(String.class))).thenReturn(jsonNodeMock);
        when(jsonNodeMock.get(any(String.class))).thenReturn(jsonNodeMock);
        when(jsonNodeMock.get(anyInt())).thenReturn(jsonNodeMock);

        Word mockWord = new Word();
        mockWord.word("тестовое слово");
        when(mapper.treeToValue(any(JsonNode.class), eq(Word.class))).thenReturn(mockWord);
        Word generatedWord = wordGenerator.generateWord(null, null);

        assertNotNull(generatedWord);
        assertEquals("тестовое слово", generatedWord.word());
    }

    @Test
    void testGenerateWord_WhenIOExceptionThrown() throws Exception {
        when(mapper.readTree(any(File.class))).thenThrow(new IOException("File not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            wordGenerator.generateWord(Levels.EASY, Categories.FRUITS);
        });

        assertEquals("File not found", exception.getCause().getMessage()); // Проверяем сообщение исключения
    }

    @Test
    void testGenerateWord_ValidParametersRandomized() throws Exception {
        for (Levels level : Levels.values()) {
            for (Categories category : Categories.values()) {
                when(mapper.readTree(any(File.class))).thenReturn(jsonNodeMock);
                when(jsonNodeMock.get(any(String.class))).thenReturn(jsonNodeMock);
                when(jsonNodeMock.get(any(String.class))).thenReturn(jsonNodeMock);
                when(jsonNodeMock.get(anyInt())).thenReturn(jsonNodeMock);

                Word mockWord = new Word();
                mockWord.word("слово для проверки");
                when(mapper.treeToValue(any(JsonNode.class), eq(Word.class))).thenReturn(mockWord);

                Word generatedWord = wordGenerator.generateWord(level, category);

                assertNotNull(generatedWord);
                assertEquals("слово для проверки", generatedWord.word());
            }
        }
    }
}
