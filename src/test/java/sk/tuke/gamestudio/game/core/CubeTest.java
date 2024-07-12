package sk.tuke.gamestudio.game.core;

import org.junit.Test;
import sk.tuke.gamestudio.game.cubemaze.core.Coordinate;
import sk.tuke.gamestudio.game.cubemaze.core.MoveDirection;
import sk.tuke.gamestudio.game.cubemaze.core.Cube;

import static org.junit.Assert.assertEquals;

public class CubeTest {

    @Test
    public void testInitialCoordinate() {
        Cube cube = new Cube();
        Coordinate initialCoordinate = new Coordinate();
        assertEquals(initialCoordinate.getX(), cube.getCoordinate().getX());
        assertEquals(initialCoordinate.getY(), cube.getCoordinate().getY());
    }

    @Test
    public void testMoveForward() {
        Cube cube = new Cube();
        cube.move(MoveDirection.FORWARD);
        Coordinate expectedCoordinate = new Coordinate(0, -1);
        assertEquals(expectedCoordinate.getX(), cube.getCoordinate().getX());
        assertEquals(expectedCoordinate.getY(), cube.getCoordinate().getY());
    }

    @Test
    public void testMoveBackward() {
        Cube cube = new Cube();
        cube.move(MoveDirection.BACKWARD);
        Coordinate expectedCoordinate = new Coordinate(0, 1);
        assertEquals(expectedCoordinate.getX(), cube.getCoordinate().getX());
        assertEquals(expectedCoordinate.getY(), cube.getCoordinate().getY());
    }

    @Test
    public void testMoveLeft() {
        Cube cube = new Cube();
        cube.move(MoveDirection.LEFT);
        Coordinate expectedCoordinate = new Coordinate(-1, 0);
        assertEquals(expectedCoordinate.getX(), cube.getCoordinate().getX());
        assertEquals(expectedCoordinate.getY(), cube.getCoordinate().getY());
    }

    @Test
    public void testMoveRight() {
        Cube cube = new Cube();
        cube.move(MoveDirection.RIGHT);
        Coordinate expectedCoordinate = new Coordinate(1, 0);
        assertEquals(expectedCoordinate.getX(), cube.getCoordinate().getX());
        assertEquals(expectedCoordinate.getY(), cube.getCoordinate().getY());
    }

    @Test
    public void testMoveForwardAndBackward() {
        Cube cube = new Cube();
        cube.move(MoveDirection.BACKWARD);
        cube.move(MoveDirection.BACKWARD);
        cube.move(MoveDirection.FORWARD);
        Coordinate expectedCoordinate = new Coordinate(0, 1);
        assertEquals(expectedCoordinate.getX(), cube.getCoordinate().getX());
        assertEquals(expectedCoordinate.getY(), cube.getCoordinate().getY());
    }

    @Test
    public void testMoveLeftAndRight() {
        Cube cube = new Cube();

        cube.move(MoveDirection.RIGHT);
        cube.move(MoveDirection.LEFT);
        Coordinate expectedCoordinate = new Coordinate(0, 0);
        assertEquals(expectedCoordinate.getX(), cube.getCoordinate().getX());
        assertEquals(expectedCoordinate.getY(), cube.getCoordinate().getY());
    }
}
