package com.lecomte.jessy.mythemoviedblib.data;

/**
 * Created by Jessy on 2016-01-26.
 */
public class Trailers
{
    private String id;

    private TrailerInfo[] results;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public TrailerInfo[] getResults ()
    {
        return results;
    }

    public void setResults (TrailerInfo[] results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", results = "+results+"]";
    }
}
