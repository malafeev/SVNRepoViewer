package org.svnrepoviewer.svn;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class ConnectionTest {
    private Connection connection;

    @BeforeMethod
    public void setUp() {
        connection = new Connection();
    }

    @Test
    public void testConnect() {
        assertFalse(connection.isConnected());

        connection.connect(null, null);
        assertFalse(connection.isConnected());

        connection.connect("", null);
        assertFalse(connection.isConnected());
    }

    @Test
    public void testListEntries() {
        assertTrue(connection.listEntries(null).isEmpty());
        assertTrue(connection.listEntries("").isEmpty());
        assertTrue(connection.listEntries("/").isEmpty());
    }
}
