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
    public static Organisation parseOutJsonData(String jsonString) throws JSONException
    {
        Employee ceo = null;
        List<Team> teams = null;

        JSONArray json = new JSONArray(jsonString);

        //parse out ceo
        JSONObject ceoJsonObject = json.getJSONObject(0);
        ceo = parseOutEmployee(ceoJsonObject, true);

        teams = new ArrayList<Team>();
        //parse out teams
        for (int i = 1; i < json.length(); i++)
        {
            JSONObject teamJsonObject = json.getJSONObject(i);
            Team team = parseOutTeam(teamJsonObject);
            teams.add(team);
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
            Employee employee = parseOutEmployee(personJsonObject, false);
            teamMembers.add(employee);
        }

        return new Team(teamName, teamMembers);
    }

    private static Employee parseOutEmployee(JSONObject personJsonObject, boolean ceo)
            throws JSONException
    {
        String id = personJsonObject.getString("id");
        String firstName = personJsonObject.getString("firstName");
        String lastName = personJsonObject.getString("lastName");
        String role = personJsonObject.getString("role");
        String profileImageURL = personJsonObject.getString("profileImageURL");

        boolean teamLeader = false;
        try
        {
            teamLeader = personJsonObject.getBoolean("teamLead");
        }
        catch (JSONException e)
        {
            // no tag for teamLead, it's ok, false by default
        }

        return new Employee(id, firstName, lastName, role, profileImageURL, teamLeader, ceo);
    }
}
