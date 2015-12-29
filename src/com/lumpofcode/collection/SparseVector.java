package com.lumpofcode.collection;

import java.util.Iterator;

/**
 * Created by emurphy on 2/25/15.
 */
public interface SparseVector<K>
{
    /**
     * Set theValue at theIndex.
     * If theValue is null, the index may
     * be removed from the vector.
     *
     * @param theIndex
     * @param theValue
     */
    void set(int theIndex, K theValue);

    /**
     * Get the value at the index or
     * theDefault if theIndex is missing.
     *
     * @param theIndex
     * @param theDefault
     * @return the value at theIndex or theDefault
     *         if theIndex is missing.
     */
    K get(int theIndex, K theDefault);

    /**
     * Get the value at theIndex.
     *
     * @param theIndex
     * @return the value at theIndex
     */
    K get(int theIndex);

    /**
     * Iterator for the indices.
     * This will produce the indices in order.
     * If the collection is sparse, then there
     * will be gaps in the emitted indices.
     * So if we have indices [10, 15, 18] then they will
     * be emitted in that order and only the 3 will be emitted.
     *
     * @return Iterator for the indices.
     */
    IntegerIterator indices();

    /**
     * Iterator for the values.
     * Note that if the vector is sparse,
     * only values set values are returned,
     * missing values are skipped.
     *
     * @return Iterator for the sparse values in the vector.
     */
    Iterator<K> values();
}
