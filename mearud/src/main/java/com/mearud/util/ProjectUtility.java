package com.mearud.util;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.mearud.maker.EndMaker;

public class ProjectUtility {

    public static Map<String, Function> projectMap;

    private static Function<List<String>, Boolean> reframe = (projectName) -> {
        return EndMaker.constructReframeFrontendCalled(projectName);
    };

    private static Function<List<String>, Boolean> ringJetty = (projectName) -> {
        return EndMaker.constructRingJettyBackendCalled(projectName);
    };

    static {
        projectMap = new HashMap<>();
        projectMap.put("reframe", reframe);
        projectMap.put("ringJetty", ringJetty);
    }

}
