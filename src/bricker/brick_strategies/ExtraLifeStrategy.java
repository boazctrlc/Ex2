package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.FallingHeart;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A collision strategy that spawns a falling heart from the broken brick's center,
 * allowing the player to regain a life if caught by the main paddle.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 * @see BasicCollisionStrategy
 */
public class ExtraLifeStrategy extends BasicCollisionStrategy {

    private final Counter livesCounter;
    private final Renderable heartImage;
    private final Vector2 heartDimensions;
    private final float windowHeight;
    private final int maxLives;

    /**
     * Constructs a new ExtraLifeStrategy.
     *
     * @param collection The collection of game objects in the game.
     * @param bricksCounter A counter tracking the remaining bricks.
     * @param livesCounter A counter tracking the player's remaining lives.
     * @param heartImage The visual renderable for the falling heart.
     * @param heartDimensions The dimensions of the falling heart.
     * @param windowHeight The height of the game window.
     * @param maxLives The maximum number of lives the player can hold.
     */
    public ExtraLifeStrategy(GameObjectCollection collection, Counter bricksCounter,
                             Counter livesCounter, Renderable heartImage, Vector2 heartDimensions,
                             float windowHeight, int maxLives) {
        super(collection, bricksCounter);
        this.livesCounter = livesCounter;
        this.heartImage = heartImage;
        this.heartDimensions = heartDimensions;
        this.windowHeight = windowHeight;
        this.maxLives = maxLives;
    }

    /**
     * Removes the brick and spawns a falling heart at its center.
     *
     * @param brick The brick that was hit.
     * @param otherObject The game object that collided with the brick.
     */
    @Override
    public void onCollision(Brick brick, GameObject otherObject) {
        if (!removeBrick(brick)) {
            return;
        }
        FallingHeart fallingHeart = new FallingHeart(Vector2.ZERO, heartDimensions, heartImage,
                maxLives, livesCounter, collection, windowHeight);
        fallingHeart.setCenter(brick.getCenter());
        collection.addGameObject(fallingHeart);
    }
}