package com.graves.game.kakurasu;

import java.awt.Color;

import javax.swing.UIManager;

import com.graves.game.kakurasu.ui.KakurasuView;

public class Kakurasu
{
    public static void main(String[] args)
    {
        UIManager.put("ToggleButton.select", Color.BLUE);
        new KakurasuView();
    }
}
