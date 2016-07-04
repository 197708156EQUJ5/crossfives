package com.graves.game.kakurasu.lib.database;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KakurasuDataStoreTest
{

    @Before
    public void initDatabase()
    {
        System.out.println();
        System.out.println("init DB");
        KakurasuDataStore.createdDatabase();
    }

    @After
    public void closeDatabase()
    {
        System.out.println("close DB");
        KakurasuDataStore.dropTables();
    }

    @Test
    public void testAddUser()
    {
        System.out.println("Add User");
        assertTrue(KakurasuDataStore.addUser("foo.bar", "somePassword"));
    }

    @Test
    public void testGetUser()
    {
        System.out.println("Get User");
        KakurasuDataStore.addUser("foo.bar", "somePassword");
        User user = KakurasuDataStore.getUser("foo.bar");
        assertEquals(user.getUserName(), "foo.bar");
    }
}
