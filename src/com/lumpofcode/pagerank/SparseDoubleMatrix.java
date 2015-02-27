package com.lumpofcode.pagerank;

/**
 * Created by emurphy on 2/27/15.
 */
public class SparseDoubleMatrix
{
    public final double zero;
    public final int rowCount;
    public final int columnCount;
    private final int blockSize;
    private final SparseGrowableVector<SparseGrowableDoubleVector> rows;

    public SparseDoubleMatrix(final int theSquareDimension, final int theBlockSize)
    {
        this(theSquareDimension, theSquareDimension, theBlockSize, 0.0);
    }

    public SparseDoubleMatrix(final int theRowCount, final int theColumnCount, final int theBlockSize, final double zero)
    {
        if(theRowCount <= 0) throw new IllegalArgumentException("theRowCount must be a positive integer.");
        if(theColumnCount <= 0) throw new IllegalArgumentException("theColumnCount must be a positive integer.");

        this.rowCount = theRowCount;
        this.columnCount = theColumnCount;

        this.zero = zero;

        this.blockSize = theBlockSize;
        this.rows = new SparseGrowableVector<SparseGrowableDoubleVector>(theBlockSize);
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
            theRow = new SparseGrowableDoubleVector(this.blockSize);
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


}
