package util.path;

import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class PathTesting {

    public static void main(String[] args) {
        PathList pathList = new PathList(new PathPoint(10,10 , Color.BLACK));
        pathList.add(new PathPoint(20, 20, Color.BLACK));
        pathList.add(new PathPoint(10, 20, Color.BLACK));
        try {
            String pathToFile = pathList.writeToSerializationFile();
            ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(pathToFile));
            PathList pathListRecovered = (PathList) objectInput.readObject();
            System.out.println(pathListRecovered.next().getX());
        } catch (ClassNotFoundException | IOException e) {
            // do nothing
            e.printStackTrace();
        }
    }
}
