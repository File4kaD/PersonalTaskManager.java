package test;

import model.Task;
import org.junit.Test;
import io.XmlStorage;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class XmlStorageTest {

    @Test
    public void saveLoadTest() {

        List<Task> tasks = new ArrayList<>();

        tasks.add(new Task(
                "Task",
                "Description",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        ));

        XmlStorage.save(tasks);

        List<Task> loaded = XmlStorage.load();

        assertEquals(tasks.size(), loaded.size());

        assertEquals(
                tasks.get(0).getTitle(),
                loaded.get(0).getTitle()
        );

        assertEquals(
                tasks.get(0).getDescription(),
                loaded.get(0).getDescription()
        );
        assertEquals(
                tasks.get(0).getStartDateTime(),
                loaded.get(0).getStartDateTime()
        );

        assertEquals(
                tasks.get(0).getEndDateTime(),
                loaded.get(0).getEndDateTime()
        );
    }

    @Test
    public void loadWithoutFileTest() {

        File file = new File("tasks.xml");

        if (file.exists()) {
            file.delete();
        }

        List<Task> tasks = XmlStorage.load();

        assertNotNull(tasks);

        assertTrue(tasks.isEmpty());
    }


}