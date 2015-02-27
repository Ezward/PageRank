package com.lumpofcode.util;

/**
 * Created by emurphy on 2/16/15.
 */
public final class StringUtils
{
    private StringUtils() {};   // singleton

    public static final String trim(final String theString, final char theCharacter)
    {
        int theRight = theString.length();
        if(theRight > 0)
        {
            int theLeft = 0;
            while((theLeft < theRight) && (theCharacter == theString.charAt(theLeft)))
            {
                theLeft += 1;
            }
            while((theRight > theLeft) && (theCharacter == theString.charAt(theRight - 1)))
            {
                theRight -= 1;
            }
            return theString.substring(theLeft, theRight);
        }
        return "";
    }
}
