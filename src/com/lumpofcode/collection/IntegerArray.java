package com.lumpofcode.collection;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Created by emurphy on 2/28/15.
 */
public final class IntegerArray
{
    private static final int minimumLength = 16;

    private final int growSize;
    private int[] indices;      // the vector indices
    private int logicalSize;    // logical size of array, <= indices.length

    /**
     * Construct an expandable array of int.
     *
     * @param theInitialSize the number of elements to construct.
     * @param theGrowSize the number of elements to add when growing.
     */
    public IntegerArray(final int theInitialSize, final int theGrowSize)
    {
        // growSize can be zero or more; it it is more, it conforms to minimum length
        this.growSize = (theGrowSize <= 0) ? 0 : ((theGrowSize < minimumLength) ? minimumLength : theGrowSize);
        this.indices = new int[theInitialSize > minimumLength ? theInitialSize : minimumLength];
        this.logicalSize = 0;
    }

    public IntegerArray(final int theInitialSize)
    {
        this(theInitialSize, theInitialSize);
    }

    public int size()
    {
        return this.logicalSize;
    }

    public boolean isEmpty() { return 0 == this.logicalSize; }

    /**
     * Set the value of the element at the given index.
     *
     * @param theIndex
     * @param theValue
     * @throws IndexOutOfBoundsException if there is not room to do
     *         the insert and the space cannot be expanded.
     */
    public void set(final int theIndex, final int theValue)
    {
        if(!safeSet(theIndex, theValue))
        {
            throw new IndexOutOfBoundsException();
        }
    }

    public void push(final int theValue)
    {
        set(this.logicalSize, theValue);
    }

    public int peek()
    {
        return get(this.logicalSize - 1);
    }

    public int pop()
    {
        final int theValue = peek();
        this.logicalSize -= 1;
        return theValue;
    }


    /**
     * Set the value of the element at the given index if possible.
     *
     * @param theIndex
     * @param theValue
     * @return true if value could be set, false if not.
     */
    private boolean safeSet(final int theIndex, final int theValue)
    {
        //
        // we can set from 0..size.
        // if we set at size, we will try to grow to fit the element
        //
        if ((theIndex >= 0) && (theIndex <= this.logicalSize))
        {
            if(theIndex == this.indices.length)
            {
                // try to grow
                if(this.growSize > 0)
                {
                    // grow
                    this.indices = Arrays.copyOf(this.indices, this.indices.length + this.growSize);
                }
                else
                {
                    // can't grow
                    throw new IndexOutOfBoundsException();
                }
            }
            if(theIndex == this.logicalSize)
            {
                this.logicalSize += 1;
            }

            // set the value
            this.indices[theIndex] = theValue;
            return true;
        }

        throw new IndexOutOfBoundsException();
    }

    /**
     * Get the element at the given index
     *
     * @param theIndex
     * @return the element at theIndex
     */
    public int get(final int theIndex)
    {
        if ((theIndex >= 0) && (theIndex < this.logicalSize))
        {
            return indices[theIndex];
        }

        throw new IndexOutOfBoundsException();
    }


    /**
     * insert a vector index,value pair into the array at the given position
     *
     * @param theIndex the vector index
     * @param theValue the vector element value
     * @return true if the value could be inserted, false if not.
     * @throws IndexOutOfBoundsException if theArrayPosition > size()
     */
    public void insertAt(final int theIndex, final int theValue)
    {
        if((theIndex < 0) || (theIndex > this.logicalSize))
        {
            throw new IndexOutOfBoundsException();
        }

        //
        // make space for the index and the value
        //
        if(this.logicalSize == this.indices.length)
        {
            // grow
            this.indices = Arrays.copyOf(this.indices, this.indices.length + this.growSize);
        }

        if(theIndex < logicalSize)
        {
            System.arraycopy(indices, theIndex, indices, theIndex + 1, logicalSize - theIndex);
        }
        this.indices[theIndex] = theValue;
        this.logicalSize += 1;
    }

    /**
     * Remove a vector index,value pair from the sparse vector.
     *
     * @param theIndex 0..size()-1
     * @throws IndexOutOfBoundsException if array is empty or 0 < index >= size()
     */
    public int removeAt(int theIndex)
    {
        if((theIndex < 0) || (theIndex >= this.logicalSize))
        {
            throw new IndexOutOfBoundsException();
        }

        final int theValue = get(theIndex);

        this.logicalSize -= 1;
        if(theIndex < this.logicalSize)
        {
            System.arraycopy(indices, theIndex + 1, indices, theIndex, this.logicalSize - theIndex);
        }

        return theValue;
    }

    public ElementIterator iterator()
    {
        return new ElementIterator();
    }

    public final class ElementIterator
    {
        private int index;

        private ElementIterator()
        {
            this.index = 0;
        }

        public boolean hasNext()
        {
            return (this.index < logicalSize);
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

}
