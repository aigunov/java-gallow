package backend.academy.gallows.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс для хранения в себе параметров
 * и настроек игры для передачи его от
 * GameLogic -> GameSession -> GameInterface
 * и в обратном направлении
 * идея напрямую перекидывать в методах все аргументы -
 * была отклонена
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class GamePlayParameters {
    int step = 0;
    private Levels level;
    private Categories category;
    private Word word;
    private List<Character> characters;
    private int solved;
    private int hits;
}
