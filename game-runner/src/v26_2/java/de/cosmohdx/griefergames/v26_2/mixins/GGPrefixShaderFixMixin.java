package de.cosmohdx.griefergames.v26_2.mixins;

import de.cosmohdx.griefergames.v26_2.PrefixShaderFix;
import net.minecraft.client.renderer.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Patcht den GrieferGames-Text-Shader, bevor Minecraft ihn verarbeitet. */
@Mixin(ShaderManager.class)
public abstract class GGPrefixShaderFixMixin {

  @ModifyArg(
      method = "loadShader",
      at = @At(
          value = "INVOKE",
          target = "Lcom/mojang/blaze3d/preprocessor/GlslPreprocessor;process(Ljava/lang/String;)Ljava/util/List;"
      ),
      index = 0,
      require = 0
  )
  private static String ggaddon$fixPrefixAnimation(String source) {
    return PrefixShaderFix.patch(source);
  }
}
