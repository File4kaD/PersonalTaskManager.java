package io;

import model.Task;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class XmlStorage {

    // Файл, в котором будут храниться задачи
    private static final String FILE_NAME = "tasks.xml";

    // Сохраняет список задач

    public static void save(List<Task> tasks) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("tasks");
            document.appendChild(root);

            for (Task task : tasks) {
                Element taskElement = document.createElement("task");
                root.appendChild(taskElement);

                Element title = document.createElement("title");
                title.setTextContent(task.getTitle());
                taskElement.appendChild(title);

                Element description = document.createElement("description");
                description.setTextContent(task.getDescription());
                taskElement.appendChild(description);

                Element start = document.createElement("start");
                start.setTextContent(task.getStartDateTime().toString());
                taskElement.appendChild(start);

                Element end = document.createElement("end");
                end.setTextContent(task.getEndDateTime().toString());
                taskElement.appendChild(end);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(getFile()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Загружает список задач.

    public static List<Task> load() {

        List<Task> tasks = new ArrayList<>();

        if (!getFile().exists()) {
            return tasks;
        }

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(getFile());

            NodeList taskNodes = document.getElementsByTagName("task");

            for (int i = 0; i < taskNodes.getLength(); i++) {

                Element taskElement = (Element) taskNodes.item(i);

                String title = taskElement
                        .getElementsByTagName("title")
                        .item(0)
                        .getTextContent();

                String description = taskElement
                        .getElementsByTagName("description")
                        .item(0)
                        .getTextContent();

                LocalDateTime start = LocalDateTime.parse(
                        taskElement
                                .getElementsByTagName("start")
                                .item(0)
                                .getTextContent()
                );

                LocalDateTime end = LocalDateTime.parse(
                        taskElement
                                .getElementsByTagName("end")
                                .item(0)
                                .getTextContent()
                );

                Task task = new Task(
                        title,
                        description,
                        start,
                        end
                );

                tasks.add(task);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return tasks;

    }

    // Возвращает файл XML.

    private static File getFile() {

        return new File(FILE_NAME);

    }

}