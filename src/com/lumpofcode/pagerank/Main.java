package com.lumpofcode.pagerank;

import java.util.Iterator;

public class Main {

    public static void main(String[] args)
    {
        //
        // initialize the page links
        //
        final PageLinks thePageLinks = new PageLinks();

        // all nodes link to nodes other than themselves
        thePageLinks
                .addLink(0, 1)  // A -> B
                .addLink(0, 2)  // A -> B
                .addLink(1, 0)  // A -> C
                .addLink(1, 2)  // A -> C
                .addLink(2, 0)  // B -> C
                .addLink(2, 1); // C -> C
        final double beta = 0.85;

        // week 1, problem 1
//        thePageLinks
//                .addLink(0, 1)  // A -> B
//                .addLink(0, 2)  // A -> C
//                .addLink(1, 2)  // B -> C
//                .addLink(2, 2); // C -> C
//        final double beta = 0.85;

        // week 1, problem 2
//        thePageLinks
//                .addLink(0, 1)  // A -> B
//                .addLink(0, 2)  // A -> C
//                .addLink(1, 2)  // B -> C
//                .addLink(2, 0); // C -> A
//        final double beta = 0.85;

        // week 1, problem 3
        // NOTE: hack initial eigen vector to all ones!!!
//        thePageLinks
//                .addLink(0, 1)  // A -> B
//                .addLink(0, 2)  // A -> C
//                .addLink(1, 2)  // B -> C
//                .addLink(2, 0); // C -> A
//        final double beta = 1.0;    // no taxation


        //
        // show the page links
        //
        for(Integer thePage : thePageLinks.keySet())
        {
            final StringBuilder theBuilder = new StringBuilder();
            theBuilder
                    .append(thePage)
                    .append(" -> ");
            final Iterator<Integer> theToPages = thePageLinks.get(thePage).iterator();
            if(theToPages.hasNext())
            {
                theBuilder.append(theToPages.next());
                while(theToPages.hasNext())
                {
                    theBuilder.append(", ");
                    theBuilder.append(theToPages.next());
                }
            }
            System.out.println(theBuilder.toString());
        }

        System.out.println();

        final double[] thePageRankVector = PageRankCalculator.calculatePageRank(thePageLinks, 3, beta, 0.000001, 100);
    }
}
