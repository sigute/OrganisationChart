package com.github.sigute.organisationchart.organisation;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent organisation.
 * It holds CEO, teams, etc.
 *
 * @author Sigute
 */
public class Organisation
{
    private Employee ceo;
    private List<Team> teams;

    @SuppressWarnings("unused")
    private Organisation()
    {
        //should not be called directly
    }

    public Organisation(Employee ceo, List<Team> teams)
    {
        if (ceo == null)
        {
            throw new IllegalArgumentException("Organisation must have ceo!");
        }

        if (teams == null || teams.size() == 0)
        {
            throw new IllegalArgumentException("Organisation must have teams!");
        }

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

    public List<Employee> getAllEmployees()
    {
        List<Employee> allEmployees = new ArrayList<>();
        allEmployees.add(ceo);
        for (Team team : teams)
        {
            allEmployees.add(team.getTeamLeader());
            allEmployees.addAll(team.getTeamMembers());
        }
        return allEmployees;
    }
}
