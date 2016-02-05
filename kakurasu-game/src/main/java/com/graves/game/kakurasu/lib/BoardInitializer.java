package com.graves.game.kakurasu.lib;

import java.util.Random;

public class BoardInitializer
{
    private static int[] validList;
    private static int[] validEasyList;
    private static int[] validMediumList;
    private static int[] validHardList;

    public static void initialize()
    {
        int validCount = 0;
        int validEasyCount = 0;
        int validMediumCount = 0;
        int validHardCount = 0;
        int count = (int) Math.pow(2, 25);
        int[] list = new int[count];
        int[] easyList = new int[count];
        int[] mediumList = new int[count];
        int[] hardList = new int[count];
        for (int i = 0; i < count; i++)
        {
            int row1 = 1 * ((i & 0x1) >> 0) + 2 * ((i & 0x2) >> 1) + 3 * ((i & 0x4) >> 2)
                    + 4 * ((i & 0x8) >> 3) + 5 * ((i & 0x10) >> 4);
            int row2 = 1 * ((i & 0x20) >> 5) + 2 * ((i & 0x40) >> 6) + 3 * ((i & 0x80) >> 7)
                    + 4 * ((i & 0x100) >> 8) + 5 * ((i & 0x200) >> 9);
            int row3 = 1 * ((i & 0x400) >> 10) + 2 * ((i & 0x800) >> 11) + 3 * ((i & 0x1000) >> 12)
                    + 4 * ((i & 0x2000) >> 13) + 5 * ((i & 0x4000) >> 14);
            int row4 = 1 * ((i & 0x8000) >> 15) + 2 * ((i & 0x10000) >> 16)
                    + 3 * ((i & 0x20000) >> 17) + 4 * ((i & 0x40000) >> 18)
                    + 5 * ((i & 0x80000) >> 19);
            int row5 = 1 * ((i & 0x100000) >> 20) + 2 * ((i & 0x200000) >> 21)
                    + 3 * ((i & 0x400000) >> 22) + 4 * ((i & 0x800000) >> 23)
                    + 5 * ((i & 0x1000000) >> 24);
            int col1 = 1 * ((i & 0x1) >> 0) + 2 * ((i & 0x20) >> 5) + 3 * ((i & 0x400) >> 10)
                    + 4 * ((i & 0x8000) >> 15) + 5 * ((i & 0x100000) >> 20);
            int col2 = 1 * ((i & 0x2) >> 1) + 2 * ((i & 0x40) >> 6) + 3 * ((i & 0x800) >> 11)
                    + 4 * ((i & 0x10000) >> 16) + 5 * ((i & 0x200000) >> 21);
            int col3 = 1 * ((i & 0x4) >> 2) + 2 * ((i & 0x80) >> 7) + 3 * ((i & 0x1000) >> 12)
                    + 4 * ((i & 0x20000) >> 17) + 5 * ((i & 0x400000) >> 22);
            int col4 = 1 * ((i & 0x8) >> 3) + 2 * ((i & 0x100) >> 8) + 3 * ((i & 0x2000) >> 13)
                    + 4 * ((i & 0x40000) >> 18) + 5 * ((i & 0x800000) >> 23);
            int col5 = 1 * ((i & 0x16) >> 4) + 2 * ((i & 0x200) >> 9) + 3 * ((i & 0x4000) >> 14)
                    + 4 * ((i & 0x80000) >> 19) + 5 * ((i & 0x1000000) >> 24);
            if (row1 != 0 && row2 != 0 && row3 != 0 && row4 != 0 && row5 != 0 && col1 != 0
                    && col2 != 0 && col3 != 0 && col4 != 0 && col5 != 0 && row1 != row2
                    && row1 != row3 && row1 != row4 && row1 != row5 && row2 != row3 && row2 != row4
                    && row2 != row5 && row3 != row4 && row3 != row5 && row4 != row5 && col1 != col2
                    && col1 != col3 && col1 != col4 && col1 != col5 && col2 != col3 && col2 != col4
                    && col2 != col5 && col3 != col4 && col3 != col5 && col4 != col5)
            {
                list[i] = 1;
                validCount++;
                int modeCount = 0;
                modeCount += gameLevel(row1);
                modeCount += gameLevel(row2);
                modeCount += gameLevel(row3);
                modeCount += gameLevel(row4);
                modeCount += gameLevel(row5);
                modeCount += gameLevel(col1);
                modeCount += gameLevel(col2);
                modeCount += gameLevel(col3);
                modeCount += gameLevel(col4);
                modeCount += gameLevel(col5);

                if ((modeCount & 0xf) >= 2)
                {
                    easyList[i] = 1;
                    validEasyCount++;
                }
                else if ((modeCount & 0xf) == 0 && (modeCount & 0xf0) >= 0x10)
                {
                    mediumList[i] = 1;
                    validMediumCount++;
                }
                else if ((modeCount & 0xf00) == 0xA00)
                {
                    hardList[i] = 1;
                    validHardCount++;
                }
            }
        }

        validList = new int[validCount];
        int validIndex = 0;
        for (int i = 0; i < list.length; i++)
        {
            if (list[i] > 0)
            {
                validList[validIndex] = i;
                validIndex++;
            }
        }

        validEasyList = new int[validEasyCount];
        validIndex = 0;
        for (int i = 0; i < easyList.length; i++)
        {
            if (easyList[i] > 0)
            {
                validEasyList[validIndex] = i;
                validIndex++;
            }
        }

        validMediumList = new int[validMediumCount];
        validIndex = 0;
        for (int i = 0; i < mediumList.length; i++)
        {
            if (mediumList[i] > 0)
            {
                validMediumList[validIndex] = i;
                validIndex++;
            }
        }

        validHardList = new int[validHardCount];
        validIndex = 0;
        for (int i = 0; i < hardList.length; i++)
        {
            if (hardList[i] > 0)
            {
                validHardList[validIndex] = i;
                validIndex++;
            }
        }
    }

    public static int getIndex(int boardIndex)
    {
        return validList[boardIndex];
    }

    private static int gameLevel(int rowColValue)
    {
        if (rowColValue == 1 || rowColValue == 2 || rowColValue == 13 || rowColValue == 14
                || rowColValue == 15)
        {
            return 1;
        }
        if (rowColValue == 11 || rowColValue == 12)
        {
            return 16;
        }
        return 256;
    }

    public static Board createBoard()
    {
        int count = validList.length;
        int boardIndex = new Random().nextInt(count);
        int index = validList[boardIndex];

        return new Board(index, boardIndex);
    }

    public static Board createEasyBoard()
    {
        int count = validEasyList.length;
        int boardIndex = new Random().nextInt(count);
        int index = validEasyList[boardIndex];

        return new Board(index, findIndex(index));
    }

    public static Board createMediumBoard()
    {
        int count = validMediumList.length;
        int boardIndex = new Random().nextInt(count);
        int index = validMediumList[boardIndex];

        return new Board(index, findIndex(index));
    }

    public static Board createHardBoard()
    {
        int count = validHardList.length;
        int boardIndex = new Random().nextInt(count);
        int index = validHardList[boardIndex];

        return new Board(index, findIndex(index));
    }

    private static int findIndex(int index)
    {
        for (int i = 0; i < validList.length; i++)
        {
            if (validList[i] == index)
            {
                return i;
            }
        }
        return -1;
    }
}
