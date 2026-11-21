package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * A basic collision strategy that removes a brick from the game when hit
 * and updates the remaining bricks counter.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 * @see CollisionStrategy
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    /**
     * The collection of all game objects in the game.
     */
    protected final GameObjectCollection collection;
    private final Counter bricksCounter;

    /**
     * Constructs a new BasicCollisionStrategy.
     *
     * @param collection The collection of game objects in the game.
     * @param bricksCounter A counter tracking the remaining bricks.
     */
    public BasicCollisionStrategy(GameObjectCollection collection, Counter bricksCounter) {
        this.collection = collection;
        this.bricksCounter = bricksCounter;
    }

    /**
     * Removes the given brick from the static objects layer and decrements the bricks counter
     * if the removal succeeded.
     *
     * @param brick The brick to remove.
     * @return true if the brick was removed, false otherwise.
     */
    protected boolean removeBrick(Brick brick) {
        if (collection.removeGameObject(brick, Layer.STATIC_OBJECTS)) {
            bricksCounter.decrement();
            return true;
        }
        return false;
    }

    /**
     * Removes the brick upon collision.
     *
     * @param brick The brick that was hit.
     * @param otherObject The game object that collided with the brick.
     */
    @Override
    public void onCollision(Brick brick, GameObject otherObject) {
        removeBrick(brick);
    }
}