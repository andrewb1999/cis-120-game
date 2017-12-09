import java.awt.*;

public interface Drawable {
    /**
     * Default draw method that provides how the object should be drawn in the GUI. This method does
     * not draw anything. Subclass should override this method based on how their object should
     * appear.
     *
     * @param g The <code>Graphics</code> context used for drawing the object. Remember graphics
     * contexts that we used in OCaml, it gives the context in which the object should be drawn (a
     * canvas, a frame, etc.)
     */
    void draw(Graphics g);
}
