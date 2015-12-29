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
    private final SparseGrowableVector<SparseGrowableDoubleVector> rows;

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
        this.rows = new SparseGrowableVector<SparseGrowableDoubleVector>(theRowBlockSize);
    }

    public void set(final int theRowIndex, final int theColumnIndex, final double theValue)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        SparseGrowableDoubleVector theRow = this.rows.get(theRowIndex);
        if(null != theRow)
        {
            theRow.set(theColumnIndex, theValue);
        }
        else if(zero != theValue)
        {
            // only allocate a new row if we are actually setting a value
            theRow = new SparseGrowableDoubleVector(this.columnBlockSize);
            this.rows.set(theRowIndex, theRow);
            theRow.set(theColumnIndex, theValue);
        }
    }

    public double get(final int theRowIndex, final int theColumnIndex, final double theDefault)
    {
        if((theRowIndex < 0) || (theRowIndex >= this.rowCount)) throw new IndexOutOfBoundsException();
        if((theColumnIndex < 0) || (theColumnIndex >= this.columnCount)) throw new IndexOutOfBoundsException();

        final SparseGrowableDoubleVector theRow = this.rows.get(theRowIndex);

        return (null != theRow) ? theRow.get(theColumnIndex, theDefault) : theDefault;
    }

    public double get(final int theRowIndex, final int theColumnIndex)
    {
        return this.get(theRowIndex, theColumnIndex, zero);
    }

    public SparseGrowableDoubleVector row(final int theRowIndex)
    {
        return this.rows.get(theRowIndex);
    }


    public IndexIterator iterator()
    {
        return this.rows.iterator();
    }
}
