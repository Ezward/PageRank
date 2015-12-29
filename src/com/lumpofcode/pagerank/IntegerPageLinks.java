package com.lumpofcode.pagerank;

import com.lumpofcode.collection.IntegerArray;
import com.lumpofcode.collection.SparseGrowableVector;

/**
 * Created by emurphy on 2/27/15.
 */
public class IntegerPageLinks extends SparseGrowableVector<IntegerArray>
{
    private int max = 0;

    public int getMax()
    {
        return this.max;
    }

    public IntegerArray getLinksFrom(final int theFromPage)
    {
        return this.get(theFromPage);
    }

    public IntegerPageLinks()
    {
        super(16);
    }

    /**
     * Add a link from one page to another page given the
     * page indices of the two pages.
     * @param theFromPage
     * @param theToPage
     * @return this PageLinks collection for call chaining purposes
     */
    public IntegerPageLinks addLink(final int theFromPage, final int theToPage)
    {
        assert(theFromPage >= 0);
        assert(theToPage >= 0);

        IntegerArray theLinks = this.get(theFromPage);
        if(null == theLinks)
        {
            // no links for this page yet, so make a list for the first one
            theLinks = new IntegerArray(16);
            this.set(theFromPage, theLinks);
        }
        theLinks.push(theToPage);

        max = Math.max(max, theFromPage);
        max = Math.max(max, theToPage);

        return this;    // allow for call chaining
    }

}
