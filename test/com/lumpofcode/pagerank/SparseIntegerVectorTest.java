package com.lumpofcode.pagerank;

import com.lumpofcode.collection.IndexIterator;
import com.lumpofcode.collection.SparseIntegerVector;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emurphy on 2/27/15.
 */
public class SparseIntegerVectorTest
{
    @Test
    public void testSet()
    {
        final SparseIntegerVector theVector = new SparseIntegerVector(3);

        assertTrue("Should be empty after construction.", theVector.isEmpty());
        assertFalse("Should NOT be full after construction.", theVector.isFull());

        theVector.set(1000, 100 );
        assertFalse("Should NOT be empty after an insert.", theVector.isEmpty());
        assertFalse("Should NOT be full after one insert.", theVector.isFull());

        theVector.set(10, 11);
        assertFalse("Should NOT be empty after an insert.", theVector.isEmpty());
        assertFalse("Should NOT be full after two inserts.", theVector.isFull());

        theVector.set(100, 101);
        assertFalse("Should NOT be empty after an insert.", theVector.isEmpty());
        assertTrue("Should be full after three inserts.", theVector.isFull());

        assertFalse("Should not be able to set when full.", theVector.safeSet(10000, 10001));

        try
        {
            //
            // attempting to set when full will throw
            //
            theVector.set(10000, 10001);
            fail("We should throw an exception if we try to insert when full.");
        }
        catch(IndexOutOfBoundsException e)
        {
            // we caught the exception
            assertTrue(true);
        }
        catch(Exception e)
        {
            fail("Encountered an unexpected exception type.");
        }
    }


    @Test
    public void testGet()
    {
        final SparseIntegerVector theVector = new SparseIntegerVector(3);


        assertTrue("Should have 0 at 1000.", 0 == theVector.get(1000));
        theVector.set(1000, 1001);
        assertTrue("Should have 1001 at 1000.", 1001 == theVector.get(1000));

        assertTrue("Should have 0 at 10.", 0 == theVector.get(10));
        theVector.set(10, 11);
        assertTrue("Should have 11 at 10.", 11 == theVector.get(10));

        assertTrue("Should have 0 at 100.", 0 == theVector.get(100));
        theVector.set(100, 101);
        assertTrue("Should have 101 at 100", 101 == theVector.get(100));
    }

    @Test
    public void testIterator()
    {
        final SparseIntegerVector theVector = new SparseIntegerVector(3);


        theVector.set(1000, 1001);
        theVector.set(10, 11);
        theVector.set(100, 101);

        final IndexIterator theIterator = theVector.iterator();
        assertNotNull("Iterator should never be null.", theIterator);

        assertTrue("Should have next.", theIterator.hasNext());
        assertTrue("First element is 10.", 10 == theIterator.next());

        assertTrue("Should have next.", theIterator.hasNext());
        assertTrue("Second element is 100.", 100 == theIterator.next());

        assertTrue("Should have next.", theIterator.hasNext());
        assertTrue("Third element is 1000.", 1000 == theIterator.next());

        assertFalse("Should NOT have next.", theIterator.hasNext());

    }

    @Test
    public void testRemove()
    {
        final SparseIntegerVector theVector = new SparseIntegerVector(3);

        assertTrue("Should be empty after construction.", theVector.isEmpty());

        theVector.set(1000, 1001);
        theVector.set(10, 11);
        theVector.set(100, 101);

        assertFalse("Should NOT be empty after an insert.", theVector.isEmpty());
        assertTrue("Should be full after three inserts.", theVector.isFull());

        theVector.set(100, 0);    // insert zero at index 100
        assertFalse("Should NOT be empty.", theVector.isEmpty());
        assertFalse("Should NOT be full after inserting zero.", theVector.isFull());

        theVector.set(10, 0);    // insert zero at index 10
        assertFalse("Should NOT be empty.", theVector.isEmpty());
        assertFalse("Should NOT be full after inserting zero.", theVector.isFull());

        theVector.set(1000, 0);    // insert zero at index 1000
        assertTrue("Should be empty.", theVector.isEmpty());
        assertFalse("Should NOT be full after inserting zero.", theVector.isFull());


    }

}
