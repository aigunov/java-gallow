package backend.academy.gallows;

public interface GameUI {
    void printMenu();

    void printTony(final int step);

    void printWord(final String word, final String[] letters);

    void printIntermediateResults(final int solved, final int heats);

    void printGameWin(final String name);

    void printGameLost(final String name);
}
