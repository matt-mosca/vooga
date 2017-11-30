package util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SpriteTemplateIoTesting {

    public static void main(String[] args) {
        SpriteTemplateIoHandler spriteTemplateExporter = new SpriteTemplateIoHandler();
        Map<String, String> map = new HashMap();
        map.put("b","c");
        Map<String, Map<String, String>> mapMap = new HashMap<>();
        mapMap.put("a", map);
        spriteTemplateExporter.exportSpriteTemplates("test", mapMap);
        Map<String, Map<String, String>> recovered = new HashMap<>();
        try {
            spriteTemplateExporter.loadSpriteTemplates("test", recovered);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(recovered);
    }

}
