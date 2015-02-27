package com.lumpofcode.pagerank;

/**
 * Created by emurphy on 2/26/15.
 */
public class SparseGrowableMatrix<K extends Object> implements SparseMatrix<K>
{
    public final int rowCount;
    public final int columnCount;
    private final int blockSize;
    private final SparseGrowableVector<SparseGrowableVector<K>> rows;

    public SparseGrowableMatrix(final int theSquareDimension, final int theBlockSize)
    {
        this(theSquareDimension, theSquareDimension, theBlockSize);
    }

    public SparseGrowableMatrix(final int theRowCount, final int theColumnCount, final int theBlockSize)
    {
        if(theRowCount <= 0) throw new IllegalArgumentException("theRowCount must be a positive integer.");
        if(theColumnCount <= 0) throw new IllegalArgumentException("theColumnCount must be a positive integer.");

        this.rowCount = theRowCount;
        this.columnCount = theColumnCount;

        this.blockSize = theBlockSize;
        this.rows = new SparseGrowableVector<SparseGrowableVector<K>>(theBlockSize);
    }

    @Override
    public void set(final int theRowIndex, final int theColumnIndex, final K theValue)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        SparseGrowableVector<K> theRow = this.rows.get(theRowIndex);
        if(null != theRow)
        {
            theRow.set(theColumnIndex, theValue);
        }
        else if(null != theValue)
        {
            // only allocate a new row if we are actually setting a value
            theRow = new SparseGrowableVector<>(this.blockSize);
            this.rows.set(theRowIndex, theRow);
            theRow.set(theColumnIndex, theValue);
        }
    }

    @Override
    public K get(final int theRowIndex, final int theColumnIndex, final K theDefault)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        final SparseGrowableVector<K> theRow = this.rows.get(theRowIndex);

        return (null != theRow) ? theRow.get(theColumnIndex, theDefault) : theDefault;
    }

    @Override
    public K get(final int theRowIndex, final int theColumnIndex)
    {
        return this.get(theRowIndex, theColumnIndex, null);
    }

}
