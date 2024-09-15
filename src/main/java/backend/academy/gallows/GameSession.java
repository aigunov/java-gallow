package backend.academy.gallows;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameSession {
    private final String playerName;

    public GameSession(final String name) {
        this.playerName = name;
    }
}
