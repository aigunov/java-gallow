package backend.academy.gallows;

import java.util.List;

public interface GameUI {
    void printTony(int step);

    void printWord(List<Character> letters, String word);

    void printIntermediateResults(int solved, int heats);

    void printGameWin();

    void printGameLost();

    void printEmoji(String emojiType);

    void printMenu(String menuType);
}
