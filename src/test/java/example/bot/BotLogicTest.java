package example.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирует верхнеуровневую логику бота
 */
public class BotLogicTest {
    /**
     * Пользователь бота
     */
    private User user;

    /**
     * Фейковый бот для тестирования логики
     */
    private FakeConsoleBot bot;

    /**
     * Объект верхнеуровневой логики бота
     */
    private BotLogic botLogic;

    /**
     * Проинициализировать все объекты перед запуском каждого теста
     */
    @BeforeEach
    public void init() {
        user = new User(67563L);
        bot = new FakeConsoleBot();
        botLogic = new BotLogic(bot);
    }

    /**
     * Протестировать команду запуска теста, проверив логику при
     * обработке правильного/неправильного ответа на вопросы
     */
    @Test
    public void testCommandTest() {
        botLogic.processCommand(user, "/start");
        Assertions.assertEquals(State.INIT, user.getState());
        Assertions.assertEquals("Привет!", bot.getLastSentMessage());

        botLogic.processCommand(user, "/test");
        Assertions.assertEquals(State.TEST, user.getState());
        Assertions.assertEquals(
                "Вычислите степень: 10^2",
                bot.getLastSentMessage()
        );

        botLogic.processCommand(user, "100");
        Assertions.assertEquals(
                "Правильный ответ!",
                bot.getPreviousSentMessage());
        Assertions.assertEquals(
                "Сколько будет 2 + 2 * 2",
                bot.getLastSentMessage());

        botLogic.processCommand(user, "8");
        Assertions.assertEquals(
                "Вы ошиблись, верный ответ: 6",
                bot.getPreviousSentMessage()
        );
        Assertions.assertEquals(
                "Тест завершен",
                bot.getLastSentMessage()
        );
        Assertions.assertEquals(State.INIT, user.getState());
    }

    /**
     * Протестировать команду отправки уведомления.
     */
    @Test
    public void testCommandNotify() throws InterruptedException {
        botLogic.processCommand(user, "/notify");
        Assertions.assertEquals(State.SET_NOTIFY_TEXT, user.getState());
        Assertions.assertEquals(
                "Введите текст напоминания",
                bot.getLastSentMessage()
        );

        botLogic.processCommand(user, "Купить хлеба");
        Assertions.assertEquals(
                "Через сколько секунд напомнить?",
                bot.getLastSentMessage()
        );
        Assertions.assertEquals(State.SET_NOTIFY_DELAY, user.getState());

        botLogic.processCommand(user, "5");
        Assertions.assertEquals(
                "Напоминание установлено",
                bot.getLastSentMessage()
        );

        Thread.sleep(1000);
        Assertions.assertNotEquals(
                "Сработало напоминание: 'Купить хлеба'",
                bot.getLastSentMessage()
        );
        //Добавил 100 мс, потому что не всегда успевает установиться новое сообщение
        Thread.sleep(4100);
        Assertions.assertEquals(
                "Сработало напоминание: 'Купить хлеба'",
                bot.getLastSentMessage()
        );
        Assertions.assertEquals(State.INIT, user.getState());
    }

    /**
     * Протестировать команду повторного задавания вопроса
     */
    @Test
    public void testCommandRepeat() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "100");
        botLogic.processCommand(user, "8");

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals(State.REPEAT, user.getState());
        Assertions.assertEquals(
                "Сколько будет 2 + 2 * 2",
                bot.getLastSentMessage()
        );

        botLogic.processCommand(user, "6");
        Assertions.assertEquals(
                "Правильный ответ!",
                bot.getPreviousSentMessage()
        );
        Assertions.assertEquals(
                "Тест завершен",
                bot.getLastSentMessage()
        );
        Assertions.assertEquals(State.INIT, user.getState());
    }

    //Остальные команды не были протестированы, так как,
    //во-первых, этого не требует условие задачи, во-вторых,
    //команда help просто возвращает статичный текст,
    //команда start протестирована выше
    //команда stop вызывает processStop, который уже протестирован
}
