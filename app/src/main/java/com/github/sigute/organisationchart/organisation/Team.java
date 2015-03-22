package com.github.sigute.organisationchart.organisation;

import com.github.sigute.organisationchart.organisation.Employee;

import java.util.List;

/**
 * Created by spikereborn on 21/03/2015.
 */
public class Team
{
    private String teamName;
    private Employee teamLeader;
    private List<Employee> teamMembers;

    @SuppressWarnings("unused")
    private Team()
    {
        //should not be called directly
    }

    public Team(String teamName, List<Employee> teamMembers)
    {
        this.teamName = teamName;
        for (Employee employee : teamMembers)
        {
            if (employee.isTeamLeader())
            {
                teamLeader = employee;
                teamMembers.remove(employee);
                break;
            }
        }

        if (teamLeader == null)
        {
            //TODO review this
            throw new IllegalStateException("Team must have a leader!");
        }

        this.teamMembers = teamMembers;
    }

    public String getName()
    {
        return teamName;
    }

    public Employee getTeamLeader()
    {
        return teamLeader;
    }

    public List<Employee> getTeamMembers()
    {
        return teamMembers;
    }
}
