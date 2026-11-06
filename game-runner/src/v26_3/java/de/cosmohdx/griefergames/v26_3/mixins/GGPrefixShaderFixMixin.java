package de.cosmohdx.griefergames.v26_3.mixins;

import de.cosmohdx.griefergames.v26_3.PrefixShaderFix;
import java.io.IOException;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Patcht den GrieferGames-Text-Shader direkt nach dem Einlesen. */
@Mixin(ShaderManager.class)
public abstract class GGPrefixShaderFixMixin {

  @Redirect(
      method = "loadShader",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/server/packs/resources/Resource;readAllAsString()Ljava/lang/String;"
      ),
      require = 0
  )
  private static String ggaddon$fixPrefixAnimation(Resource resource) throws IOException {
    return PrefixShaderFix.patch(resource.readAllAsString());
  }
}
