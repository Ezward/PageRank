package com.lumpofcode.pagerank;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by emurphy on 2/17/15.
 */
public class HashMatrix<K extends Object> implements SparseMatrix<K>
{
    public final int rowCount;
    public final int columnCount;
    private final Map<Integer, HashVector<K>> rows;

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
        this.rows = new HashMap<>();
    }

    @Override
    public void set(final int theRowIndex, final int theColumnIndex, final K theValue)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        HashVector<K> theRow = this.rows.get(theRowIndex);
        if(null != theRow)
        {
            theRow.set(theColumnIndex, theValue);
        }
        else if(null != theValue)
        {
            // only allocate a new row if we are actually setting a value
            theRow = new HashVector<>(this.columnCount);
            this.rows.put(theRowIndex, theRow);
            theRow.set(theColumnIndex, theValue);
        }

    }

    @Override
    public K get(final int theRowIndex, final int theColumnIndex, final K theDefault)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        final HashVector<K> theRow = this.rows.get(theRowIndex);

        return (null != theRow) ? theRow.get(theColumnIndex, theDefault) : theDefault;
    }

    @Override
    public K get(final int theRowIndex, final int theColumnIndex)
    {
        return this.get(theRowIndex, theColumnIndex, null);
    }
}
