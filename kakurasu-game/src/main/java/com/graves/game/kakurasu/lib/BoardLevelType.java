package com.graves.game.kakurasu.lib;

public enum BoardLevelType
{
    RANDOM("All"), EASY("Easy"), MEDIUM("Medium"), HARD("Hard");
    private String type;

    private BoardLevelType(String type)
    {
        this.type = type;
    }

    public String getLevel()
    {
        return this.type;
    }
}
