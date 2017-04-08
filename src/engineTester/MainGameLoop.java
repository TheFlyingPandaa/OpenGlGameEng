package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shader.StaticShader;
import terrain.Terrain;
import textures.ModelTexture;

/**
 * Created by Root on 2017-03-29.
 */
public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();

        Loader loader = new Loader();





        //RawModel modle = loader.loadToVAO(vertices,textureCordinates,indices);
        RawModel modle = OBJLoader.loadObjModel("dragon",loader);
        //ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
        //ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
        TexturedModel staticModle = new TexturedModel(modle,new ModelTexture(loader.loadTexture("stallTexture")));
        ModelTexture texture = staticModle.getTexture();
        texture.setShineDamper(10);
        texture.setReflectivity(1);

        //grass.getTexture.setHasTransparency(true);
        //grass.getTexture.setUseFakeLighting(true);


        Entity entity = new Entity(staticModle, new Vector3f(0,0,-50),0,0,0,1);
        Light light = new Light(new Vector3f(0,0,-20),new Vector3f(1,1,1));

        Terrain terrain = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(1,-1,loader,new ModelTexture(loader.loadTexture("grass")));

        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()) {

            //entity.increasePosition(0.002f,0,-0.02f);
            entity.increaseRotation(0,1,0);
            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processEntity(entity);
            renderer.render(light,camera);

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}

