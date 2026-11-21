package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a single brick in the game grid that delegates collision handling
 * to a CollisionStrategy.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 * @see CollisionStrategy
 */
public class Brick extends GameObject {

    private final int row;
    private final int column;
    private final CollisionStrategy collisionStrategy;

    /**
     * Constructs a new Brick instance.
     *
     * @param topLeftCorner Position of the brick, in window coordinates.
     * @param dimensions Width and height of the brick.
     * @param renderable The visual renderable representing the brick.
     * @param row The row index of the brick in the bricks grid.
     * @param column The column index of the brick in the bricks grid.
     * @param collisionStrategy The strategy invoked when the brick is hit.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 int row, int column, CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.row = row;
        this.column = column;
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Delegates collision handling to this brick's collision strategy.
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }

    /**
     * Returns the row index of this brick in the bricks grid.
     *
     * @return The brick's row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index of this brick in the bricks grid.
     *
     * @return The brick's column index.
     */
    public int getColumn() {
        return column;
    }
}