package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shader.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

/**
 * Created by Root on 2017-03-29.
 */
public class EntityRenderer {


    private StaticShader shader;



    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
        this.shader = shader;

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);


        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }


    public void render(Map<TexturedModel,List<Entity>> entities){
        for (TexturedModel model:entities.keySet()){
            prepareTextureModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity:batch){
                prepareInstace(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES,model.getRawModel().getVertexCount(),GL11.GL_UNSIGNED_INT,0);
            }
            unbindTexturedModel();
        }
    }

    private  void prepareTextureModel(TexturedModel model){
        RawModel rawModel = model.getRawModel();

        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture = model.getTexture();
        if (texture.getHasTransparency()){
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLighting(texture.getUseFakeLighting());
        shader.loadShineVariables(texture.getShineDamper(),texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,model.getTexture().getID());
    }
    private void unbindTexturedModel(){
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
    private void prepareInstace(Entity entity){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScalse());
        shader.loadTransformationMatrix(transformationMatrix);
    }


}
