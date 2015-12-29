package com.lumpofcode.collection;

import java.util.NoSuchElementException;

/**
 * Created by emurphy on 2/27/15.
 */
public class SparseIntegerVector
{
    private int count;              // the number of set elements (non zero elements)
    private final int[] indices;    // the vector indices
    private final int[] values;  // the corresponding element value
    public final int zero;      // the default value for elements that are not set.

    /**
     * Construct a sparse vector of doubles.
     *
     * @param theCardinality the number of elements that can be non-zero.
     */
    public SparseIntegerVector(final int theCardinality)
    {
        this(theCardinality, 0);
    }

    /**
     * Construct a sparse vector of doubles.
     *
     * @param theCardinality the number of elements that can be non-zero.
     * @param zero the default value for elements that are not set.
     */
    public SparseIntegerVector(final int theCardinality, final int zero)
    {
        this.count = 0;
        this.zero = zero;
        this.indices = new int[theCardinality];
        this.values = new int[theCardinality];
    }

    public int size()
    {
        return this.count;
    }

    public int free()
    {
        return this.indices.length - this.count;
    }

    /**
     * Set the value of the element at the given index.
     *
     * @param theVectorIndex
     * @param theValue
     * @throws IndexOutOfBoundsException if there is not room to do
     *         the insert and the space cannot be expanded.
     */
    public void set(final int theVectorIndex, final int theValue)
    {
        if(!safeSet(theVectorIndex, theValue))
        {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Set the value of the element at the given index if possible.
     *
     * @param theVectorIndex
     * @param theValue
     * @return true if value could be set, false if not.
     */
    public boolean safeSet(final int theVectorIndex, final int theValue)
    {
        final int theArrayPosition = positionOf(theVectorIndex);

        if (theArrayPosition < count && indices[theArrayPosition] == theVectorIndex)
        {
            if (zero != theValue)
            {
                values[theArrayPosition] = theValue;
            }
            else
            {
                // remove zero value
                removeAt(theArrayPosition);
            }
            return true;
        }
        else // new index
        {
            return insertAt(theArrayPosition, theVectorIndex, theValue);
        }
    }

    /**
     * Get the element at the given index.
     * If there is no element at the index, return the given default.
     *
     * @param theVectorIndex the index into the vector
     * @param theDefault the value to return if there is no element at the index.
     * @return the value of the element at the index or the default if there
     *         is no element at the given index.
     */
    public int get(final int theVectorIndex, final int theDefault)
    {
        final int theArrayPosition = positionOf(theVectorIndex);

        if ((theArrayPosition < count) && (indices[theArrayPosition] == theVectorIndex))
        {
            return values[theArrayPosition];
        }

        return theDefault;
    }

    /**
     * Get the element at the given index.
     * If there is no element at the index, return the given default.
     *
     * @param theVectorIndex the index into the vector
     * @return the value of the element at the index or zero if there
     *         is no element at the given index.
     */
    public int get(final int theVectorIndex)
    {
        return get(theVectorIndex, this.zero);
    }

    /**
     * Determine if there is space to insert new values.
     *
     * @return true if the vector is full and cannot be expanded
     */
    public boolean isFull()
    {
        return this.count >= this.indices.length;
    }

    /**
     * Determine if there are no non-zero entries.
     *
     * @return true if there are no non-zero entries.
     */
    public boolean isEmpty()
    {
        return 0 == this.count;
    }

    public int lowerIndex()
    {
        if(count > 0)
        {
            return this.indices[0];
        }
        return -1;
    }

    public int upperIndex()
    {
        if(count > 0)
        {
            return this.indices[count - 1];
        }
        return -1;
    }

    /**
     * find the position of the given index in the vector.
     *
     * @param theVectorIndex
     * @return position 0..count of index in the vector.
     * @throws IndexOutOfBoundsException if index is negative.
     */
    private int positionOf(final int theVectorIndex)
    {
        if(theVectorIndex < 0)
        {
            throw new IndexOutOfBoundsException();
        }

        if ((0 == count) || (theVectorIndex > indices[count - 1]))
        {
            return count;
        }

        //
        // use a binary search to find the vector index.
        //
        int theLeft = 0;
        int theRight = count;

        while (theLeft < theRight)
        {
            int thePosition = (theLeft + theRight) / 2;
            if (indices[thePosition] > theVectorIndex)
            {
                theRight = thePosition;
            }
            else if (indices[thePosition] < theVectorIndex)
            {
                theLeft = thePosition + 1;
            }
            else
            {
                return thePosition;
            }
        }

        return theLeft;
    }

    /**
     * insert a vector index,value pair into the array at the given position
     *
     * @param theArrayPosition the array position 0..count
     * @param theVectorIndex the vector index
     * @param theValue the vector element value
     * @return true if the value could be inserted, false if not.
     * @throws IndexOutOfBoundsException if theArrayPosition > count
     */
    private boolean insertAt(final int theArrayPosition, final int theVectorIndex, final int theValue)
    {
        if((theArrayPosition < 0) || (theArrayPosition > this.count))
        {
            throw new IndexOutOfBoundsException();
        }

        //
        // don't insert zero values
        //
        if (zero == theValue) {
            return true;
        }

        //
        // there must be space
        //
        if (this.count < values.length)
        {
            //
            // make space for the index and the value
            //
            if (theArrayPosition < this.count)
            {
                System.arraycopy(values, theArrayPosition, values, theArrayPosition + 1, count - theArrayPosition);
                System.arraycopy(indices, theArrayPosition, indices, theArrayPosition + 1, count - theArrayPosition);
            }

            this.values[theArrayPosition] = theValue;
            this.indices[theArrayPosition] = theVectorIndex;

            this.count += 1;
            return true;
        }

        return false;
    }

    /**
     * Remove a vector index,value pair from the sparse vector.
     *
     * @param theArrayPosition 0..count-1
     * @throws IndexOutOfBoundsException if array is empty or 0 < index >= count
     */
    private void removeAt(int theArrayPosition)
    {
        if((theArrayPosition < 0) || (theArrayPosition >= this.count))
        {
            throw new IndexOutOfBoundsException();
        }

        if(0 == this.count)
        {
            throw new IndexOutOfBoundsException();
        }

        this.count -= 1;
        if(theArrayPosition < this.count)
        {
            System.arraycopy(values, theArrayPosition + 1, values, theArrayPosition, count - theArrayPosition);
            System.arraycopy(indices, theArrayPosition + 1, indices, theArrayPosition, count - theArrayPosition);
        }
    }

    public IntegerIterator values() { return new ValueIteratorImpl(); }
    public IntegerIterator indices()
    {
        return new IndexIteratorImpl();
    }

    private final class IndexIteratorImpl implements IntegerIterator
    {
        private int index;

        private IndexIteratorImpl()
        {
            this.index = 0;
        }

        public boolean hasNext()
        {
            return (this.index < count);
        }

        public int next()
        {
            if(hasNext())
            {
                return indices[this.index++];
            }

            throw new NoSuchElementException();
        }
    }

    private final class ValueIteratorImpl implements IntegerIterator
    {
        private int index;

        private ValueIteratorImpl()
        {
            this.index = 0;
        }

        public boolean hasNext()
        {
            return (this.index < count);
        }

        public int next()
        {
            if(hasNext())
            {
                return values[this.index++];
            }

            throw new NoSuchElementException();
        }
    }

}
