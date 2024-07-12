package sk.tuke.gamestudio.game.core;

import org.junit.jupiter.api.BeforeEach;
import sk.tuke.gamestudio.game.cubemaze.core.Field;
import sk.tuke.gamestudio.game.cubemaze.core.Cube;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FieldTest {

    private Field field;

    @BeforeEach
    public void setUp() {
        field = new Field(new Cube());
    }

    private static Stream<List<int[]>> provideTestData() {
        List<int[]> testData = new ArrayList<>();
        testData.add(new int[]{0, 2, 3});
        testData.add(new int[]{4, 5, 6});
        return Stream.of(testData);
    }
}
