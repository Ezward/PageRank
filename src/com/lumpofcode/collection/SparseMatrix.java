package com.lumpofcode.collection;

/**
 * Created by emurphy on 2/25/15.
 */
public interface SparseMatrix<K>
{
    void set(int theRowIndex, int theColumnIndex, K theValue);

    K get(int theRowIndex, int theColumnIndex, K theDefault);

    K get(int theRowIndex, int theColumnIndex);

    /**
     * return an Iterator for the column indices in the matrix.
     *
     * @return Iterator for the columns indices in the matrix.
     */
    IntegerIterator columnIndices();

    /**
     * return an Iterator for the row indices in the given column.
     *
     * @param theColumnIndex
     * @return Iterator for the row indices for the column at theColumnIndex
     */
    IntegerIterator rowIndices(final int theColumnIndex);

}
