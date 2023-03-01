package ru.sberbank.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sberbank.data.Developer;
import ru.sberbank.data.Task;
import ru.sberbank.data.TeamMember;
import ru.sberbank.data.Tester;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProcessServiceTest {

    private ProcessService processService;
    private final int EXPECTED_TASK_ID = 0;
    private final String EXPECTED_TASK_SUMMARY = "Тестовая задача";
    ArrayList<Developer> developers;
    ArrayList<Tester> testers;
    TaskService taskService;
    DeveloperService developerService;
    TesterService testerService;
    Task expectedTask;

    @BeforeEach
    void setUp() {


        taskService = mock(TaskService.class);
        expectedTask = new Task(EXPECTED_TASK_ID, EXPECTED_TASK_SUMMARY);

        developerService = mock(DeveloperService.class);
        testerService = mock(TesterService.class);

        this.processService = new ProcessService(taskService, developerService, testerService);
    }

    @Test
    void createTask_successful(){
        int actual = processService.createTask(EXPECTED_TASK_SUMMARY);
        ArrayList<Task> tasks = new ArrayList<>();
        when(taskService.getTasks()).thenReturn(tasks);
        when(taskService.createTask(EXPECTED_TASK_ID, EXPECTED_TASK_SUMMARY)).thenReturn(expectedTask);
        Assertions.assertEquals(0, actual);
        // не поняла, почему не работает
    }


    @Test
    void pushStatusTaskTest_successful(){
        ArrayList<Developer> developers = new ArrayList<>();
        developers.add(new Developer(1, "Ivan", "Ivan"));
        when(developerService.getListOfFree()).thenReturn(developers);
        expectedTask.setDeveloped(true);
        // создала девелопера. Но как присвоить таску девелоперу? она же уже по коду добавляется?

        ArrayList<Tester> testers = new ArrayList<>();
        testers.add(new Tester(1, "Oleg", "Oleg"));
        when(testerService.getListOfFree()).thenReturn(testers);
        expectedTask.setTested(true);
        // создала тестера. Но как присвоить таску тестеру? она же уже по коду добавляется?

        //expectedTask.setDeveloped(true);
        //expectedTask.setTested(true);
        Task actual = processService.pushStatusTask(EXPECTED_TASK_ID);
        Assertions.assertEquals(expectedTask, actual);
    }
    @Test
    void pushStatusTaskTest_failStatusException() {
        expectedTask.setDeveloped(true);
        expectedTask.setTested(true);
        when(taskService.getTask(EXPECTED_TASK_ID)).thenReturn(expectedTask);
        String expected = "Задача уже в финальном статусе!";

        Exception exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> {
                    processService.pushStatusTask(EXPECTED_TASK_ID);
                }
        );

        Assertions.assertEquals(expected, exception.getMessage());

    }


    @Test
    void pushStatusTaskTest_failDeveloperException() {
        expectedTask.setDeveloped(false);
        expectedTask.setTested(false);
        when(taskService.getTask(EXPECTED_TASK_ID)).thenReturn(expectedTask);
        when(developerService.getListOfFree()).thenReturn(new ArrayList<>());
        String expected = "Нет свободных разработчиков!";

        Exception exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> {
                    processService.pushStatusTask(EXPECTED_TASK_ID);
                }
        );

        Assertions.assertEquals(expected, exception.getMessage());

    }
    @Test
    void pushStatusTaskTest_failTesterException() {
        expectedTask.setDeveloped(false);
        expectedTask.setTested(false);
        when(taskService.getTask(EXPECTED_TASK_ID)).thenReturn(expectedTask);
        ArrayList<Developer> developers = new ArrayList<>();
        developers.add(new Developer(1, "Ivan", "Ivan"));
        when(developerService.getListOfFree()).thenReturn(developers);
        when(testerService.getListOfFree()).thenReturn(new ArrayList<>());

        String expected = "Нет свободных тестировщиков!";

        Exception exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> {
                    processService.pushStatusTask(EXPECTED_TASK_ID);
                }
        );

        Assertions.assertEquals(expected, exception.getMessage());

    }
}