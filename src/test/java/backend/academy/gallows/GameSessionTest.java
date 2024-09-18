package backend.academy.gallows;

import backend.academy.gallows.model.Categories;
import backend.academy.gallows.model.Levels;
import backend.academy.gallows.model.Word;
import backend.academy.gallows.model.WordFromDictionaryNotValid;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameSessionTest {
    private GameSession gameSession;
    private WordGenerator generator;
    private Word word;

    @BeforeEach
    void setUp() {
        word = mock(Word.class);
        generator = mock(WordGenerator.class);
        gameSession = new GameSession("bruh");
        gameSession.word(word);
    }

    @Test
    void testValidateLetter_WhenInputIsMoreThanOneCharacter() {
        assertFalse(gameSession.validateLetter("ab"));
    }

    @Test
    void testValidateLetter_WhenInputIsOneCharacterAndIsOne() {
        when(word.didUseHintAlready()).thenReturn(false);
        when(word.hint()).thenReturn("Hint Example");

        boolean result = gameSession.validateLetter("1");

        assertFalse(result);
        verify(word, times(1)).hint();
        verify(word, times(1)).didUseHintAlready(true);
    }

    @Test
    void testValidateLetter_WhenInputIsOneCharacterAndHintAlreadyUsed() {
        when(word.didUseHintAlready()).thenReturn(true);

        boolean result = gameSession.validateLetter("1");

        assertFalse(result);
        verify(word, never()).hint();
        verify(word, never()).didUseHintAlready(anyBoolean());
    }

    @Test
    void testValidateLetter_WhenInputIsNotALetter() {
        boolean result = gameSession.validateLetter("1$");

        assertFalse(result);
    }

    @Test
    void testValidateLetter_WhenLetterAlreadyUsed() {
        gameSession.characters().add('а');
        boolean result = gameSession.validateLetter("а");

        assertFalse(result);
    }

    @Test
    void testValidateLetter_WhenInputIsLowerCaseLatinLetter() {
        boolean result = gameSession.validateLetter("a");

        assertFalse(result);
    }

    @Test
    void testValidateLetter_WhenInputIsValidRussianLetter() {
        boolean result = gameSession.validateLetter("а");

        assertTrue(result);
    }

    @Test
    void testCreateWord_EmptyWord() {
        Word mockWord = mock(Word.class);
        gameSession.generator(generator);
        when(mockWord.word()).thenReturn("");
        when(generator.generateWord(any(Levels.class), any(Categories.class))).thenReturn(mockWord);

        assertThrows(WordFromDictionaryNotValid.class, () -> {
            gameSession.createWord(Levels.EASY, Categories.FRUITS);
        });
    }

    @Test
    void testCreateWord_BlankWord() {
        Word mockWord = mock(Word.class);
        gameSession.generator(generator);
        when(mockWord.word()).thenReturn("   ");
        when(generator.generateWord(any(Levels.class), any(Categories.class))).thenReturn(mockWord);

        assertThrows(WordFromDictionaryNotValid.class, () -> {
            gameSession.createWord(Levels.EASY, Categories.FRUITS);
        });
    }

    @Test
    void testCreateWord_InvalidCharacters() {
        Word mockWord = mock(Word.class);
        gameSession.generator(generator);
        when(mockWord.word()).thenReturn("Слово123");
        when(generator.generateWord(any(Levels.class), any(Categories.class))).thenReturn(mockWord);

        assertThrows(WordFromDictionaryNotValid.class, () -> {
            gameSession.createWord(Levels.EASY, Categories.FRUITS);
        });
    }

    @Test
    void testCreateWord_UpperCaseLetter() {
        Word mockWord = mock(Word.class);
        gameSession.generator(generator);
        when(generator.generateWord(any(Levels.class), any(Categories.class))).thenReturn(mockWord);
        when(mockWord.word()).thenReturn("СЛОВО");

        assertThrows(WordFromDictionaryNotValid.class, () -> {
            gameSession.createWord(Levels.EASY, Categories.FRUITS);
        });
    }

    @Test
    void testCreateWord_ValidWord() {
        Word mockWord = mock(Word.class);
        gameSession.generator(generator);
        when(mockWord.word()).thenReturn("слово");
        when(generator.generateWord(any(Levels.class), any(Categories.class))).thenReturn(mockWord);

        Word result = gameSession.createWord(Levels.EASY, Categories.FRUITS);

        assertEquals(mockWord, result);
    }
}
