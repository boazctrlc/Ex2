package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.util.Counter;

/**
 * A collision strategy for exploding bricks.
 * When hit, the brick plays an explosion sound and triggers collisions on its four
 * orthogonal neighboring bricks.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 * @see BasicCollisionStrategy
 */
public class ExplodingBrickStrategy extends BasicCollisionStrategy {

    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static final int ROW_OFFSET_INDEX = 0;
    private static final int COLUMN_OFFSET_INDEX = 1;
    private static final int MIN_GRID_INDEX = 0;

    private final Brick[][] bricksGrid;
    private final Sound explosionSound;

    /**
     * Constructs a new ExplodingBrickStrategy.
     *
     * @param collection The collection of game objects in the game.
     * @param bricksCounter A counter tracking the remaining bricks.
     * @param bricksGrid A two-dimensional array representing the grid of bricks.
     * @param explosionSound The sound played when the brick explodes.
     */
    public ExplodingBrickStrategy(GameObjectCollection collection, Counter bricksCounter,
                                  Brick[][] bricksGrid, Sound explosionSound) {
        super(collection, bricksCounter);
        this.bricksGrid = bricksGrid;
        this.explosionSound = explosionSound;
    }

    /**
     * Removes the brick, plays the explosion sound, and triggers collisions on adjacent bricks.
     *
     * @param brick The brick that was hit.
     * @param otherObject The game object that collided with the brick.
     */
    @Override
    public void onCollision(Brick brick, GameObject otherObject) {
        if (!removeBrick(brick)) {
            return;
        }
        explosionSound.play();
        int row = brick.getRow();
        int column = brick.getColumn();

        for (int[] direction : DIRECTIONS) {
            int newRow = row + direction[ROW_OFFSET_INDEX];
            int newColumn = column + direction[COLUMN_OFFSET_INDEX];
            if (isInBounds(newRow, newColumn) && bricksGrid[newRow][newColumn] != null) {
                bricksGrid[newRow][newColumn].onCollisionEnter(brick, null);
            }
        }
    }

    /*
     * Checks whether the given row and column indices fall within the bricks grid bounds.
     */
    private boolean isInBounds(int row, int column) {
        return row >= MIN_GRID_INDEX && row < bricksGrid.length
                && column >= MIN_GRID_INDEX && column < bricksGrid[row].length;
    }
}