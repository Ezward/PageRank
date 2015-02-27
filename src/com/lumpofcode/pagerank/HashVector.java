package com.lumpofcode.pagerank;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by emurphy on 2/17/15.
 */
public class HashVector<K extends Object> implements SparseVector<K>
{
    public final int dimension;
    private final Map<Integer, K> vectorMap;

    public HashVector(final int theDimension)
    {
        if(theDimension <= 0) throw new IllegalArgumentException("theDimension must be a positive integer.");

        this.dimension = theDimension;
        this.vectorMap = new HashMap<>();
    }

    @Override
    public void set(final int theIndex, final K theValue)
    {
        if((theIndex < 0) || (theIndex >= this.dimension)) throw new IndexOutOfBoundsException();

        if(null != theValue)
        {
            this.vectorMap.put(theIndex, theValue);
        }
        else
        {
            this.vectorMap.remove(theIndex);
        }
    }

    @Override
    public K get(final int theIndex, final K theDefault)
    {
        final K theValue = this.vectorMap.get(theIndex);
        return (null != theValue) ? theValue : theDefault;
    }

    @Override
    public K get(final int theIndex)
    {
        return this.get(theIndex, null);
    }

}