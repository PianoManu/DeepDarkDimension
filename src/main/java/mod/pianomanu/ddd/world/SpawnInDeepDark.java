package mod.pianomanu.ddd.world;

import mod.pianomanu.ddd.DDDMain;
import mod.pianomanu.ddd.config.DDDConfig;
import mod.pianomanu.ddd.util.JSONBuilder;
import mod.pianomanu.ddd.util.JSONParser;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sun.misc.IOUtils;

import java.io.*;
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
    private static IWorld world;

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

    /*@SubscribeEvent
    public void playerLogginIn(final PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        loadPlayerList();
        if (player.level.getServer() != null) {
            File saveFile = player.level.getServer().getServerDirectory();
            //String worldName = saveFile.getName();
            String worldName = saveFile.getName();
            System.out.println(worldName);
            System.out.println(player.server.getPlayerList().getPlayers());
            System.out.println(world.players());
            String playerName = player.getName().getString();
            if(playersPerWorld.get(worldName) != null) {
                if (!playersPerWorld.get(worldName).contains(playerName)) {
                    System.out.println("Player new");
                    registerPlayer(worldName, playerName);
                    savePlayerList();
                    ServerWorld deepDark = player.server.getLevel(DDDMain.DEEP_DARK_DIMENSION);
                    if (DDDConfig.PLAYER_SPAWNS_IN_DEEP_DARK_DIMENSION.get() && deepDark != null) {
                        DDDTeleporter teleporter = new DDDTeleporter(new BlockPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get()));
                        teleporter.setOverworldTeleporterPos(player.blockPosition());
                        player.setPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get());
                        player.changeDimension(deepDark, teleporter);
                        player.setRespawnPosition(DDDMain.DEEP_DARK_DIMENSION, new BlockPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get()), 0, true, false);
                    }
                } else {
                    System.out.println("Player not new");
                }
            } else {
                registerPlayer(worldName, playerName);
                savePlayerList();
                System.out.println("Player new and first");
            }
            System.out.println(world.players());
            System.out.println(world.getPlayerByUUID(player.getUUID()));
            //player.server.getWorldPath()
        }
    }*/

    @SubscribeEvent
    public void playerLoadsWorld(final WorldEvent.Load event) {
        world = event.getWorld();
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

    public static void writeContent(File file, String content) {
        try {
            writeFile(file, content);
            //System.out.println("wrote");
        } catch (FileNotFoundException e) {
            try {
                //System.out.println(file.getAbsolutePath());
                file.createNewFile();
                writeFile(file, content);
                //System.out.println("created file");
            } catch (IOException ignored) {
                //System.out.println("error");
                ignored.printStackTrace();
            }
        } catch (IOException ignored) {
            //System.out.println("didn't wrote");
            ignored.printStackTrace();
        }
    }

    private static void writeFile(File file, String content) throws IOException {
        File newFile = new File(file.getAbsolutePath());
        //file.delete();
        try (FileWriter writer = new FileWriter(newFile.getAbsolutePath())) {
            writer.write(content);
            //System.out.println("writing...");
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
            //System.out.println("wrote");
            //System.out.println(builder.build());
        } catch (IOException ignored) {
            //System.out.println("didn't wrote");
            ignored.printStackTrace();
        }
    }

    private static void loadPlayerList() {
        File playerFile = new File(FILE_PATH);
        String content = getContent(playerFile);
        JSONParser parser = new JSONParser(content);
        try {
            playersPerWorld = parser.parse();
            //System.out.println(playersPerWorld); //TODO Debug - remove
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '[') {
                String worldName = "";
                for (int j = i; content.charAt(j) != ':'; j++) {
                    worldName = content.substring(i, j);
                }
                if (!worldName.isEmpty()) {
                    playersPerWorld.put(worldName, new ArrayList<>());
                    for (int j = i; content.charAt(j) != '}'; j++) {
                        if (content.charAt(j) == '{' || content.charAt(j) == ',') {
                            String playerName = "";
                            for (int k = j + 1; content.charAt(j) != ','; j++) {
                                try {
                                    playerName = content.substring(j + 1, k);
                                } catch (StringIndexOutOfBoundsException e) {
                                    System.out.println("TTTTTTest");
                                }
                            }
                            List<String> players = playersPerWorld.get(worldName);
                            players.add(playerName);
                            playersPerWorld.put(worldName, players);
                        }
                    }
                }
            }

        }*/
    }

    @SubscribeEvent
    public void playerLogginIn(final PlayerEvent.PlayerLoggedInEvent event) {
        loadPlayerList();
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        //System.out.println(player.server.getPlayerList().getPlayers());
        //System.out.println(world.players());
        String worldIdentifier = player.level.toString();
        String playerName = player.getName().getString();
        //System.out.println("World: "+worldIdentifier+", Name: "+playerName);
        if (!playersPerWorld.containsKey(worldIdentifier)) {
            playersPerWorld.put(worldIdentifier, new ArrayList<>());
            //System.out.println("Added new World");
        }
        if (!playersPerWorld.get(worldIdentifier).contains(playerName)) {
            //System.out.println("PlayerNew");
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
        } else {
            //System.out.println("PlayerNotNew");
        }
    }
}
//========SOLI DEO GLORIA========//