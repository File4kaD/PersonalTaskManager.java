package view;

import model.Task;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TaskDialog extends JDialog {

    // Поля ввода
    private JTextField titleField;
    private JTextField startField;
    private JTextField endField;
    private JTextArea descriptionArea;

    // Кнопки
    private JButton saveButton;
    private JButton cancelButton;

    // Созданная задача
    private Task task;

    // Формат даты
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public TaskDialog(JFrame owner) {

        super(owner, "Новая задача", true);

        initializeDialog();
        createComponents();
        buildInterface();
        addListeners();
        setVisible(true);
    }
    public TaskDialog(JFrame owner, Task task) {

        super(owner, "Изменение задачи", true);

        this.task = task;

        initializeDialog();
        createComponents();
        buildInterface();

        // Заполняем поля
        titleField.setText(task.getTitle());
        descriptionArea.setText(task.getDescription());

        startField.setText(task.getStartDateTime().format(formatter));

        endField.setText(task.getEndDateTime().format(formatter));

        addListeners();

        setVisible(true);
    }

    //Настройка окна
    private void initializeDialog() {

        setSize(500, 450);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    // Создание компоненто
    private void createComponents() {

        titleField = new JTextField();

        startField = new JTextField(
                "24.07.2026 10:00"
        );

        endField = new JTextField(
                "24.07.2026 12:00"
        );

        descriptionArea = new JTextArea(5,20);

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        saveButton = new JButton("Сохранить");
        cancelButton = new JButton("Отмена");
    }

    // Построение интерфейса
    private void buildInterface() {

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Название

        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(new JLabel("Название"), gbc);

        gbc.gridy = 1;

        panel.add(titleField, gbc);

        // Описание

        gbc.gridy = 2;

        panel.add(new JLabel("Описание"), gbc);

        gbc.gridy = 3;

        JScrollPane scrollPane =
                new JScrollPane(descriptionArea);

        panel.add(scrollPane, gbc);

        // Начало

        gbc.gridy = 4;

        panel.add(
                new JLabel("Начало (дд.ММ.гггг ЧЧ:мм)"),
                gbc
        );

        gbc.gridy = 5;

        panel.add(startField, gbc);

        // Конец

        gbc.gridy = 6;

        panel.add(
                new JLabel("Конец (дд.ММ.гггг ЧЧ:мм)"),
                gbc
        );

        gbc.gridy = 7;

        panel.add(endField, gbc);

        // Панель кнопок

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridy = 8;

        panel.add(buttonPanel, gbc);

        add(panel);
    }

    // Обработчики кнопок
    private void addListeners() {

        // Кнопка Сохранить
        saveButton.addActionListener(e -> {

            try {

                String title = titleField.getText().trim();
                String description = descriptionArea.getText().trim();

                // Проверяем название
                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Введите название задачи!",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // Преобразуем строки в LocalDateTime
                LocalDateTime startDateTime =
                        LocalDateTime.parse(startField.getText().trim(), formatter);

                LocalDateTime endDateTime =
                        LocalDateTime.parse(endField.getText().trim(), formatter);

                // Проверяем даты
                if (endDateTime.isBefore(startDateTime)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Дата окончания должна быть позже даты начала!",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }
                //выводим ошибку если задача не в течении дня
                if (!startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Задача должна начинаться и заканчиваться в один день!",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }
                // Создаем задачу

                if (task == null) {

                    task = new Task(
                            title,
                            description,
                            startDateTime,
                            endDateTime
                    );

                } else {

                    task.setTitle(title);
                    task.setDescription(description);
                    task.setStartDateTime(startDateTime);
                    task.setEndDateTime(endDateTime);

                }

                dispose();

            } catch (DateTimeParseException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Неверный формат даты!\nИспользуйте: дд.ММ.гггг ЧЧ:мм",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );

            }

        });

        // Кнопка "Отмена"
        cancelButton.addActionListener(e -> dispose());

    }

    //, Возвращает созданную задачу. Если нажали "Отмена", то метод вернет null.
    public Task getTask() {
        return task;
    }

}