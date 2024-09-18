package backend.academy.gallows.model;

public class WordFromDictionaryNotValid extends RuntimeException {
    public WordFromDictionaryNotValid(String message) {
        super(message);
    }
}
