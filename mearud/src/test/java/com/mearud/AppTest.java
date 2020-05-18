package com.mearud;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.mearud.cmdparser.CMDParser;
import com.mearud.util.StringUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testCommandLineParser() {
        String[] cmdsFront = {"new", "frontend", "frontend-test-app"};
        String[] cmdsBack = {"new", "backend", "backend-test-app"};
        //finish the full set of commands that will make this shit complete.

        List<String> properOut1 = new ArrayList<>();
        List<String> properOut2 = new ArrayList<>();

        CMDParser p  = new CMDParser(cmdsFront);
        CMDParser p2 = new CMDParser(cmdsBack);

        List<String> outFront = p.getCommandInstructionSet();
        List<String> outBack = p2.getCommandInstructionSet();


        assertEquals(StringUtility.ASSERT_EQUALS_ERROR, properOut1.size(), outFront.size());
        assertEquals(StringUtility.ASSERT_EQUALS_ERROR, properOut2.size(), outBack.size());
        assertArrayEquals(StringUtility.ASSERT_ARRAYS_EQUAL, properOut1.toArray(), outFront.toArray());
        assertArrayEquals(StringUtility.ASSERT_ARRAYS_EQUAL, properOut2.toArray(), outBack.toArray());
        //develop an instruction set to define operations
    }

    @Test
    public void testAppMaker() {
        String ins = "";
        //ins -> process_ids/callable_leftovers

    }

    @Test
    public void testAppRunner() {

    }

    @Test
    public void testAppShipper() {

    }
}
