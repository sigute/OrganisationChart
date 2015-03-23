package com.github.sigute.organisationchart.tests.utils;

import android.test.InstrumentationTestCase;

import com.github.sigute.organisationchart.R;
import com.github.sigute.organisationchart.organisation.Employee;
import com.github.sigute.organisationchart.organisation.Organisation;
import com.github.sigute.organisationchart.organisation.Team;
import com.github.sigute.organisationchart.utils.JsonParser;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Test cases for JsonParser
 *
 * @author Sigute
 */
public class JsonParserTest extends InstrumentationTestCase
{
    private String validJson;

    @Override
    protected void setUp() throws Exception
    {
        InputStream is = getInstrumentation().getTargetContext().getResources()
                .openRawResource(R.raw.json_test);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            json.append(line);
        }

        validJson = json.toString();
    }

    public void testParseOutInvalidJsonData() throws Exception
    {
        try
        {
            Organisation organisation = JsonParser.parseOutJsonData("invalidData");
            assertFalse("Parsed out organisation from invalid string", organisation != null);
        }
        catch (JSONException e)
        {
            assertTrue("Could not parse invalid string, as expected", true);
        }
    }

    public void testParseOutValidJsonData() throws Exception
    {
        Organisation organisation;
        try
        {
            organisation = JsonParser.parseOutJsonData(validJson);
        }
        catch (JSONException e)
        {
            assertFalse("Could not parse the JSON string!", false);
            return;
        }

        assertTrue("Parsed out organisation from string", organisation != null);

        Employee ceo = organisation.getCEO();
        assertTrue("Organisation has CEO", ceo != null);
        assertTrue("CEO is actually CEO", ceo.isCEO() == true);
        assertTrue("CEO has correct name", ceo.getFirstName().equals("Mark"));
        assertTrue("CEO has correct name", ceo.getLastName().equals("Mason"));
        assertTrue("CEO has correct id", ceo.getId().equals("001"));
        assertTrue("CEO has correct role", ceo.getRole().equals("CEO"));
        assertTrue("CEO has correct profile image URL", ceo.getImageURL()
                .equals("http://mubaloo.com/dev/developerTestResources/profilePlaceholder.png"));

        List<Team> teams = organisation.getTeams();
        assertTrue("Organisation has two teams", teams.size() == 2);

        List<Employee> employees = organisation.getAllEmployees();
        assertTrue("Organisation has five employees", employees.size() == 5);

        //TODO could continue adding things here, like verifying each team leader, etc...
    }
}