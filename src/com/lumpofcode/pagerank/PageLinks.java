package com.lumpofcode.pagerank;

import java.util.*;

/**
 * Storage for sparse set of links from page to page.
 *
 * Created by emurphy on 2/8/15.
 */
public final class PageLinks extends HashMap<Integer, List<Integer>>
{

    /**
     * Add a link from one page to another page given the
     * page indices of the two pages.
     * @param theFromPage
     * @param theToPage
     * @return this PageLinks collection for call chaining purposes
     */
    public PageLinks addLink(final int theFromPage, final int theToPage)
    {
        assert(theFromPage >= 0);
        assert(theToPage >= 0);

        final Integer theFromIndex = Integer.valueOf(theFromPage);
        List<Integer> theLinks = this.get(theFromIndex);
        if(null == theLinks)
        {
            // no links for this page yet, so make a list for the first one
            theLinks = new ArrayList<>();
            this.put(theFromIndex, theLinks);
        }
        theLinks.add(Integer.valueOf(theToPage));

        return this;    // allow for call chaining
    }
}
