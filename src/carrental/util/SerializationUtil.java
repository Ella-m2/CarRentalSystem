package carrental.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerializationUtil {
    private static final Logger logger = Logger.getLogger(SerializationUtil.class.getName());

    // Private constructor to prevent instantiation
    private SerializationUtil() {}

    public static void serializeObject(Object obj, String filePath) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void loadDatabaseFromFile(String filePath, Map<String, T> database) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            Object loadedObject = inputStream.readObject();

            if (loadedObject instanceof Map) {
                database.clear();
                database.putAll((Map<String, T>) loadedObject);
            } else {
                logger.warning("Loaded object is null or not an instance of Map");
            }
        } catch (FileNotFoundException e) {
            logger.warning("Database file not found. Creating a new database.");
            // If the file is not found, create a new instance of the database
            database = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error loading database from file", e);
        }
    }

    public static <T> T deserializeObject(String filePath, Class<T> myClass) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            Object obj = inputStream.readObject();
            if (myClass.isInstance(obj)) {
                return myClass.cast(obj);
            } else {
                System.out.println("Invalid file content. Unable to deserialize " + myClass.getSimpleName());
                return null;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Creating a new " + myClass.getSimpleName() + ".");
            try {
                return myClass.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}