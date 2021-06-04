package mod.pianomanu.ddd.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.0 06/04/21
 */
public class JSONBuilder {
    private final StringBuilder builder = new StringBuilder();

    private final Map<String, List<String>> playersPerWorld = new HashMap<>();

    public JSONBuilder(String worldName, List<String> players) {
        this.playersPerWorld.put(worldName, players);
    }

    public JSONBuilder() {

    }

    public Map<String, List<String>> getPlayersPerWorld() {
        return this.playersPerWorld;
    }

    public void addPlayersToWorld(String worldName, List<String> players) {
        if (!this.playersPerWorld.containsKey(worldName)) {
            this.playersPerWorld.put(worldName, players);
            return;
        }
        for (String p : players) {
            if (!this.playersPerWorld.get(worldName).contains(p)) {
                List<String> newPlayers = this.playersPerWorld.get(worldName);
                newPlayers.add(p);
                this.playersPerWorld.put(worldName, newPlayers);
            }
        }
    }

    public String build() {
        return this.builder.toString();
    }

    public void createJSON() {
        this.builder.append("{\n\"Worlds\":{\n");
        int wIndex = 0;
        for (String w : this.playersPerWorld.keySet()) {
            wIndex++;
            this.builder.append("\"").append(w).append("\":\n[\n");
            int pIndex = 0;
            for (String p : this.playersPerWorld.get(w)) {
                pIndex++;
                this.builder.append("\"").append(p).append("\"");
                if (pIndex < this.playersPerWorld.get(w).size())
                    this.builder.append(",\n");
            }
            this.builder.append("\n]");
            if (wIndex < this.playersPerWorld.size())
                this.builder.append(",");
            this.builder.append("\n");
        }
        this.builder.append("}\n}");
    }
}
//========SOLI DEO GLORIA========//