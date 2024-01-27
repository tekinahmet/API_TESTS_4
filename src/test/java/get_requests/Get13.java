package get_requests;

import base_urls.SpaceXBaseUrl;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.hasItems;

public class Get13 extends SpaceXBaseUrl {
            /*
    Given
        https://api.spacexdata.com/v3/launches
    When
        User sends Get Request to the Url
    Then
        Status code is 200
    And
        There are  111 launches
    And
        "Falcon 1" and "Falcon 9" are among the rocket names
    And
        25 launches are fired on 2020
    And
        "Trailblazer" is one of the  failed mission name



     */
    @Test
    public void get(){
        // Set Url:
        spec.pathParam("first","launches");

        // Set Expected Data:

        // Sent Request and Get Response:
       Response response =  given(spec).when().get("{first}");
       response.prettyPrint();

       // Do Assertions:
        JsonPath json = response.jsonPath();

        //  Status code is 200

        assertEquals(200,response.statusCode());

       // There are  111 launches
        int numOfFlights = json.getList("flight_number").size();
        assertEquals(111,numOfFlights);

        //  "Falcon 1" and "Falcon 9" are among the rocket names
        List<String> rocketNamesList =  json.getList("rocket.rocket_name");
        assertTrue(rocketNamesList.contains("Falcon 1"));
        assertTrue(rocketNamesList.contains("Falcon 9"));

        response.then()
                .body("rocket.rocket_name",hasItems("Falcon 1","Falcon 9"));



        // 25 launches are fired on 2020
           int flightNums2020 = json.getList("findAll{it.launch_year=='2020'}").size();
           assertEquals(25,flightNums2020);
          List<List<Integer>> flightTime = json.getList("rocket.second_stage.payloads.flight_time_sec");
        System.out.println(flightTime);
        System.out.println(flightTime.get(0).size());
        List<Integer> flightTimeList = new ArrayList<>();
        for (List<Integer> w: flightTime){
       // if (w.size()==1) {
            if (w.get(0) != null) {
                flightTimeList.add(w.get(0));
            }

       // }
        }
        System.out.println("flightTimeList = " + flightTimeList);
        Collections.sort(flightTimeList,Comparator.reverseOrder());
        System.out.println("flightTimeList = " + flightTimeList);

        Integer longest =flightTimeList.get(0);
        System.out.println(longest);
        List<Integer> longer = new ArrayList<>();
        longer.add(longest);
        System.out.println(json.getList("findAll{it.launch_success==false}.mission_name"));

        List<String> name =json.getList("findAll{it.rocket.second_stage.payloads.flight_time_sec=="+longer+"}.mission_name");
        System.out.println("name.get(0) = " + name.get(0));
    }
}
