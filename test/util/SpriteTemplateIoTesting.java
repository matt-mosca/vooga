package util;

import util.io.SerializationUtils;
import util.io.SpriteTemplateIoHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SpriteTemplateIoTesting {

    public static void main(String[] args) {
        SpriteTemplateIoHandler spriteTemplateExporter = new SpriteTemplateIoHandler(new SerializationUtils());
        Map<String, Object> map = new HashMap();
        map.put("b","c");
        Map<String, Map<String, Object>> mapMap = new HashMap<>();
        mapMap.put("a", map);
        spriteTemplateExporter.exportSpriteTemplates("test", mapMap);
        try {
            Map<String, Map<String, Object>> recovered = spriteTemplateExporter.loadElementTemplates("test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
