package mod.pianomanu.ddd.world;

import mod.pianomanu.ddd.DDDMain;
import mod.pianomanu.ddd.config.DDDConfig;
import mod.pianomanu.ddd.util.JSONBuilder;
import mod.pianomanu.ddd.util.JSONParser;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.3 06/04/21
 */
public class SpawnInDeepDark {

    private static final String FILE_PATH = "ddd_cache.json";
    private static Map<String, List<String>> playersPerWorld = new HashMap<>();

    private static String getContent(File file) {
        byte[] fileContent;
        try {
            fileContent = readFile(file);
            return new String(fileContent, StandardCharsets.UTF_8);
        } catch (IOException ignored) {

        }
        return "ERROR - Could not get Content";
    }


    private static byte[] readFile(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file.getAbsolutePath());
        byte[] content;
        try {
            content = IOUtils.readFully(inputStream, (int) file.length(), true);
        } finally {
            inputStream.close();
        }
        return content;
    }

    private static void writeFile(File file, String content) throws IOException {
        File newFile = new File(file.getAbsolutePath());
        try (FileWriter writer = new FileWriter(newFile.getAbsolutePath())) {
            writer.write(content);
        }
    }

    private static void addPlayer(String worldName, String playerName) {
        List<String> players = playersPerWorld.get(worldName);
        if (players == null) {
            players = new ArrayList<>();
        }
        players.add(playerName);
        playersPerWorld.put(worldName, players);
    }

    private static void savePlayerList() {
        File playerFile = new File(FILE_PATH);
        JSONBuilder builder = new JSONBuilder();
        for (String worldName : playersPerWorld.keySet()) {
            builder.addPlayersToWorld(worldName, playersPerWorld.get(worldName));
        }
        builder.createJSON();
        try {
            writeFile(playerFile, builder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadPlayerList() {
        File playerFile = new File(FILE_PATH);
        String content = getContent(playerFile);
        JSONParser parser = new JSONParser(content);
        try {
            playersPerWorld = parser.parse();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void playerLogginIn(final PlayerEvent.PlayerLoggedInEvent event) {
        loadPlayerList();
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        String worldIdentifier = player.level.toString();
        String playerName = player.getName().getString();
        if (!playersPerWorld.containsKey(worldIdentifier)) {
            playersPerWorld.put(worldIdentifier, new ArrayList<>());
        }
        if (!playersPerWorld.get(worldIdentifier).contains(playerName)) {
            addPlayer(worldIdentifier, playerName);
            ServerWorld deepDark = player.server.getLevel(DDDMain.DEEP_DARK_DIMENSION);
            if (DDDConfig.PLAYER_SPAWNS_IN_DEEP_DARK_DIMENSION.get() && deepDark != null) {
                DDDTeleporter teleporter = new DDDTeleporter(new BlockPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get()));
                teleporter.setOverworldTeleporterPos(player.blockPosition());
                player.setPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get());
                player.changeDimension(deepDark, teleporter);
                player.setRespawnPosition(DDDMain.DEEP_DARK_DIMENSION, new BlockPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get()), 0, true, false);
            }
            savePlayerList();
        }
    }
}
//========SOLI DEO GLORIA========//