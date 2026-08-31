package view;

import model.Task;
import model.TaskManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TimelinePanel extends JPanel {

    private final TaskManager taskManager;
    //изменение задач
    private java.util.List<TaskView> taskViews = new java.util.ArrayList<>();

    private Task selectedTask; //хранит ссылку на кликнутом объекте

    // Первый отображаемый день
    private LocalDate firstVisibleDay;

    // Количество отображаемых дней
    private int visibleDays;

    // Размеры шапки
    private static final int HEADER_HEIGHT = 35;
    private static final int TIME_SCALE_HEIGHT = 25;

    // Размер одной задачи
    private static final int TASK_HEIGHT = 28;
    private static final int TASK_GAP = 15;

    // Отступы
    private static final int LEFT_MARGIN = 20;
    private static final int RIGHT_MARGIN = 20;

    //Выбор задачи для изменения

    public TimelinePanel(TaskManager taskManager) {

        this.taskManager = taskManager;

        firstVisibleDay = LocalDate.now();
        visibleDays = 3;

        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                for (TaskView view : taskViews) {

                    if (view.getBounds().contains(e.getPoint())) {

                        selectedTask = view.getTask();
                        repaint();
                        return;

                    }

                }

                // Если кликнули мимо задачи
                selectedTask = null;
                repaint();
            }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        drawBackground(g2);
        drawHeader(g2);
        drawGrid(g2);
        drawTasks(g2);
    }

    //Рисует фон панели
    private void drawBackground(Graphics2D g2) {

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

    }

    public void setVisibleDays(int visibleDays) {
        this.visibleDays = visibleDays;
        repaint();
    }

    private void drawHeader(Graphics2D g2) {

        g2.setColor(new Color(240,240,240));
        g2.fillRect(0,0,getWidth(),HEADER_HEIGHT);

        int dayWidth = getWidth()/visibleDays;

        for(int i=0;i<visibleDays;i++){

            LocalDate day = firstVisibleDay.plusDays(i);

            String text = String.format(
                    "%02d.%02d",
                    day.getDayOfMonth(),
                    day.getMonthValue()
            );

            int x=i*dayWidth;

            if (day.equals(LocalDate.now())) {

                g2.setColor(new Color(211, 238, 250));
                g2.fillRect(x, 0, dayWidth, getHeight());

            }

            g2.setColor(Color.GRAY);
            g2.drawLine(x,0,x,getHeight());

            g2.setColor(Color.BLACK);
            g2.drawString(
                    text,
                    x+dayWidth/2-20,
                    22
            );
        }

        g2.drawLine(0, HEADER_HEIGHT, getWidth(), HEADER_HEIGHT);

    }

    private void drawGrid(Graphics2D g2) {

        int dayWidth = getWidth() / visibleDays;

        int usableWidth = dayWidth - LEFT_MARGIN - RIGHT_MARGIN;

        double pixelsPerHour = usableWidth / 24.0;

        int top = HEADER_HEIGHT + TIME_SCALE_HEIGHT;

        for (int day = 0; day < visibleDays; day++) {

            int startX = day * dayWidth + LEFT_MARGIN;
            int hourStep = (visibleDays == 3) ? 1 : 3;

            for (int hour = 0; hour <= 24; hour++) {

                int x = startX + (int) (hour * pixelsPerHour);

                g2.setColor(new Color(225, 225, 225));
                g2.drawLine(x, top, x, getHeight());

                if (hour < 24 && hour % hourStep == 0) {

                    g2.setColor(Color.GRAY);

                    g2.drawString(
                            String.format("%02d", hour),
                            x + 2,
                            HEADER_HEIGHT + 18
                    );
                }
            }

        }

    }

    private void drawTasks(Graphics2D g2) {
        taskViews.clear();

        int y = HEADER_HEIGHT + TIME_SCALE_HEIGHT + 10;

        int dayWidth = getWidth() / visibleDays;

        int usableDayWidth = dayWidth - LEFT_MARGIN - RIGHT_MARGIN;

        double pixelsPerMinute = usableDayWidth / (24.0 * 60.0);

        for (Task task : taskManager.getTasks()) {

            long dayOffset = ChronoUnit.DAYS.between(firstVisibleDay, task.getStartDateTime().toLocalDate());

            if (dayOffset < 0 || dayOffset >= visibleDays) {
                continue;
            }

            int startMinutes = task.getStartDateTime().getHour() * 60 + task.getStartDateTime().getMinute();

            int endMinutes = task.getEndDateTime().getHour() * 60 + task.getEndDateTime().getMinute();

            int dayStartX = (int) dayOffset * dayWidth;

            int x = dayStartX + LEFT_MARGIN + (int) (startMinutes * pixelsPerMinute);

            int width = (int) ((endMinutes - startMinutes) * pixelsPerMinute);

            if (width < 20) {
                width = 20;
            }

            // Чтобы задача никогда не вылезала за пределы дня
            int maxWidth = dayWidth - RIGHT_MARGIN - (x - dayStartX);

            if (width > maxWidth) {
                width = maxWidth;
            }

            // Запоминаем координаты задачи
            Rectangle bounds = new Rectangle(
                    x,
                    y,
                    width,
                    TASK_HEIGHT
            );

            taskViews.add(new TaskView(task, bounds));

            if (task == selectedTask) {

                g2.setColor(new Color(80, 255, 229));

            } else {

                g2.setColor(new Color(120, 170, 255));

            }

            g2.fillRoundRect(x, y, width, TASK_HEIGHT, 10, 10);

            g2.setColor(Color.BLACK);
            g2.drawString(task.getTitle(), x + 5, y + 18);

            y += TASK_HEIGHT + TASK_GAP;
        }
    }

    //Перейти на один день вперед.
    public void nextDay() {

        firstVisibleDay = firstVisibleDay.plusDays(1);
        repaint();

    }

    //Перейти на один день назад.
    public void previousDay() {

        firstVisibleDay = firstVisibleDay.minusDays(1);
        repaint();

    }

    //Перейти вперед на весь отображаемый интервал.
    public void nextPage() {

        firstVisibleDay = firstVisibleDay.plusDays(visibleDays);
        repaint();

    }

    //Перейти назад на весь отображаемый интервал.
    public void previousPage() {

        firstVisibleDay = firstVisibleDay.minusDays(visibleDays);
        repaint();

    }


    //Перейти к сегодняшнему дню.
    public void goToToday() {

        if (visibleDays == 3) {

            // Сегодня будет посередине
            firstVisibleDay = LocalDate.now().minusDays(1);

        } else {

            // Сегодня по центру недели
            firstVisibleDay = LocalDate.now().minusDays(3);

        }

        repaint();

    }
    //удаление задачи и возврат нажатой
    public Task getSelectedTask() {
        return selectedTask;
    }

    public void clearSelection() {
        selectedTask = null;
    }
}


