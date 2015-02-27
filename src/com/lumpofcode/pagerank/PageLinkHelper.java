package com.lumpofcode.pagerank;

import com.lumpofcode.util.StringUtils;

import java.io.*;
import java.util.Iterator;

/**
 * Created by emurphy on 2/16/15.
 */
public class PageLinkHelper
{
    /**
     * Read page links from a text file given the file name.
     *
     * @param thePageLinks
     * @param theFileName
     */
    public static void readPageLinks(final PageLinks thePageLinks, final String theFileName, final int theSkipLines, final char theFieldDelimiter, final Character theTextFieldQuote)
    {
        final File theFile = new File(theFileName);
        readPageLinks(thePageLinks, theFile, theSkipLines, theFieldDelimiter, theTextFieldQuote);
    }

    /**
     * Read page links from a text file.
     *
     * @param thePageLinks
     * @param theFile
     */
    public static void readPageLinks(final PageLinks thePageLinks, final File theFile, final int theSkipLines, final char theFieldDelimiter, final Character theTextFieldQuote)
    {
        System.out.println("Start importing file " + theFile.getAbsolutePath());

        try
        {
            int theLineCount = 0;
            final BufferedReader theReader = new BufferedReader(new FileReader(theFile));
            try
            {
                String theLine;
                while (null != (theLine = theReader.readLine()))
                {
                    // skip the beginning lines
                    if(theLineCount >= theSkipLines)
                    {
                        theLine = theLine.trim();
                        if (!theLine.isEmpty())
                        {
                            // parse the line and add it to the links
                            parsePageLine(thePageLinks, theLine, '\t', null);
                        }
                    }
                    theLineCount += 1;
                }

                System.out.println("Successfully imported " + String.valueOf(theLineCount) + " lines from " + theFile.getPath());
            }
            catch (Exception ex)
            {
                System.out.println("Failed to import file " + theFile.getPath() + " after " + String.valueOf(theLineCount) + "lines.");
            }
            finally
            {
                theReader.close();
                System.out.println("Finished importing file " + theFile.getAbsolutePath());
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found: " + theFile.getAbsolutePath());
        }
        catch (IOException e)
        {
            System.out.println("Failure closing file reader.");
        }
    }

    public static void parsePageLine(final PageLinks thePageLinks, final String theLine, final char theFieldDelimiter, final Character theTextFieldQuote)
    {
        //
        // break the delimited line up into fields
        // we are expecting all the fields in the school and courses tables
        //
        if ((null != theLine) && !theLine.isEmpty())
        {
            System.out.println("Importing line:" + theLine);

            //
            // skip comments
            //
            if('#' == theLine.charAt(0))
            {
                return;
            }

            //
            // if the line is not empty, then the number of
            // tabs in the line must match the expeced field count
            //
            final String[] theFields = theLine.split(String.valueOf(theFieldDelimiter));
            if (2 == theFields.length)
            {
                //
                // remove whitespace and quotes
                //
                if (null != theTextFieldQuote)
                {
                    for (int i = 0; i < theFields.length; i += 1)
                    {
                        theFields[i] = StringUtils.trim(theFields[i].trim(), theTextFieldQuote);
                    }
                }
                else
                {
                    // remove whitespace only
                    for (int i = 0; i < theFields.length; i += 1)
                    {
                        theFields[i] = theFields[i].trim();
                    }
                }

                final Integer theFromPage = Integer.valueOf(theFields[0]);
                final Integer theToPage = Integer.valueOf(theFields[1]);

                thePageLinks.addLink(theFromPage, theToPage);

            }
            else
            {
                System.out.println("Skipping line with wrong number of fields (" + String.valueOf(theFields.length) + ") should be 2.");
            }
        }
        else
        {
            System.out.println("Skipping empty line.");
        }
    }

    public static void dumpPageLinks(final PageLinks thePageLinks)
    {
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

    }

}
