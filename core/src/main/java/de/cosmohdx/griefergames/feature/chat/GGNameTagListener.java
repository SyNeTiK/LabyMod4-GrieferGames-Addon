package de.cosmohdx.griefergames.feature.chat;

import de.cosmohdx.griefergames.GrieferGames;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.entity.player.tag.TagType;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent;

public class GGNameTagListener {

  private final GrieferGames griefergames;
  private final OriginalDisplayNames originalDisplayNames;

  public GGNameTagListener(GrieferGames griefergames, OriginalDisplayNames originalDisplayNames) {
    this.griefergames = griefergames;
    this.originalDisplayNames = originalDisplayNames;
  }

  @Subscribe
  public void onRender(PlayerNameTagRenderEvent event) {
    if (!griefergames.configuration().enabled().get()
        || !griefergames.state().isOnGrieferGames()) {
      return;
    }
    if (!griefergames.configuration().chat().showPrefixInDisplayName()) return;
    NetworkPlayerInfo playerInfo = event.getPlayerInfo();
    if (playerInfo == null) return;
    if (event.tagType() != TagType.MAIN_TAG) return;
    // Originalen Namen holen (ohne Änderungen durch "Magische Prefixe ersetzen")
    // und nur auf einer Kopie arbeiten, damit die Tabliste nicht verändert wird.
    Component original = originalDisplayNames.resolve(playerInfo);
    if (original == null) return;
    Component copy = original.copy();
    if (copy instanceof TextComponent) {
      event.setNameTag(removeMarker((TextComponent) copy));
    }
  }

  private TextComponent removeMarker(TextComponent textComponent) {
    if (textComponent.getText().equals(" ✎")) {
      textComponent.text("");
    }
    for (Component component : textComponent.getChildren()) {
      if (component instanceof TextComponent) {
        removeMarker((TextComponent) component);
      }
    }
    return textComponent;
  }
}