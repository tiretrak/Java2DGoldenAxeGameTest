package infra;


import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * AnimationPlayer class.
 * 
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class AnimationPlayer {

    private final BufferedImage spriteSheet;
    private final Map<String, Sprite[]> animations = new HashMap<>();
    private Sprite[] currentAnimation;
    private int frame;
    private boolean flip;
    private final List<Collider> colliders;
    
    public AnimationPlayer(String spriteSheetRes, String collidersRes, String ... animationResources) {
        spriteSheet = Resource.getImage(spriteSheetRes);
        colliders = Resource.getColliders(collidersRes);
        for (String animationResource : animationResources) {
            loadAnimationInternal(animationResource);
        }
    }

    public void loadAnimation(String spritesRes) {
        loadAnimationInternal(spritesRes);
    }
    
    private void loadAnimationInternal(String animationRes) {
        try {
            String name = animationRes;
            List<Sprite> spritesList = new ArrayList<>();
            InputStream is = getClass().getResourceAsStream(animationRes);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] data = line.split("\\ ");
                if (data[0].equals("name")) {
                    name = data[1];
                }
                else if (data[0].equals("sprite")) {
                    int x = Integer.parseInt(data[1]);
                    int y = Integer.parseInt(data[2]);
                    int width = Integer.parseInt(data[3]);
                    int height = Integer.parseInt(data[4]);
                    int ox = Integer.parseInt(data[5]);
                    int oy = Integer.parseInt(data[6]);
                    BufferedImage subimage = spriteSheet.getSubimage(x, y, width, height);
                    Rectangle rectangle = new Rectangle(x, y, width, height);
                    Sprite sprite = new Sprite(subimage, ox, oy, rectangle, colliders);
                    spritesList.add(sprite);
                }
            }
            br.close();
            Sprite[] spritesArr = spritesList.toArray(new Sprite[0]);
            animations.put(name, spritesArr);
            
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
    public Sprite getCurrentAnimationFrame() {
        return currentAnimation[frame];
    }
    
    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame % currentAnimation.length;
    }

    public void setAnimation(String name) {
        currentAnimation = animations.get(name);
        setFrame(frame);
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }
    
    public void draw(Graphics2D g, int x, int y) {
        if (currentAnimation != null) {
            Sprite sprite = currentAnimation[frame];
            sprite.draw(g, x, y, flip);
        }
    }

}
