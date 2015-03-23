package com.github.sigute.organisationchart.organisation;

import java.util.List;

/**
 * This class represent team. Each team has its attributes, such as name, leader, members, etc.
 *
 * @author Sigute
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

        if (teamMembers == null || teamMembers.size() == 0)
        {
            throw new IllegalArgumentException("Team must have members!");
        }

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
