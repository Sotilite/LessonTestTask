package example.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирует верхнеуровневую логику бота.
 * Остальные команды не были протестированы, так как,
 * команда help просто возвращает статичный текст,
 * команда start протестирована ниже
 */
public class BotLogicTest {
    /**
     * Пользователь бота
     */
    private User user;

    /**
     * Фейковый бот для тестирования логики
     */
    private FakeBot bot;

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
        bot = new FakeBot();
        botLogic = new BotLogic(bot);
    }

    /**
     * Протестировать команду запуска теста, проверив логику
     * при обработке правильных ответов на вопросы
     */
    @Test
    public void testCommandTestWithCorrectAnswers() {
        botLogic.processCommand(user, "/start");
        Assertions.assertEquals("Привет!", bot.getLastSentMessage());

        botLogic.processCommand(user, "/test");
        Assertions.assertEquals(
                "Вычислите степень: 10^2",
                bot.getLastSentMessage()
        );

        botLogic.processCommand(user, "100");
        Assertions.assertEquals(
                "Правильный ответ!",
                bot.getPreviousSentMessage()
        );
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
    }

    /**
     * Протестировать команду запуска теста, проверив логику
     * при обработке неправильных ответов на вопросы
     */
    @Test
    public void testCommandTestWithIncorrectAnswers() {
        botLogic.processCommand(user, "/test");
        Assertions.assertEquals(
                "Вычислите степень: 10^2",
                bot.getLastSentMessage()
        );

        botLogic.processCommand(user, "110");
        Assertions.assertEquals(
                "Вы ошиблись, верный ответ: 100",
                bot.getPreviousSentMessage()
        );
        Assertions.assertEquals(
                "Сколько будет 2 + 2 * 2",
                bot.getLastSentMessage()
        );

        botLogic.processCommand(user, "8");
        Assertions.assertEquals(
                "Вы ошиблись, верный ответ: 6",
                bot.getPreviousSentMessage()
        );
        Assertions.assertEquals(
                "Тест завершен",
                bot.getLastSentMessage()
        );
    }

    /**
     * Протестировать команду отправки уведомления.
     */
    @Test
    public void testCommandNotify() throws InterruptedException {
        botLogic.processCommand(user, "/notify");
        Assertions.assertEquals(
                "Введите текст напоминания",
                bot.getLastSentMessage()
        );

        botLogic.processCommand(user, "Купить хлеба");
        Assertions.assertEquals(
                "Через сколько секунд напомнить?",
                bot.getLastSentMessage()
        );

        botLogic.processCommand(user, "1");
        Assertions.assertEquals(
                "Напоминание установлено",
                bot.getLastSentMessage()
        );

        Thread.sleep(900);
        Assertions.assertNotEquals(
                "Сработало напоминание: 'Купить хлеба'",
                bot.getLastSentMessage()
        );
        Thread.sleep(120);
        Assertions.assertEquals(
                "Сработало напоминание: 'Купить хлеба'",
                bot.getLastSentMessage()
        );
    }

    /**
     * Протестировать команду повторного задавания вопроса.
     * Решил объединить тестирование граничных случаев в один метод,
     * поскольку их воспроизведение последовательно, также будет
     * дублирование кода, если разбить на несколько методов
     */
    @Test
    public void testCommandRepeat() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "100");
        botLogic.processCommand(user, "8");

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals(
                "Сколько будет 2 + 2 * 2",
                bot.getLastSentMessage()
        );
        botLogic.processCommand(user, "8");
        Assertions.assertEquals(
                "Вы ошиблись, верный ответ: 6",
                bot.getPreviousSentMessage()
        );
        Assertions.assertEquals(
                "Тест завершен",
                bot.getLastSentMessage()
        );

        botLogic.processCommand(user, "/repeat");
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

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals(
                "Нет вопросов для повторения",
                bot.getLastSentMessage()
        );
    }

    /**
     * Протестировать команду остановки, когда не началось тестирование
     */
    @Test
    public void testCommandStopWhenTestNotStart() {
        botLogic.processCommand(user, "/stop");
        Assertions.assertEquals(
              "Вы не начинали тестирование. Воспользуйтесь командой " +
                      "/help, чтобы прочитать инструкцию.",
              bot.getLastSentMessage()
        );
    }

    /**
     * Протестировать команду остановки, когда не началось тестирование
     */
    @Test
    public void testCommandStopWhenNotify() {
        botLogic.processCommand(user, "/notify");

        botLogic.processCommand(user, "/stop");
        Assertions.assertEquals(
                "Вы не начинали тестирование. Воспользуйтесь командой " +
                        "/help, чтобы прочитать инструкцию.",
                bot.getLastSentMessage()
        );
    }

    /**
     * Протестировать команду остановки во время теста
     */
    @Test
    public void testCommandStopWhenTest() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "100");

        botLogic.processCommand(user, "/stop");
        Assertions.assertEquals(
                "Тест завершен",
                bot.getLastSentMessage()
        );
    }

    /**
     * Протестировать команду остановки во время повторения
     */
    @Test
    public void testCommandStopWhenRepeat() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "100");
        botLogic.processCommand(user, "10");

        botLogic.processCommand(user, "/repeat");

        botLogic.processCommand(user, "/stop");
        Assertions.assertEquals(
                "Тест завершен",
                bot.getLastSentMessage()
        );
    }
}
