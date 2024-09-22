package backend.academy.gallows;

import backend.academy.gallows.logic.GameLogic;
import backend.academy.gallows.model.GameResults;
import backend.academy.gallows.ui.GameInterface;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Главный класс для всего проекта в котором и происходит весь игровой процесс
 * Этот класс вызывает функционал всех остальных и запускает игру
 * начиная с метода session()
 */
@SuppressWarnings({"RegexpSinglelineJava", "MagicNumber"})
@Getter
@Setter
@Slf4j
public final class GameSession {
    //статические поля константы для того чтобы избежать "непонятных значений" в коде и сделать его более читаемым
    private final String playerName;

    private GameInterface gameInterface;
    private GameLogic gameLogic;

    public GameSession(final String name) {
        this.playerName = name;
    }

    /**
     * Основной метод игровой сессии
     * тут происходит вся игра
     */
    public void session() {
        GameResults result = GameResults.IN_PROGRESS;
        gameInterface = GameInterface.getInstance();
        gameLogic = GameLogic.getInstance();

        var params = gameLogic.createWord(gameInterface.menuCircle(playerName));

        while (result == GameResults.IN_PROGRESS) {
            gameInterface.roundBeginning(params);
            var middleRoundParams = gameLogic.roundMiddle();
            params.characters(middleRoundParams.characters()).step(middleRoundParams.step());
            gameInterface.roundEnding(params);
            result = gameLogic.checkResult();
        }

        if (result == GameResults.WIN) {
            gameInterface.winMenu(playerName);
        } else {
            gameInterface.loseMenu(playerName);
        }
    }
}
