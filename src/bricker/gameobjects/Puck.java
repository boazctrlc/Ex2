package bricker.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a secondary puck ball spawned by a puck brick.
 * Removes itself from the game when leaving the vertical window boundaries.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 * @see Ball
 */
public class Puck extends Ball {

    private static final float MIN_WINDOW_HEIGHT = 0;

    private final float windowHeight;
    private final GameObjectCollection collection;

    /**
     * Constructs a new Puck instance.
     *
     * @param topLeftCorner Position of the puck, in window coordinates.
     * @param dimensions Width and height of the puck.
     * @param renderable The visual renderable representing the puck.
     * @param collisionSound The sound played when the puck collides with another object.
     * @param windowHeight The height of the game window.
     * @param collection The collection of game objects in the game.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound, float windowHeight, GameObjectCollection collection) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.windowHeight = windowHeight;
        this.collection = collection;
    }

    /**
     * Removes the puck from the game once it exits the vertical bounds of the window.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isOutOfBounds()) {
            collection.removeGameObject(this);
        }
    }

    /*
     * Checks whether the puck has completely moved outside the vertical screen bounds.
     */
    private boolean isOutOfBounds() {
        float topY = getTopLeftCorner().y();
        float bottomY = topY + getDimensions().y();
        return bottomY < MIN_WINDOW_HEIGHT || topY > windowHeight;
    }
}