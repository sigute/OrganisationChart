package com.github.sigute.organisationchart.organisation;

import java.util.List;

/**
 * Created by spikereborn on 21/03/2015.
 */
public class Organisation
{
    private Employee ceo;
    private List<Team> teams;

    public Organisation(Employee ceo, List<Team> teams)
    {
        this.ceo = ceo;
        this.teams = teams;
    }

    public Employee getCEO()
    {
        return ceo;
    }

    public List<Team> getTeams()
    {
        return teams;
    }
}
