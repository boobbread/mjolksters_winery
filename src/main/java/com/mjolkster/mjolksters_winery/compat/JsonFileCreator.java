package com.mjolkster.mjolksters_winery.compat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonFileCreator {
    public static void main(String[] args) {

        for (String i : COLOURS.keySet()) {
            try {
                File myObj = new File("D:\\WineryModMC\\src\\generated\\resources\\data\\mjolksters_winery\\recipe\\" + i + "_juice.json");
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException ignored) {
            }

            try {
                FileWriter myWriter = new FileWriter("D:\\WineryModMC\\src\\generated\\resources\\data\\mjolksters_winery\\recipe\\" + i + "_juice.json");
                myWriter.write(
                        "{\n" +
                                "  \"type\": \"create:compacting\",\n" +
                                "  \"ingredients\": [\n" +
                                "    {\n" +
                                "      \"item\": \"mjolksters_winery:" + i + "_grapes\"\n" +
                                "    }\n" +
                                "  ],\n" +
                                "  \"results\": [\n" +
                                "    {\n" +
                                "      \"fluid\": \"mjolksters_winery:create_source_" + i + "_juice\",\n" +
                                "      \"amount\": 1000,\n" +
                                "      \"id\": \"mjolksters_winery:create_source_" + i + "_juice\"\n" +
                                "    }\n" +
                                "  ],\n" +
                                "  \"conditions\": [\n" +
                                "    {\n" +
                                "      \"type\": \"neoforge:mod_loaded\",\n" +
                                "      \"modid\": \"create\"\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}"
                );
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException ignored) {
            }
        }
    }

    public static Map<String, Integer> COLOURS = new HashMap<>();
    static {
        COLOURS.put("pinot_noir", 0xFF1E0926);
        COLOURS.put("sangiovese", 0xFF200F40);
        COLOURS.put("cabernet_sauvignon", 0xFF1C0F2E);
        COLOURS.put("tempranillo", 0xFF291261);
        COLOURS.put("moondrop", 0xFF1F072E);
        COLOURS.put("autumn_royal", 0xFF1B0D40);
        COLOURS.put("ruby_roman", 0xFF820C1B);

        COLOURS.put("riesling", 0xFFACB030);
        COLOURS.put("chardonnay", 0xFFCFBD48);
        COLOURS.put("sauvignon_blanc", 0xFFB8A727);
        COLOURS.put("pinot_grigio", 0xFFD9B841);
        COLOURS.put("cotton_candy", 0xFFFFE9AA);
        COLOURS.put("grenache_blanc", 0xFFD6D060);
        COLOURS.put("waterfall", 0xFF8DBD4A);

        COLOURS.put("koshu", 0xFFFF8797);
        COLOURS.put("pinot_de_lenfer", 0xFF33060C);

        COLOURS.put("sweet_berry", 0xFFFFE9AA);
        COLOURS.put("glow_berry", 0xFFE6922C);
        COLOURS.put("chorus_fruit", 0xFF8F5CB5);

    }
}
