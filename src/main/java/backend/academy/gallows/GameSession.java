package backend.academy.gallows;

import backend.academy.gallows.logic.GameLogic;
import backend.academy.gallows.model.GamePlayParameters;
import backend.academy.gallows.model.GameResults;
import backend.academy.gallows.ui.GameInterface;
import java.io.IOException;
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
        try {
            gameInterface = GameInterface.getInstance();
        } catch (IOException e) {
            log.error("Возникла ошибка при чтениие json файла: {}", e.getMessage());
            System.err.println("Не удалось загрузить меню. Пожалуйста, перезапустите приложение.");
            throw new RuntimeException(e);
        }
        gameLogic = GameLogic.getInstance();

        GamePlayParameters params = null;
        try {
            params = gameLogic.createWord(gameInterface.menuCircle(playerName));
        } catch (IOException e) {
            log.error("Ошибка при чтении файла: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        while (result == GameResults.IN_PROGRESS) {
            gameInterface.roundBeginning(params);
            var middleRoundParams = gameLogic.roundMiddle();
            params
                .characters(middleRoundParams.characters())
                .step(middleRoundParams.step())
                .hits(middleRoundParams.hits())
                .solved(middleRoundParams.solved());
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
