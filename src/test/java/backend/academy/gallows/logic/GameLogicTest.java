package backend.academy.gallows.logic;

import backend.academy.gallows.model.Categories;
import backend.academy.gallows.model.GamePlayParameters;
import backend.academy.gallows.model.Levels;
import backend.academy.gallows.model.Word;
import backend.academy.gallows.model.WordFromDictionaryNotValid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameLogicTest {
    private Word word;
    private WordGenerator generator;
    private GameLogic gameLogic;

    @BeforeEach
    void setUp() {
        word = mock(Word.class);
        generator = mock(WordGenerator.class);
        gameLogic = GameLogic.getInstance();
        gameLogic.word(word);
    }

    @Test
    void testValidateLetter_WhenInputIsMoreThanOneCharacter() {
        assertFalse(gameLogic.validateLetter("ab"));
    }

    @Test
    void testValidateLetter_WhenInputIsOneCharacterAndIsOne() {
        when(word.didUseHintAlready()).thenReturn(false);
        when(word.hint()).thenReturn("Hint Example");

        boolean result = gameLogic.validateLetter("1");

        assertFalse(result);
        verify(word, times(1)).hint();
        verify(word, times(1)).didUseHintAlready(true);
    }

    @Test
    void testValidateLetter_WhenInputIsOneCharacterAndHintAlreadyUsed() {
        when(word.didUseHintAlready()).thenReturn(true);

        boolean result = gameLogic.validateLetter("1");

        assertFalse(result);
        verify(word, never()).hint();
        verify(word, never()).didUseHintAlready(anyBoolean());
    }

    @Test
    void testValidateLetter_WhenInputIsNotALetter() {
        boolean result = gameLogic.validateLetter("1$");

        assertFalse(result);
    }

    @Test
    void testValidateLetter_WhenLetterAlreadyUsed() {
        gameLogic.characters().add('н');
        boolean result = gameLogic.validateLetter("н");

        assertFalse(result);
    }

    @Test
    void testValidateLetter_WhenInputIsLowerCaseLatinLetter() {
        boolean result = gameLogic.validateLetter("a");

        assertFalse(result);
    }

    @Test
    void testValidateLetter_WhenInputIsValidRussianLetter() {
        boolean result = gameLogic.validateLetter("а");

        assertTrue(result);
    }

    @Test
    void testCreateWord_EmptyWord() {
        Word mockWord = mock(Word.class);
        gameLogic.generator(generator);
        var params = GamePlayParameters.builder()
            .level(Levels.EASY)
            .category(Categories.FRUITS)
            .build();

        when(mockWord.word()).thenReturn("");
        when(generator.generateWord(any(Levels.class), any(Categories.class))).thenReturn(mockWord);

        assertThrows(WordFromDictionaryNotValid.class, () -> {
            gameLogic.createWord(params);
        });
    }

    @Test
    void testCreateWord_BlankWord() {
        Word mockWord = mock(Word.class);
        gameLogic.generator(generator);
        var params = GamePlayParameters.builder()
            .level(Levels.EASY)
            .category(Categories.FRUITS)
            .build();

        when(mockWord.word()).thenReturn("   ");
        when(generator.generateWord(any(Levels.class), any(Categories.class))).thenReturn(mockWord);

        assertThrows(WordFromDictionaryNotValid.class, () -> {
            gameLogic.createWord(params);
        });
    }

    @Test
    void testCreateWord_InvalidCharacters() {
        Word mockWord = mock(Word.class);
        gameLogic.generator(generator);
        var params = GamePlayParameters.builder()
            .level(Levels.EASY)
            .category(Categories.FRUITS)
            .build();

        when(mockWord.word()).thenReturn("Слово123");
        when(generator.generateWord(any(Levels.class), any(Categories.class))).thenReturn(mockWord);

        assertThrows(WordFromDictionaryNotValid.class, () -> {
            gameLogic.createWord(params);
        });
    }

    @Test
    void testCreateWord_UpperCaseLetter() {
        Word mockWord = mock(Word.class);
        gameLogic.generator(generator);
        var params = GamePlayParameters.builder()
            .level(Levels.EASY)
            .category(Categories.FRUITS)
            .build();

        when(generator.generateWord(any(Levels.class), any(Categories.class))).thenReturn(mockWord);
        when(mockWord.word()).thenReturn("СЛОВО");

        assertThrows(WordFromDictionaryNotValid.class, () -> {
            gameLogic.createWord(params);
        });
    }

    @Test
    void testCreateWord_ValidWord() {
        Word mockWord = mock(Word.class);
        gameLogic.generator(generator);
        var params = GamePlayParameters.builder()
            .level(Levels.EASY)
            .category(Categories.FRUITS)
            .build();

        when(mockWord.word()).thenReturn("слово");
        when(mockWord.levels()).thenReturn(Levels.EASY);
        when(mockWord.category()).thenReturn(Categories.FRUITS);
        when(generator.generateWord(any(Levels.class), any(Categories.class))).thenReturn(mockWord);

        var result = gameLogic.createWord(params);

        assertEquals(mockWord, result.word());
    }
}
