package com.lumpofcode.collection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by emurphy on 2/17/15.
 */
public class HashMatrix<K extends Object> implements SparseMatrix<K>
{
    public final int rowCount;
    public final int columnCount;
    private final Map<Integer, HashVector<K>> columns;
    private static final EmptyIntegerIterator emptyIterator = EmptyIntegerIterator.SINGLETON;

    public HashMatrix(final int theSquareDimension)
    {
        this(theSquareDimension, theSquareDimension);
    }

    public HashMatrix(final int theRowCount, final int theColumnCount)
    {
        if(theRowCount <= 0) throw new IllegalArgumentException("theRowCount must be a positive integer.");
        if(theColumnCount <= 0) throw new IllegalArgumentException("theColumnCount must be a positive integer.");

        this.rowCount = theRowCount;
        this.columnCount = theColumnCount;
        this.columns = new HashMap<>();
    }

    @Override
    public void set(final int theRowIndex, final int theColumnIndex, final K theValue)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        HashVector<K> theColumn = this.columns.get(theColumnIndex);
        if(null != theColumn)
        {
            theColumn.set(theRowIndex, theValue);
        }
        else if(null != theValue)
        {
            // only allocate a new column if we are actually setting a value
            theColumn = new HashVector<>(this.rowCount);
            this.columns.put(theColumnIndex, theColumn);
            theColumn.set(theRowIndex, theValue);
        }
    }

    @Override
    public K get(final int theRowIndex, final int theColumnIndex, final K theDefault)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        final HashVector<K> theColumn = this.columns.get(theColumnIndex);

        return (null != theColumn) ? theColumn.get(theRowIndex, theDefault) : theDefault;
    }

    @Override
    public K get(final int theRowIndex, final int theColumnIndex)
    {
        return this.get(theRowIndex, theColumnIndex, null);
    }

    @Override
    public IntegerIterator columnIndices()
    {
        return new ColumnIteratorImpl();
    }

    @Override
    public IntegerIterator rowIndices(int theColumnIndex)
    {
        final HashVector<K> theColumn = this.columns.get(theColumnIndex);

        return (null != theColumn) ? theColumn.indices() : emptyIterator;
    }

    private final class ColumnIteratorImpl implements IntegerIterator
    {
        //
        // we must put the indices into a TreeSet so the come out in sorted order
        //
        private Iterator<Integer> thisIterator = new TreeSet<Integer>(columns.keySet()).iterator();

        public boolean hasNext()
        {
            return thisIterator.hasNext();
        }

        public int next()
        {
            return thisIterator.next().intValue();
        }
    }

}
