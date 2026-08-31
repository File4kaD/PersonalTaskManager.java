package view;

import model.Task;
import model.TaskManager;
import io.XmlStorage;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // Главная панель
    private JPanel mainPanel;
    private JPanel controlPanel;

    // Панель которая рисует задачи
    private TimelinePanel timelinePanel;

    // Менеджер задач
    private TaskManager taskManager;

    // Кнопки управления задачами
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    // Кнопки навигации
    private JButton fastBackButton;
    private JButton backButton;
    private JButton todayButton;
    private JButton forwardButton;
    private JButton fastForwardButton;

    // Выбор масштаба
    private JComboBox<String> scaleComboBox;

    public MainFrame() {
        initializeFrame();
        createComponents();
        buildInterface();
        addListeners();
        setVisible(true);
    }

    // Настройка окна
    private void initializeFrame() {

        setTitle("Personal Task Manager");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // Создание компонентов.
    private void createComponents() {

        // Менеджер задач
        taskManager = new TaskManager();

        // Загружаем задачи из XML
        taskManager.setTasks(XmlStorage.load());

        // Панели
        mainPanel = new JPanel(new BorderLayout());
        controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Панель диаграммы
        timelinePanel = new TimelinePanel(taskManager);

        // Кнопки
        addButton = new JButton("+ Добавить");
        editButton = new JButton("✎ Изменить");
        deleteButton = new JButton("🗑 Удалить");

        // Масштаб
        scaleComboBox = new JComboBox<>(new String[] {"3 дня", "Неделя"});

        // Навигация
        fastBackButton = new JButton("<<");
        backButton = new JButton("<");
        todayButton = new JButton("Сегодня");
        forwardButton = new JButton(">");
        fastForwardButton = new JButton(">>");

    }

    // Сборка интерфейса
    private void buildInterface() {

        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);

        controlPanel.add(Box.createHorizontalStrut(20));

        controlPanel.add(new JLabel("Масштаб:"));
        controlPanel.add(scaleComboBox);

        controlPanel.add(Box.createHorizontalStrut(20));

        controlPanel.add(fastBackButton);
        controlPanel.add(backButton);
        controlPanel.add(todayButton);
        controlPanel.add(forwardButton);
        controlPanel.add(fastForwardButton);

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(timelinePanel, BorderLayout.CENTER);

        add(mainPanel);

    }

    // Обработчики кнопок
    private void addListeners() {

        // Добавление задачи
        addButton.addActionListener(e -> {

            TaskDialog dialog = new TaskDialog(this);

            Task task = dialog.getTask();

            if (task != null) {

                taskManager.addTask(task);

                XmlStorage.save(taskManager.getTasks());

                timelinePanel.repaint();

                System.out.println("Добавлена задача: " + task.getTitle());

            }

        });
        // Изменение масштаба
        scaleComboBox.addActionListener(e -> {

            String selectedScale = (String) scaleComboBox.getSelectedItem();

            if ("3 дня".equals(selectedScale)) {

                timelinePanel.setVisibleDays(3);

            } else if ("Неделя".equals(selectedScale)) {

                timelinePanel.setVisibleDays(7);

            }

        });
        // Навигация
        backButton.addActionListener(e ->
                timelinePanel.previousDay());

        forwardButton.addActionListener(e ->
                timelinePanel.nextDay());

        fastBackButton.addActionListener(e ->
                timelinePanel.previousPage());

        fastForwardButton.addActionListener(e ->
                timelinePanel.nextPage());

        todayButton.addActionListener(e ->
                timelinePanel.goToToday());

        // Кнопка "Изменить"
        editButton.addActionListener(e -> {

            Task selectedTask = timelinePanel.getSelectedTask();

            if (selectedTask == null) {

                JOptionPane.showMessageDialog(this, "Сначала выберите задачу.");

                return;
            }

            new TaskDialog(this, selectedTask);

            XmlStorage.save(taskManager.getTasks());

            timelinePanel.repaint();

        });
        deleteButton.addActionListener(e -> {

            Task selectedTask = timelinePanel.getSelectedTask();

            if (selectedTask == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Сначала выберите задачу."
                );

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Удалить выбранную задачу?",
                    "Подтверждение",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {

                taskManager.removeTask(selectedTask);

                XmlStorage.save(taskManager.getTasks());

                timelinePanel.clearSelection();

                timelinePanel.repaint();

            }

        });
    }

}