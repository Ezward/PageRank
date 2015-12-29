package com.lumpofcode.collection;

/**
 * Created by emurphy on 2/26/15.
 */
public class SparseGrowableMatrix<K extends Object> implements SparseMatrix<K>
{
    public final int rowCount;
    public final int columnCount;
    private final int blockSize;
    private final SparseGrowableVector<SparseGrowableVector<K>> columns;
    private static final EmptyIntegerIterator emptyIterator = EmptyIntegerIterator.SINGLETON;

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
        this.columns = new SparseGrowableVector<SparseGrowableVector<K>>(theBlockSize);
    }

    @Override
    public void set(final int theRowIndex, final int theColumnIndex, final K theValue)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        SparseGrowableVector<K> theColumn = this.columns.get(theColumnIndex);
        if(null != theColumn)
        {
            theColumn.set(theRowIndex, theValue);
        }
        else if(null != theValue)
        {
            // only allocate a new column if we are actually setting a value
            theColumn = new SparseGrowableVector<>(this.blockSize);
            this.columns.set(theColumnIndex, theColumn);
            theColumn.set(theRowIndex, theValue);
        }
    }

    @Override
    public K get(final int theRowIndex, final int theColumnIndex, final K theDefault)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        final SparseGrowableVector<K> theColumn = this.columns.get(theColumnIndex);

        return (null != theColumn) ? theColumn.get(theRowIndex, theDefault) : theDefault;
    }

    @Override
    public K get(final int theRowIndex, final int theColumnIndex)
    {
        return this.get(theRowIndex, theColumnIndex, null);
    }

    /**
     * return an Iterator for the column indices in the matrix.
     *
     * @return Iterator for the columns indices in the matrix.
     */
    public IntegerIterator columnIndices() { return this.columns.indices(); }

    /**
     * return an Iterator for the row indices in the given column.
     *
     * @param theColumnIndex
     * @return Iterator for the row indices for the column at theColumnIndex
     */
    public IntegerIterator rowIndices(final int theColumnIndex)
    {
        final SparseGrowableVector<K> theColumn = this.columns.get(theColumnIndex);

        return (null != theColumn) ? theColumn.indices() : emptyIterator;
    }

}
