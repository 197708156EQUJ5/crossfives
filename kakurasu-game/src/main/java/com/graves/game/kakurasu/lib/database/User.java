package com.graves.game.kakurasu.lib.database;

public class User
{
    private int id;
    private String userName;
    private byte[] encryptedPassword;
    private byte[] salt;

    public User(int id, String userName, byte[] encryptedPassword, byte[] salt)
    {
        super();
        this.id = id;
        this.userName = userName;
        this.encryptedPassword = encryptedPassword;
        this.salt = salt;
    }

    public int getId()
    {
        return this.id;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public byte[] getEncryptedPassword()
    {
        return this.encryptedPassword;
    }
    
    public byte[] getSalt()
    {
        return this.salt;
    }
}
