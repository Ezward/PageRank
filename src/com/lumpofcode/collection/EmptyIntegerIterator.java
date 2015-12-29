package com.lumpofcode.collection;

/**
 * Created by emurphy on 12/29/15.
 */
public enum EmptyIntegerIterator implements IntegerIterator
{
    SINGLETON;

    @Override
    public boolean hasNext()
    {
        return false;
    }

    @Override
    public int next()
    {
        throw new IndexOutOfBoundsException();
    }
}
