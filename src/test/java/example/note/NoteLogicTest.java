package example.note;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирует логику по работе с заметками
 */
public class NoteLogicTest {
    /**
     * Объект логики работы с заметками
     */
    private NoteLogic logic;

    /**
     * Проинициализировать объект логики работы с заметками перед запуском каждого теста
     */
    @BeforeEach
    public void init() {
        logic = new NoteLogic();
    }

    /**
     * Протестировать добавление заметки
     */
    @Test
    public void addNoteTest() {
        Assertions.assertEquals(
                "Note added!",
                logic.handleMessage("/add My_note")
        );
        Assertions.assertEquals(
                "Your notes:\nMy_note",
                logic.handleMessage("/notes")
        );
    }

    /**
     * Протестировать изменение заметки
     */
    @Test
    public void editNoteTest() {
        Assertions.assertEquals(
                "Note edited!",
                logic.handleMessage("/edit My_note Your_note")
        );
        Assertions.assertEquals(
                "Your notes:\nYour_note",
                logic.handleMessage("/notes")
        );
    }

    /**
     * Протестировать удаление заметки
     */
    @Test
    public void deleteNoteTest() {
        logic.handleMessage("/add My_note");
        logic.handleMessage("/add Your_note");

        Assertions.assertEquals(
                "Note deleted!",
                logic.handleMessage("/del My_note")
        );
        Assertions.assertEquals(
                "Your notes:\nYour_note",
                logic.handleMessage("/del My_note")
        );
    }

    //Не тестирую команду "/notes", поскольку она тестируется
    //в методах добавления, редактирования и удаления

    //Не тестирую не известную команду, поскольку тест
    //всегда будет проходить, если команда не распознана
}
