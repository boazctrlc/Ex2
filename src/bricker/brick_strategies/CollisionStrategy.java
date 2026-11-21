package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;

/**
 * Defines a strategy for handling collisions between a brick and another game object.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 */
public interface CollisionStrategy {

    /**
     * Handles the behavior triggered when a brick collides with another game object.
     *
     * @param brick The brick that was hit.
     * @param otherObject The game object that collided with the brick.
     */
    void onCollision(Brick brick, GameObject otherObject);
}