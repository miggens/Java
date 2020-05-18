package com.mearud.maker;

import com.mearud.util.ColorUtility;
import com.mearud.util.ReframeFileUtility;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * EndMaker is used to create CLJS Frontend Templates and CLJ/Ring Backend Templates.
 */
public class EndMaker {

    public static boolean constructReframeFrontendCalled(List<String> projectArgs) {

        boolean success = true;
        Function<Path, Path> createDir = (path) -> {

            try {
                return Files.createDirectory(path);
            }
            catch (IOException ioe) {
                System.out.println(ColorUtility.wrapError("Failed to create Directory "+path.toString()+"\n"+ioe.getMessage()));
            }
            return null;
        };

        Function<Path, Path> createFile = (path) -> {

            try {
                return Files.createFile(path);
            }
            catch (IOException ioe) {
                System.out.println(ColorUtility.wrapError("Failed to create File "+path.toString()+"\n"+ioe.getMessage()));
            }

            return null;
        };

        //validate projectArgs
        if (!validateReframeArgs(projectArgs)) {
            System.out.println(ColorUtility.wrapError("[constructReframeFrontendCalled] Invalid project args "));
            projectArgs.forEach( a -> { System.out.println(ColorUtility.wrapError("Args -> " + a)); } );
        }
        else {
            List<Path> projectPaths = new ArrayList<>();
            Path pwd = getRootDirPath();
            String projectName = projectArgs.remove(0);
            String projectNameUnderscore = projectName.replace("-", "_");
            pwd.resolve(projectName);

            Path projectRoot = Paths.get(pwd.toString()+"/"+projectName);
            Path projectSrcDir = Paths.get(projectRoot.toString() + "/src");
            Path namespaceRoot = Paths.get(projectSrcDir.toString()+"/"+projectNameUnderscore);
            Path namespaceCore = Paths.get(namespaceRoot.toString()+"/core.cljs");
            Path namespaceEvents = Paths.get(namespaceRoot+"/events");
            Path nsEventsCore = Paths.get(namespaceEvents.toString()+"/core.cljs");
            Path namespaceViews = Paths.get(namespaceRoot+"/views");
            Path nsViewsCore = Paths.get(namespaceViews.toString()+"/core.cljs");
            Path namespaceSubs = Paths.get(namespaceRoot+"/subs");
            Path nsSubsCore = Paths.get(namespaceSubs.toString()+"/core.cljs");
            Path projectResourceDir = Paths.get(projectRoot.toString() + "/resources");
            Path projectResourcePublic = Paths.get(projectResourceDir.toString()+"/public");
            Path projectResourceIdx = Paths.get(projectRoot.toString()+"/index.html");
            Path projectResourceRobots = Paths.get(projectResourcePublic.toString()+"/robots.txt");
            Path projectPublicCssDir = Paths.get(projectResourcePublic.toString()+"/css");
            Path projectPublicCssStyles = Paths.get(projectPublicCssDir+"/styles.css");
            Path projectDeps = Paths.get(projectRoot.toString()+"/deps.edn");
            Path projectReplOpts = Paths.get(projectRoot.toString()+"/repl.edn");
            Path projectCompileOptsDev = Paths.get(projectRoot.toString()+"/compile-dev.edn");
            Path projectCompileOptsProd = Paths.get(projectRoot.toString()+"/compile-prod.edn");
            Path projectConfigDir = Paths.get(projectRoot.toString()+"/.mearud");

            projectPaths.add(projectRoot);
            projectPaths.add(projectSrcDir);
            projectPaths.add(namespaceRoot);
            projectPaths.add(namespaceCore);
            projectPaths.add(namespaceEvents);
            projectPaths.add(nsEventsCore);
            projectPaths.add(namespaceSubs);
            projectPaths.add(nsSubsCore);
            projectPaths.add(namespaceViews);
            projectPaths.add(nsViewsCore);
            projectPaths.add(projectResourceDir);
            projectPaths.add(projectResourcePublic);
            projectPaths.add(projectResourceRobots);
            projectPaths.add(projectResourceIdx);
            projectPaths.add(projectPublicCssDir);
            projectPaths.add(projectPublicCssStyles);
            projectPaths.add(projectDeps);
            projectPaths.add(projectReplOpts);
            projectPaths.add(projectCompileOptsDev);
            projectPaths.add(projectCompileOptsProd);
            projectPaths.add(projectConfigDir);

            debugPrintProjectStructure(projectPaths);

            List<Path> dirs = projectPaths.stream().filter( f -> {return !f.toFile().getName().contains(".") || f.toFile().isHidden(); } ).collect(Collectors.toList());
            List<Path> writables = projectPaths.stream().filter( f -> { return f.toFile().getName().contains(".") && !f.toFile().isHidden(); } ).collect(Collectors.toList());

            List<Path> createdDirs = dirs.stream().map( createDir ).collect(Collectors.toList());
            List<Path> createdWritables = writables.stream().map( createFile ).collect(Collectors.toList());

            Function<Path, Boolean> writeFile = (path) -> {
                String name = path.toFile().getName();
                String contents = null;
                if (name.contains("core")) {
                    String parentDir = path.getParent().toString();
                    if (parentDir.contains("events")) {
                        contents = ReframeFileUtility.contentsMap.get("events-core");
                    }
                    else if (parentDir.contains("views")) {
                        contents = ReframeFileUtility.contentsMap.get("views-core");
                    }
                    else if (parentDir.contains("subs")) {
                        contents = ReframeFileUtility.contentsMap.get("subs-core");
                    }
                    else {
                        contents = ReframeFileUtility.contentsMap.get("app-core");
                    }
                }
                else {
                    contents = ReframeFileUtility.contentsMap.get(name);
                }

                String finalContents = null;
                if (contents.contains("APP_NAME_UNDERSCORE")) {
                    //System.out.println(ColorUtility.wrapError(contents.replaceAll("APP_NAME_UNDERSCORE", projectNameUnderscore)));
                    finalContents = contents.replaceAll("APP_NAME_UNDERSCORE", projectNameUnderscore);
                }
                else if (contents.contains("APP_NAME")) {
                    //System.out.println(ColorUtility.wrapSuccess(contents.replaceAll("APP_NAME", projectName)));
                    finalContents = contents.replaceAll("APP_NAME", projectName);
                }
                else {
                    finalContents = contents;
                }

                try {
                    BufferedWriter w = Files.newBufferedWriter(path);
                    w.write(finalContents);
                    w.close();
                }
                catch (IOException ioe) {
                    System.out.println(ColorUtility.wrapError("FileWrite Failed"));
                }

                return true;
            };
            writables.forEach( (wr) -> {System.out.println(ColorUtility.wrapInfo2(wr.toFile().getName()+" "+wr.getParent()));} );
            //dirs.forEach( (d) -> { System.out.println(ColorUtility.wrapError(d.toString()));} );

            List<Boolean> writtenFiles = createdWritables.stream().map( writeFile ).collect(Collectors.toList());
            success = writtenFiles.stream().reduce(true, Boolean::equals);
        }

        return success;
    }

    public static boolean constructRingJettyBackendCalled(List<String> projectArgs) {

        if (!validateRingJettyArgs(projectArgs)) {

        }
        else {
            Path pwd = getRootDirPath();
            String projectName = projectArgs.remove(0);
            pwd.resolve(projectName);
            System.out.println(ColorUtility.wrapSuccess("\nCreating new RingJetty Backend called "+projectName ));
        }

        return false;
    }

    public static boolean constructBasicClojureAppCalled(String projectName) {
        Path pwd = getRootDirPath();
        pwd.resolve(projectName);
        System.out.println(ColorUtility.wrapSuccess("\nCreating new ClojureApp called "+projectName ));
        return false;
    }

    public static boolean constructBasicCLJSAppCalled(String projectName, String engine) {
        Path pwd = getRootDirPath();
        pwd.resolve(projectName);
        System.out.println(ColorUtility.wrapSuccess("\nCreating new CLJSApp ["+engine+"] called "+projectName ));

        return false;
    }

    private static Path getRootDirPath() {
        return Paths.get( System.getProperty("user.dir") );
    }

    private static boolean validateReframeArgs(List<String> args) {
        return args.size() != 0;
    }

    private static boolean validateRingJettyArgs(List<String> args) {
        return args.size() != 0;
    }

    private static void debugPrintProjectStructure(List<Path> project) {
        System.out.println(ColorUtility.wrapError("Project Structure:"));
        project.forEach( (path) -> { System.out.println(ColorUtility.wrapSuccess(path.toString()));});
    }
}
