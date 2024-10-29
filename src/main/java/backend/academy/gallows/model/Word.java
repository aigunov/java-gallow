package backend.academy.gallows.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    String word;
    String hint;
    Levels levels;
    Categories category;

    boolean didUseHintAlready = false;
}
