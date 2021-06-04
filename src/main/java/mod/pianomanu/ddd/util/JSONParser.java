package mod.pianomanu.ddd.util;

import java.text.ParseException;
import java.util.*;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.0 06/04/21
 */
public class JSONParser {
    private final Map<String, List<String>> playersPerWorld = new HashMap<>();
    private String JSON;

    public JSONParser(String JSON) {
        this.JSON = JSON;
    }

    public Map<String, List<String>> parse() throws ParseException {
        try {
            if (removeOuterBrackets())
                return processJSONWorlds(Objects.requireNonNull(getJSONWorlds(this.JSON)));
        } catch (StringIndexOutOfBoundsException ignored) {

        }
        throw new ParseException("Malformed JSON - JSON should start with '{' and end with '}'", 0);
    }

    private boolean removeOuterBrackets() {
        if (this.JSON.charAt(this.JSON.length() - 1) == '}' && this.JSON.charAt(0) == '{') {
            this.JSON = this.JSON.substring(1, this.JSON.length() - 2);
            return true;
        }
        return false;
    }

    private String getJSONWorlds(String JSON) {
        for (int i = 0; i < JSON.length(); i++) {
            if (JSON.charAt(i) == '{') {
                for (int j = i; j < JSON.length(); j++) {
                    if (JSON.charAt(j) == '}') {
                        //if (removeOuterBrackets(JSON.substring(i, j)) != null)
                        return JSON.substring(i, j);
                    }
                }
            }
        }
        return null;
    }

    private Map<String, List<String>> processJSONWorlds(String JSONWorlds) {
        Map<String, List<String>> playersPerWorld = new HashMap<>();
        List<String> JSONWorld = getWorlds(JSONWorlds);
        for (String world : JSONWorld) {
            playersPerWorld.put(world, new ArrayList<>());
        }
        //playersPerWorld.replaceAll((w, v) -> getPlayersFromWorld(JSONWorlds, w));
        for (String w : playersPerWorld.keySet()) {
            playersPerWorld.replace(w, getPlayersFromWorld(JSONWorlds, w));
        }
        //System.out.println(playersPerWorld);
        return playersPerWorld;
    }

    private List<String> getWorlds(String JSONWorlds) {
        List<String> JSONWorld = new ArrayList<>();
        int begin = 0;
        int end = 0;
        for (int j = 0; j < JSONWorlds.length(); j++) {
            if (JSONWorlds.charAt(j) == '{') {
                begin = j;
            }
            if (j > 0 && JSONWorlds.charAt(j - 1) == ']' && JSONWorlds.charAt(j) == ',') {
                begin = j;
            }
            if (JSONWorlds.charAt(j) == ':') {
                end = j;
                JSONWorld.add(JSONWorlds.substring(begin + 3, end - 1));
                begin = j;
            }
        }
        return JSONWorld;
    }

    private List<String> getPlayersFromWorld(String JSONWorlds, String worldName) {
        String playerString = "";
        int begin = 0;
        int end = 0;
        if (JSONWorlds.contains(worldName + "\":")) {
            begin = JSONWorlds.indexOf(worldName);
            for (int j = begin; j < JSONWorlds.length(); j++) {
                if (JSONWorlds.charAt(j) == '[' && JSONWorlds.charAt(j + 1) == '\n') {
                    begin = j;
                }
                if (j > 0 && JSONWorlds.charAt(j) == ']' && JSONWorlds.charAt(j - 1) == '\n') {
                    end = j;
                    playerString = JSONWorlds.substring(begin + 2, end);
                    break;
                }
            }
        }
        //System.out.println(JSONWorlds + ",   " + playerString);
        List<String> players = new ArrayList<>();
        begin = 0;
        end = 0;
        //playerString = playerString.replace("\n","");
        for (int i = begin; i < playerString.length(); i++) {
            if (playerString.charAt(i) == ',') {
                end = i;
                if (begin == 0)
                    players.add(playerString.substring(begin + 1, end - 1));
                else
                    players.add(playerString.substring(begin + 3, end - 1));
                begin = i;
            }
            if (i == playerString.length() - 1) {
                end = i;
                players.add(playerString.substring(begin + 1, end - 1));
            }
        }
        //System.out.println("Players for World " + worldName + ": " + players);
        return players;
    }
}
//========SOLI DEO GLORIA========//