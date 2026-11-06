package de.cosmohdx.griefergames.v26_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Korrigiert den Text-Shader aus dem GrieferGames-Server-Resourcepack.
 *
 * <p>Der Shader animiert Prefixe (z. B. "Animierte Rainbow Farbe") nur, wenn
 * {@code int(Position.z) == int(0)} gilt. In GUI/Chat/Tabliste ist das immer erfüllt, bei
 * Nametags in der Welt ist {@code Position} aber relativ zur Kamera. Dadurch wird die
 * Animation abhängig von der Blickrichtung (Nord/Süd-Abstand der Kamera) übersprungen und
 * der Prefix erscheint in seinen rohen Code-Farben. Außerhalb der GUI wird die Bedingung
 * deshalb entfernt.
 */
public final class PrefixShaderFix {

  private static final Logger LOGGER = LoggerFactory.getLogger("GrieferGames-Addon");

  /** Nur Shader mit der GrieferGames-Prefix-Animation werden angefasst. */
  private static final String MARKER = "prefix_animation_lut";
  private static final String BROKEN_CONDITION = "if (int(Position.z) == int(0)) {";
  private static final String FIXED_CONDITION =
      "\n#if defined(IS_GUI)\n"
          + "    if (int(Position.z) == int(0)) {\n"
          + "#else\n"
          + "    if (true) { // GrieferGames-Addon: Prefix-Animation auch auf Nametags\n"
          + "#endif\n";

  private PrefixShaderFix() {
  }

  public static String patch(String source) {
    if (source == null || !source.contains(MARKER) || !source.contains(BROKEN_CONDITION)) {
      return source;
    }
    LOGGER.info("Prefix-Animations-Shader von GrieferGames für Nametags korrigiert");
    return source.replace(BROKEN_CONDITION, FIXED_CONDITION);
  }
}
