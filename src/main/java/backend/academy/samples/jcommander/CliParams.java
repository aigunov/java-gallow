package backend.academy.samples.jcommander;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CliParams {
    @Parameter
    private final List<String> parameters = new ArrayList<>();

    @Parameter(names = {"-log", "-verbose"}, description = "Level of verbosity")
    private final Integer verbose = 1;

    @Parameter(names = "-groups", description = "Comma-separated list of group names to be run")
    private String groups;

    @Parameter(names = "-debug", description = "Debug mode")
    private final boolean debug = false;

    private Integer setterParameter;

    @Parameter(names = "-setterParameter", description = "A parameter annotation on a setter method")
    public void setParameter(Integer value) {
        this.setterParameter = value;
    }
}
