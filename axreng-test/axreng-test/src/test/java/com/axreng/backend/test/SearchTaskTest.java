package com.axreng.backend.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.axreng.backend.SearchTask;

public class SearchTaskTest {

    @Test
    public void testValidKeywordLength() {
        // Valid keyword length should not throw an exception
        assertDoesNotThrow(() -> new SearchTask("test")); // Minimum length
        assertDoesNotThrow(() -> new SearchTask("shorterKeyword")); // Between minimum and maximum length
        assertDoesNotThrow(() -> new SearchTask("keywordWithExactly32Characters")); // Maximum length
    }

    @Test
    public void testInvalidKeywordLength() {
        // Invalid keyword length should throw an exception
        assertThrows(IllegalArgumentException.class, () -> new SearchTask(null)); // Null keyword
        assertThrows(IllegalArgumentException.class, () -> new SearchTask("")); // Empty keyword
        assertThrows(IllegalArgumentException.class, () -> new SearchTask("abc")); // Below minimum length
        assertThrows(IllegalArgumentException.class, () -> new SearchTask("keywordWithTooManyCharacters1234567890")); // Above maximum length
    }

    @Test
    public void testTaskIdGeneration() {
        // Create a search task with a keyword
        SearchTask searchTask = new SearchTask("test");
        
        // Get the generated ID
        String taskId = searchTask.getId();
        
        // Check if the ID has 8 characters
        assertEquals(8, taskId.length());
        
        // Check if the ID is alphanumeric
        assertTrue(taskId.matches("[a-zA-Z0-9]+"));
    }
}
