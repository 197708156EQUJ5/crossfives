package com.graves.game.kakurasu.util;

public class Util
{
    public static void main(String[] args)
    {
        for (int i = 1; i <= 15; i++)
        {
            int sum = 0;
            StringBuffer sb = new StringBuffer();
            for (int j = 1; j <= 5; j++)
            {
                sum += j;
                sb.append(j);
                if(sum >= i)
                {
                    break;
                }
            }
            System.out.println(i + ": " + sb.toString());
            
        }
    }
}
