package com.lumpofcode.math;

/**
 * Created by emurphy on 3/19/15.
 */
public final class VectorHelper
{
    private VectorHelper() {};  // don't allow this to be constructed.

    //
    // sum of the elements of the vector
    //
    public static final double vectorSumOfElements(double[] theVector)
    {
        double theSum = 0.0;
        for(double theElement : theVector)
        {
            theSum += theElement;
        }

        return theSum;
    }

    //
    // length (magnitude) of the vector,
    // the square root of the sum of the squares of the elements.
    //
    public static final double vectorMagnitude(double[] theVector)
    {
        double theSum = 0.0;
        for(double theElement : theVector)
        {
            theSum += theElement * theElement;
        }

        return Math.sqrt(theSum);

    }
}
