package com.lumpofcode.collection;

/**
 * Created by emurphy on 2/27/15.
 */
public class SparseDoubleMatrix
{
    public final double zero;
    public final int rowCount;
    public final int columnCount;
    private final int rowBlockSize;
    private final int columnBlockSize;
    private final SparseGrowableVector<SparseGrowableDoubleVector> columns;
    private static final EmptyIntegerIterator emptyIterator = EmptyIntegerIterator.SINGLETON;

    public SparseDoubleMatrix(final int theSquareDimension, final int theRowBlockSize, final int theColumnBlockSize)
    {
        this(theSquareDimension, theSquareDimension, theRowBlockSize, theColumnBlockSize, 0.0);
    }

    public SparseDoubleMatrix(final int theRowCount, final int theColumnCount, final int theRowBlockSize, final int theColumnBlockSize, final double zero)
    {
        if(theRowCount <= 0) throw new IllegalArgumentException("theRowCount must be a positive integer.");
        if(theColumnCount <= 0) throw new IllegalArgumentException("theColumnCount must be a positive integer.");

        this.rowCount = theRowCount;
        this.columnCount = theColumnCount;

        this.zero = zero;

        this.columnBlockSize = theColumnBlockSize;
        this.rowBlockSize = theRowBlockSize;
        this.columns = new SparseGrowableVector<SparseGrowableDoubleVector>(theColumnBlockSize);
    }

    public void set(final int theRowIndex, final int theColumnIndex, final double theValue)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        SparseGrowableDoubleVector theColumn = this.columns.get(theRowIndex);
        if(null != theColumn)
        {
            theColumn.set(theRowIndex, theValue);
        }
        else if(zero != theValue)
        {
            // only allocate a new row if we are actually setting a value
            theColumn = new SparseGrowableDoubleVector(this.rowBlockSize);
            this.columns.set(theColumnIndex, theColumn);
            theColumn.set(theColumnIndex, theValue);
        }
    }

    public double get(final int theRowIndex, final int theColumnIndex, final double theDefault)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        final SparseGrowableDoubleVector theColumn = this.columns.get(theColumnIndex);

        return (null != theColumn) ? theColumn.get(theRowIndex, theDefault) : theDefault;
    }

    public double get(final int theRowIndex, final int theColumnIndex)
    {
        return this.get(theRowIndex, theColumnIndex, zero);
    }

    public IntegerIterator columnIterator()
    {
        return this.columns.indices();
    }

    public IntegerIterator rowIterator(final int theColumnIndex)
    {
        final SparseGrowableDoubleVector theColumn = this.columns.get(theColumnIndex);

        return (null != theColumn) ? theColumn.indices() : emptyIterator;
    }
}
