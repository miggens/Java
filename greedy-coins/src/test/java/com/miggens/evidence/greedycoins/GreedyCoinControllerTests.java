package com.miggens.evidence.greedycoins;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreedyCoinControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    public static void initTests() {

    }

    @AfterAll
    public static void teardownTests() {

    }

    @Test
    public void testParseAmountEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Map<String, String> in = new HashMap<>();
        in.put("dollarAmount", "0.99");

        HttpEntity<Map<String, String>> postDataEntity = new HttpEntity<>(in, headers);

        ResponseEntity<String> resp = restTemplate.exchange("/greedy/parse", HttpMethod.POST, postDataEntity ,String.class);

        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());

        JSONObject body = (JSONObject) JSONValue.parse(resp.getBody());

        Assert.assertTrue( body.containsKey("dollar") );
        Assert.assertTrue( body.containsKey("halfDollar") );
        Assert.assertTrue( body.containsKey("quarter") );
        Assert.assertTrue( body.containsKey("dime") );
        Assert.assertTrue( body.containsKey("nickel") );
        Assert.assertTrue( body.containsKey("penny") );

        Assert.assertEquals(Long.valueOf("0"), body.get("dollar") );
        Assert.assertEquals(Long.valueOf("1"), body.get("halfDollar") );
        Assert.assertEquals(Long.valueOf("1"), body.get("quarter") );
        Assert.assertEquals(Long.valueOf("2"), body.get("dime") );
        Assert.assertEquals(Long.valueOf("0"), body.get("nickel") );
        Assert.assertEquals(Long.valueOf("4"), body.get("penny") );
    }
}
