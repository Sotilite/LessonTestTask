package example.bot;

/**
 * Фейковый консольный бот, предназначенный для тестирования логики
 */
public class FakeConsoleBot implements Bot {
    /**
     * Хранит предыдущее отправленное сообщение пользователю
     */
    private  String previousSentMessage;

    /**
     * Хранит последнее отправленные сообщение пользователю
     */
    private String lastSentMessage;

    @Override
    public void sendMessage(Long chatId, String message) {
        previousSentMessage = lastSentMessage;
        lastSentMessage = message;
        System.out.println(message);
    }

    /**
     * Получить предыдущее отправленное сообщение
     */
    public String getPreviousSentMessage() {
        return previousSentMessage;
    }

    /**
     * Получить последнее отправленное сообщение
     */
    public String getLastSentMessage() {
        return lastSentMessage;
    }
}
