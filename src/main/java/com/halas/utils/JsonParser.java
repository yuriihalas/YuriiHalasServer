package com.halas.utils;

import com.halas.model.Copter;
import com.halas.model.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public final class JsonParser {
    private static final Logger LOG = LogManager.getLogger(JsonParser.class);
    private static final String PATH_TO_FILE = "src/main/resources/copter.json";
    private static JSONObject jsonObject;

    static {
        createInstanceJsonObject();
    }

    private JsonParser() {
    }

    /**
     * note that previous data in file will be destroyed.
     */
    public static void writeCopterToJson(List<Copter> copters) {
        JSONObject jObj = new JSONObject();
        JSONArray arrCopters = new JSONArray();
        for (Copter copter : copters) {
            JSONObject copterObj = new JSONObject();
            addCopterToJson(copterObj, copter);
            arrCopters.add(copterObj);
            jObj.put("copters", arrCopters);
        }
        appendToJson(jObj);
    }

    private static void addCopterToJson(JSONObject copterObj, Copter cpt) {
        copterObj.put("id", cpt.getId());
        copterObj.put("name", cpt.getName());

        JSONObject objectPos = new JSONObject();
        objectPos.put("x", cpt.getPosition().getCoordinateX());
        objectPos.put("y", cpt.getPosition().getCoordinateY());
        objectPos.put("z", cpt.getPosition().getCoordinateZ());

        copterObj.put("position", objectPos);
    }

    private static void appendToJson(JSONObject jObj) {
        try (FileWriter file = new FileWriter(PATH_TO_FILE)) {
            file.write(jObj.toJSONString());
            LOG.info("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            LOG.error("Can't parse JSON..");
            LOG.error(e.getStackTrace());
        }
    }

    public static List<Copter> readAllCopters() {
        List<Copter> copters = new ArrayList<>();
        int sizeCopters = getAmountCopters();
        LOG.debug("size: " + sizeCopters);
        for (int i = 0; i < sizeCopters; i++) {
            copters.add(i, getCopterByIndex(i));
        }
        LOG.debug(copters);
        return copters;
    }

    private static void createInstanceJsonObject() {
        try {
            jsonObject = (JSONObject) (new JSONParser().parse(new FileReader(PATH_TO_FILE)));
        } catch (IOException | ParseException e) {
            LOG.error("Cannot find file json..");
            LOG.error("Class: " + e.getClass());
            LOG.error("Message: " + e.getMessage());
            LOG.error(e.getStackTrace());
        }
    }

    private static Copter getCopterByIndex(int index) {
        Copter copter = new Copter();
        int idCopter = getIdCopter(index);
        copter.setId(idCopter);
        String nameCopter = getNameCopter(index);
        copter.setName(nameCopter);
        Position possCopter = getPositionByIndex(index);
        copter.setPosition(possCopter);
        return copter;
    }

    private static int getIdCopter(int index) {
        return Integer.parseInt(getCopterJson(index).get("id").toString());
    }

    private static String getNameCopter(int index) {
        return getCopterJson(index).get("name").toString();
    }

    private static Position getPositionByIndex(int index) {
        Position position = getPositionFromJson(index);
        LOG.debug(position);
        return position;
    }

    private static int getAmountCopters() {
        return ((JSONArray) jsonObject.get("copters")).size();
    }

    private static JSONArray getCopters() {
        return (JSONArray) jsonObject.get("copters");
    }

    private static JSONObject getCopterJson(int index) {
        return ((JSONObject) getCopters().get(index));
    }

    private static String getPosition(int index, String poss) {
        return ((JSONObject) getCopterJson(index).get("position")).get(poss).toString();
    }

    private static Position getPositionFromJson(int index) {
        double posX = Double.parseDouble(getPosition(index, "x"));
        double posY = Double.parseDouble(getPosition(index, "y"));
        double posZ = Double.parseDouble(getPosition(index, "z"));
        return new Position(posX, posY, posZ);
    }
}
