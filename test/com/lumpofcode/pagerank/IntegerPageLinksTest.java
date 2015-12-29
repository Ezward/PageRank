package com.lumpofcode.pagerank;

import com.lumpofcode.collection.IntegerIterator;
import com.lumpofcode.collection.IntegerArray;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emurphy on 3/20/15.
 */
public class IntegerPageLinksTest
{
    @Test
    public void testAddLink()
    {
        IntegerPageLinks thePageLinks = new IntegerPageLinks();

        assertTrue("thePageLinks should be empty.", 0 == thePageLinks.size());

        thePageLinks.addLink(0, 1);
        assertTrue("thePageLinks should have one from page", 1 == thePageLinks.size());
        assertTrue("thePageLinks should have the from page 0", null != thePageLinks.getLinksFrom(0));
        assertTrue("thePageLinks max should be 1.", 1 == thePageLinks.getMax());
        assertTrue("thePageLinks should NOT have a fromPage of 1.", null == thePageLinks.getLinksFrom(1));

        IntegerIterator theFromPageIterator = thePageLinks.indices();
        assertTrue("theFromPageIterator.hasNext() should be true", theFromPageIterator.hasNext());
        assertTrue("theFromPageIterator.next() should return page 0", 0 == theFromPageIterator.next());
        assertFalse("theFromPageIterator.hasNext() should return false", theFromPageIterator.hasNext());

        thePageLinks.addLink(1, 2);
        assertTrue("thePageLinks should have 2 from pages", 2 == thePageLinks.size());
        assertTrue("thePageLinks should have the from page 1", null != thePageLinks.getLinksFrom(1));
        assertTrue("thePageLinks max should be 2.", 2 == thePageLinks.getMax());

        theFromPageIterator = thePageLinks.indices();
        assertTrue("theFromPageIterator.hasNext() should be true", theFromPageIterator.hasNext());
        assertTrue("theFromPageIterator.next() should return page 0", 0 == theFromPageIterator.next());
        assertTrue("theFromPageIterator.hasNext() should be true", theFromPageIterator.hasNext());
        assertTrue("theFromPageIterator.next() should return page 1", 1 == theFromPageIterator.next());
        assertFalse("theFromPageIterator.hasNext() should return false", theFromPageIterator.hasNext());


        thePageLinks = new IntegerPageLinks();
        final int n = 1000;
        for(int i = 0; i < n; i += 1)
        {
            thePageLinks.addLink(i, i + 1);
            thePageLinks.addLink(i, n - (i + 1));
        }

        assertTrue("thePageLinks should have 1000 from pages (0..999).", 1000 == thePageLinks.size());
        for(int i = 0; i < n; i += 1)
        {
            final IntegerArray theTargetPages = thePageLinks.getLinksFrom(i);

            //
            // each from page should have two links
            //
            assertTrue("theTargetPages should not be null.", null != theTargetPages);
            assertTrue("from page zero should have a two target pages.", 2 == theTargetPages.size());
            assertTrue("the first target page should be " + String.valueOf(i + 1), (i + 1) == theTargetPages.get(0));
            assertTrue("the second target page should be " + String.valueOf(n - (i + 1)), (n - (i + 1)) == theTargetPages.get(1));
        }
        assertTrue("thePageLinks should NOT have a fromPage of 1000.", null == thePageLinks.getLinksFrom(1000));

    }
}
