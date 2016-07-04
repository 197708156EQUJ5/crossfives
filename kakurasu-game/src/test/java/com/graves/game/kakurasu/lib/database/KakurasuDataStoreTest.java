package com.graves.game.kakurasu.lib.database;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KakurasuDataStoreTest
{
    private KakurasuDataStore kakurasuDataStore;

    @Before
    public void initDatabase()
    {
        System.out.println();
        System.out.println("init DB");
        kakurasuDataStore = new KakurasuDataStore();
        kakurasuDataStore.createdDatabase();
    }

    @After
    public void closeDatabase()
    {
        System.out.println("close DB");
        kakurasuDataStore.dropTables();
    }

    @Test
    public void testAddUser()
    {
        System.out.println("Add User");
        assertTrue(kakurasuDataStore.addUser("foo.bar", "somePassword"));
    }

    @Test
    public void testGetUser()
    {
        System.out.println("Get User");
        kakurasuDataStore.addUser("foo.bar", "somePassword");
        User user = kakurasuDataStore.getUser("foo.bar");
        assertEquals(user.getUserName(), "foo.bar");
    }
}
