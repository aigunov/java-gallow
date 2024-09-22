package backend.academy.gallows.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class GamePlayParameters {
    private Levels level;
    private Categories category;
    private Word word;
    private List<Character> characters;

    int step = 0;
}
