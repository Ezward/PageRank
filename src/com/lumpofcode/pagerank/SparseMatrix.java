package com.lumpofcode.pagerank;

/**
 * Created by emurphy on 2/25/15.
 */
public interface SparseMatrix<K>
{
    void set(int theRowIndex, int theColumnIndex, K theValue);

    K get(int theRowIndex, int theColumnIndex, K theDefault);

    K get(int theRowIndex, int theColumnIndex);
}
