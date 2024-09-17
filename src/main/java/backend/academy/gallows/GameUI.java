package backend.academy.gallows;

import java.util.List;

public interface GameUI {
    void printTony(final int step);

    void printWord(List<Character> letters, final String word);

    void printIntermediateResults(final int solved, final int heats);

    void printGameWin();

    void printGameLost();

    void printEmoji(final String emojiType);

    void printMenu(final String menuType);
}
