package backend.academy.gallows;

import java.util.List;

public interface GameUI {
    void printTony(final int step);

    void printWord(List<Character> letters, final String word);

    void printIntermediateResults(final int solved, final int heats);

    void printGameWin(final String name);

    void printGameLost();
}
