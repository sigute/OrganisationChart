package com.github.sigute.organisationchart.utils;

import com.github.sigute.organisationchart.organisation.Employee;
import com.github.sigute.organisationchart.organisation.Organisation;
import com.github.sigute.organisationchart.organisation.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spikereborn on 21/03/2015.
 */
public class JsonParser
{
    public static Organisation parseOutJsonData(String jsonString)
    {
        Employee ceo = null;
        List<Team> teams = null;

        try
        {
            JSONArray json = new JSONArray(jsonString);

            //TODO check json length, should have CEO and at least one team? at least CEO?

            //parse out ceo
            JSONObject ceoJsonObject = json.getJSONObject(0);
            ceo = parseOutEmployee(ceoJsonObject);

            teams = new ArrayList<Team>();
            //parse out teams
            for (int i = 1; i < json.length(); i++)
            {
                JSONObject teamJsonObject = json.getJSONObject(i);
                Team team = parseOutTeam(teamJsonObject);
                teams.add(team);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return new Organisation(ceo, teams);
    }

    private static Team parseOutTeam(JSONObject teamJsonObject) throws JSONException
    {
        String teamName = teamJsonObject.getString("teamName");
        JSONArray teamMembersJsonArray = teamJsonObject.getJSONArray("members");

        List<Employee> teamMembers = new ArrayList<>();
        for (int j = 0; j < teamMembersJsonArray.length(); j++)
        {
            //parse out each person
            JSONObject personJsonObject = teamMembersJsonArray.getJSONObject(j);
            Employee employee = parseOutEmployee(personJsonObject);
            teamMembers.add(employee);
        }

        return new Team(teamName, teamMembers);
    }

    private static Employee parseOutEmployee(JSONObject personJsonObject)
    {
        String id;
        String firstName;
        String lastName;
        String role;
        String profileImageURL;
        boolean teamLeader = false;

        try
        {
            id = personJsonObject.getString("id");
            firstName = personJsonObject.getString("firstName");
            lastName = personJsonObject.getString("lastName");
            role = personJsonObject.getString("role");
            profileImageURL = personJsonObject.getString("profileImageURL");
        }
        catch (JSONException e)
        {
            //TODO throw a different exception?
            // mandatory string missing! making assumption on mandatory...
            return null;
        }

        try
        {
            teamLeader = personJsonObject.getBoolean("teamLead");
        }
        catch (JSONException e)
        {
            // no tag for teamLead, it's ok, false by default
        }

        return new Employee(id, firstName, lastName, role, profileImageURL, teamLeader);
    }
}
