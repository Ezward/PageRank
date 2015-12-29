package com.lumpofcode.collection;

/**
 * Created by emurphy on 12/29/15.
 */
public enum EmptyDoubleIterator implements DoubleIterator
{
    SINGLETON;

    @Override
    public boolean hasNext()
    {
        return false;
    }

    @Override
    public double next()
    {
        throw new IndexOutOfBoundsException();
    }
}
