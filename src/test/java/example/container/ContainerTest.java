package example.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирует контейнер.
 * Не тестирую остальные метода, поскольку они
 * протестированы в методах добавления и удаления
 */
public class ContainerTest {
    /**
     * Объект контейнера
     */
    private Container container;

    /**
     * Проинициализировать объект контейнера перед запуском каждого теста
     */
    @BeforeEach
    public void init() {
        container = new Container();
    }

    /**
     * Протестировать добавление элемента в контейнер
     */
    @Test
    public void addItemTest() {
        Item item = new Item(5473L);

        Assertions.assertTrue(container.add(item));
        Assertions.assertEquals(1, container.size());
        Assertions.assertTrue(container.contains(item));
        Assertions.assertEquals(item, container.get(0));
    }

    /**
     * Протестировать удаление элемента из контейнера
     */
    @Test
    public void removeItemTest() {
        Item item = new Item(54745L);
        container.add(item);

        Assertions.assertTrue(container.remove(item));
        Assertions.assertEquals(0, container.size());
        Assertions.assertFalse(container.contains(item));
    }

    /**
     * Протестировать удаление элемента, которого нет в контейнере
     */
    @Test
    public void removeNonExistentItemTest() {
        Item item = new Item(905345L);

        Assertions.assertFalse(container.contains(item));
        Assertions.assertFalse(container.remove(item));
    }
}
