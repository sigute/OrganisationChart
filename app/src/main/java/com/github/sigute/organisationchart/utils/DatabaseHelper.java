package com.github.sigute.organisationchart.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.sigute.organisationchart.organisation.Employee;
import com.github.sigute.organisationchart.organisation.Organisation;
import com.github.sigute.organisationchart.organisation.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spikereborn on 22/03/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "organisation.db";
    private static final int DATABASE_VERSION = 1;

    private class TableNames
    {
        static final String ORGANISATION = "organisation";
        static final String EMPLOYEES = "employees";
        static final String TEAMS = "teams";
        static final String TEAM_MEMBERS = "team_members";
    }

    private class OrganisationTableColumns
    {
        static final String CEO_ID = "ceo_id";
        static final String NAME = "name";
    }

    private class EmployeesTableColumns
    {
        static final String ID = "id";
        static final String FIRST_NAME = "first_name";
        static final String LAST_NAME = "last_name";
        static final String ROLE = "role";
        static final String IMAGE_URL = "image_url";
        static final String TEAM_LEADER = "team_leader";
    }

    private class TeamsTableColumns
    {
        static final String NAME = "name";
    }

    private class TeamMembersTableColumns
    {
        static final String TEAM_NAME = "team_name";
        static final String EMPLOYEE_ID = "employee_id";
    }

    public static final String CREATE_ORGANISATION_TABLE = //
            "CREATE TABLE  " + TableNames.ORGANISATION + //
                    "(" + //
                    OrganisationTableColumns.CEO_ID + " text not null, " + //
                    OrganisationTableColumns.NAME + " text not null" + //
                    ");";

    public static final String CREATE_EMPLOYEES_TABLE = //
            "CREATE TABLE  " + TableNames.EMPLOYEES + //
                    "(" + //
                    EmployeesTableColumns.ID + " text not null, " + //
                    EmployeesTableColumns.FIRST_NAME + " text not null, " + //
                    EmployeesTableColumns.LAST_NAME + " text not null, " + //
                    EmployeesTableColumns.ROLE + " text not null, " + //
                    EmployeesTableColumns.IMAGE_URL + " text not null, " + //
                    EmployeesTableColumns.TEAM_LEADER + " boolean not null check (" + EmployeesTableColumns.TEAM_LEADER + " in (0,1))" + //
                    ");";

    public static final String CREATE_TEAM_TABLE = //
            "CREATE TABLE  " + TableNames.TEAMS + //
                    "(" + //
                    TeamsTableColumns.NAME + " text not null" + //
                    ");";

    public static final String CREATE_TEAM_MEMBERS_TABLE = //
            "CREATE TABLE  " + TableNames.TEAM_MEMBERS + //
                    "(" + //
                    TeamMembersTableColumns.TEAM_NAME + " text not null, " + //
                    TeamMembersTableColumns.EMPLOYEE_ID + " text not null" + //
                    ");";


    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_ORGANISATION_TABLE);
        sqLiteDatabase.execSQL(CREATE_EMPLOYEES_TABLE);
        sqLiteDatabase.execSQL(CREATE_TEAM_TABLE);
        sqLiteDatabase.execSQL(CREATE_TEAM_MEMBERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
    }

    public void insertOrganisation(Organisation organisation)
    {
        SQLiteDatabase database = instance.getWritableDatabase();

        insertAllEmployees(organisation, database);
        insertOrganisationTable(organisation, database);
        insertTeamsAndTeamMembers(organisation, database);

        database.close();
    }

    private void insertTeamsAndTeamMembers(Organisation organisation, SQLiteDatabase database)
    {
        List<Team> allTeams = organisation.getTeams();
        for (Team team : allTeams)
        {
            insertTeamName(team.getName(), database);
            List<Employee> allTeamMembers = team.getTeamMembers();
            allTeamMembers.add(team.getTeamLeader());

            insertTeamMembers(allTeamMembers, team.getName(), database);
        }
    }

    private void insertTeamMembers(List<Employee> teamMembers, String teamName,
            SQLiteDatabase database)
    {
        for (Employee teamMember : teamMembers)
        {
            ContentValues values = new ContentValues();
            values.put(TeamMembersTableColumns.TEAM_NAME, teamName);
            values.put(TeamMembersTableColumns.EMPLOYEE_ID, teamMember.getId());
            long result = database.insert(TableNames.TEAMS, null, values);
            if (result == -1)
            {
                //TODO error occured!! do something!
            }
        }
    }

    private void insertTeamName(String name, SQLiteDatabase database)
    {
        ContentValues values = new ContentValues();
        values.put(TeamsTableColumns.NAME, name);
        long result = database.insert(TableNames.TEAMS, null, values);
        if (result == -1)
        {
            //TODO error occured!! do something!
        }
    }

    private void insertOrganisationTable(Organisation organisation, SQLiteDatabase database)
    {
        ContentValues values = new ContentValues();
        values.put(OrganisationTableColumns.CEO_ID, organisation.getCEO().getId());
        values.put(OrganisationTableColumns.NAME, "Mubaloo");
        long result = database.insert(TableNames.ORGANISATION, null, values);
        if (result == -1)
        {
            //TODO error occured!! do something!
        }
    }

    private void insertAllEmployees(Organisation organisation, SQLiteDatabase database)
    {
        List<Employee> allEmployees = organisation.getAllEmployees();
        for (Employee employee : allEmployees)
        {
            ContentValues values = new ContentValues();
            values.put(EmployeesTableColumns.ID, employee.getId());
            values.put(EmployeesTableColumns.FIRST_NAME, employee.getFirstName());
            values.put(EmployeesTableColumns.LAST_NAME, employee.getLastName());
            values.put(EmployeesTableColumns.ROLE, employee.getRole());
            values.put(EmployeesTableColumns.IMAGE_URL, employee.getImageURL());
            values.put(EmployeesTableColumns.TEAM_LEADER, employee.isTeamLeader() ? 1 : 0);
            long result = database.insert(TableNames.EMPLOYEES, null, values);
            if (result == -1)
            {
                //TODO error occured!! do something!
            }
        }
    }

    public Organisation selectOrganisation()
    {
        SQLiteDatabase database = instance.getReadableDatabase();

        Employee ceo = selectCEO(database);
        List<Team> teams = selectTeams(database);

        database.close();
        return new Organisation(ceo, teams);
    }

    private Employee selectCEO(SQLiteDatabase database)
    {
        String ceoID = selectCeoID(database);
        return selectEmployee(ceoID, true, database);
    }

    private String selectCeoID(SQLiteDatabase database)
    {
        Cursor cursor = database.query(TableNames.ORGANISATION, // table
                new String[]{OrganisationTableColumns.CEO_ID}, // column
                OrganisationTableColumns.NAME + "=?", // where
                new String[]{"Mubaloo"}, // where value
                null, null, null);

        if (cursor == null || !cursor.moveToNext())
        {
            //TODO no results, return error
        }

        return cursor.getString(cursor.getColumnIndex(OrganisationTableColumns.CEO_ID));
    }

    private Employee selectEmployee(String employeeId, boolean ceo, SQLiteDatabase database)
    {
        Cursor cursor = database.query(TableNames.EMPLOYEES, // table
                new String[]{ // columns
                        EmployeesTableColumns.ID, //
                        EmployeesTableColumns.FIRST_NAME, //
                        EmployeesTableColumns.LAST_NAME,//
                        EmployeesTableColumns.ROLE, //
                        EmployeesTableColumns.IMAGE_URL, //
                        EmployeesTableColumns.TEAM_LEADER}, //
                EmployeesTableColumns.ID + "=?", //where
                new String[]{employeeId}, // where value
                null, null, null);

        if (cursor == null || !cursor.moveToNext())
        {
            //TODO no results, return error
        }

        String id = cursor.getString(cursor.getColumnIndex(EmployeesTableColumns.ID));
        String firstName = cursor
                .getString(cursor.getColumnIndex(EmployeesTableColumns.FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(EmployeesTableColumns.LAST_NAME));
        String role = cursor.getString(cursor.getColumnIndex(EmployeesTableColumns.ROLE));
        String imageURL = cursor.getString(cursor.getColumnIndex(EmployeesTableColumns.IMAGE_URL));
        boolean teamLeader = (cursor.getInt(cursor
                .getColumnIndex(EmployeesTableColumns.TEAM_LEADER)) == 1) ? true : false;

        return new Employee(id, firstName, lastName, role, imageURL, teamLeader, ceo);
    }

    private List<Team> selectTeams(SQLiteDatabase database)
    {
        List<Team> teams = new ArrayList<>();

        List<String> teamNames = selectTeamNames(database);
        for (String teamName : teamNames)
        {
            List<Employee> teamMembers = selectTeamMembers(teamName, database);
            teams.add(new Team(teamName, teamMembers));
        }

        return teams;
    }

    private List<Employee> selectTeamMembers(String teamName, SQLiteDatabase database)
    {
        List<String> employeeIds = selectEmployeeIds(teamName, database);

        List<Employee> employees = new ArrayList<>();
        for (String employeeId : employeeIds)
        {
            employees.add(selectEmployee(employeeId, false, database));
        }

        return employees;
    }

    private List<String> selectEmployeeIds(String teamName, SQLiteDatabase database)
    {
        Cursor cursor = database.query(TableNames.TEAM_MEMBERS, // table
                new String[]{TeamMembersTableColumns.EMPLOYEE_ID}, // column
                TeamMembersTableColumns.TEAM_NAME + "=?", // where
                new String[]{teamName}, // where value
                null, null, null);

        if (cursor == null || !cursor.moveToNext())
        {
            //TODO no results, return error
        }

        List<String> employeeIds = new ArrayList<>();
        do
        {
            employeeIds.add(cursor.getString(cursor.getColumnIndex(TeamsTableColumns.NAME)));
        }
        while (cursor.moveToNext());

        return employeeIds;
    }

    private List<String> selectTeamNames(SQLiteDatabase database)
    {
        Cursor cursor = database.query(TableNames.TEAMS, // table
                new String[]{TeamsTableColumns.NAME}, // column
                null, null, null, null, null);

        if (cursor == null || !cursor.moveToNext())
        {
            //TODO no results, return error
        }

        List<String> teamNames = new ArrayList<>();
        do
        {
            teamNames.add(cursor.getString(cursor.getColumnIndex(TeamsTableColumns.NAME)));
        }
        while (cursor.moveToNext());

        return teamNames;
    }
}
