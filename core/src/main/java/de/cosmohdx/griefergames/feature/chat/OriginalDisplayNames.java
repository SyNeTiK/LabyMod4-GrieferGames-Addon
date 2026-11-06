package de.cosmohdx.griefergames.feature.chat;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.NetworkPlayerInfo;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Merkt sich den unveränderten Tablisten-Namen eines Spielers, bevor das Addon ihn
 * (z. B. beim Ersetzen magischer Prefixe) verändert. So kann der Nametag über dem Kopf
 * immer den originalen Prefix vom Server anzeigen.
 *
 * <p>Das Original gilt nur, solange der Tablisten-Name noch genau das Objekt ist, das
 * das Addon verändert hat. Schickt der Server einen neuen Namen (z. B. das nächste Bild
 * eines animierten Prefix), wird automatisch wieder der aktuelle Name verwendet.
 */
public class OriginalDisplayNames {

  private final Map<UUID, Entry> entries = new ConcurrentHashMap<>();

  /** Vor dem Verändern aufrufen: speichert eine Kopie des aktuellen Namens. */
  public void remember(NetworkPlayerInfo playerInfo) {
    UUID id = idOf(playerInfo);
    Component current = playerInfo.displayName();
    if (id == null || current == null) {
      return;
    }
    Entry existing = this.entries.get(id);
    if (existing != null && existing.modified == current) {
      return; // dieses Objekt wurde schon gesichert
    }
    this.entries.put(id, new Entry(current, current.copy()));
  }

  /** Liefert den originalen Namen, falls das Addon ihn verändert hat, sonst den aktuellen. */
  public Component resolve(NetworkPlayerInfo playerInfo) {
    Component current = playerInfo.displayName();
    UUID id = idOf(playerInfo);
    if (id == null) {
      return current;
    }
    Entry entry = this.entries.get(id);
    if (entry == null) {
      return current;
    }
    if (entry.modified == current) {
      return entry.original;
    }
    // Server hat inzwischen einen neuen Namen geschickt -> Original ist veraltet.
    this.entries.remove(id, entry);
    return current;
  }

  private static UUID idOf(NetworkPlayerInfo playerInfo) {
    if (playerInfo == null || playerInfo.profile() == null) {
      return null;
    }
    return playerInfo.profile().getUniqueId();
  }

  private static final class Entry {
    private final Component modified;
    private final Component original;

    private Entry(Component modified, Component original) {
      this.modified = modified;
      this.original = original;
    }
  }
}
