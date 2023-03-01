package ru.sberbank.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sberbank.data.Developer;
import ru.sberbank.data.TeamMember;
import ru.sberbank.data.Tester;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleServiceTest {

    private SimpleService simpleService;

    @BeforeEach
    void setUp(){
        // замокировали объекты, которые используются в SimpleService
        DeveloperService developerService = mock(DeveloperService.class);
        TesterService testerService = mock(TesterService.class);
        TaskService taskService = mock(TaskService.class);

        // готовим данные для условий мокирования ниже when-then
        ArrayList<Developer> developer = new ArrayList();
        developer.add(new Developer(1, "Ivan", "Ivan"));
        ArrayList<Tester> tester = new ArrayList();
        tester.add(new Tester(1, "Oleg", "Oleg"));

        // структура для мокирования "когда/ тогда верни"
        when(developerService.getListOfFree()).thenReturn(developer);
        when(testerService.getListOfFree()).thenReturn(tester);

        // определили объект для тестирования в фикстуре, чтобы не дублировать код, если тестов будет 20Фкк
        this.simpleService = new SimpleService(developerService, testerService, taskService);

    }


    @Test
    void simpleMethodTest_successful() {
        ArrayList<TeamMember> actual = simpleService.simpleMethod();
        Assertions.assertEquals(2, actual.size());
        // 2, потому что возвращаем 2 объекта
    }
}