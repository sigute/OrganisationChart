package com.github.sigute.organisationchart.organisation;

/**
 * This class represents an employee and holds employee attributes.
 *
 * @auther Sigute
 */
public class Employee
{
    private String id;
    private String firstName;
    private String lastName;
    private String role;
    private String imageUrl;
    boolean teamLeader;
    boolean ceo;

    @SuppressWarnings("unused")
    private Employee()
    {
        //should not be called directly
    }

    public Employee(String id, String firstName, String lastName, String role, String imageUrl,
            boolean teamLeader, boolean ceo)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.imageUrl = imageUrl;
        this.teamLeader = teamLeader;
        this.ceo = ceo;
    }

    public String getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getRole()
    {
        return role;
    }

    public boolean isTeamLeader()
    {
        return teamLeader;
    }

    public boolean isCEO()
    {
        return ceo;
    }

    public String getImageURL()
    {
        return imageUrl;
    }
}
