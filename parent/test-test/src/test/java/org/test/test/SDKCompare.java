package org.test.test;

import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class SDKCompare {

    @Test
    public void test() {
        compareSDK("C:\\Users\\kongtao01\\IdeaProjects\\hadean\\hadean-test-web-api\\src\\test\\java\\hadean\\api\\tests",
                "C:\\Users\\kongtao01\\IdeaProjects\\hadean\\hadean-test-web-api\\src\\test\\java\\api",
                "hadean.api.tests",
                "api");
    }

    public void compareSDK(String oldSDKPath, String newSDKPath, String oldPackageName, String newPackageName) {
        List<String> oldClassNames = getClassNames(oldSDKPath);
        List<String> newClassNames = getClassNames(newSDKPath);

        Map<String, List<String>> classNamesMap = compareOldAndNew(oldClassNames, newClassNames);
        System.out.println("##################################################");
        System.out.println("Class(es) only in old sdk: ");
        classNamesMap.get("onlyInOld").forEach(name -> System.out.println(name));
        System.out.println("");
        System.out.println("##################################################");
        System.out.println("Class(es) only in new sdk: ");
        classNamesMap.get("onlyInNew").forEach(name -> System.out.println(name));
        System.out.println("");
        System.out.println("##################################################");
        classNamesMap.get("same").forEach(clazz -> {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("For method in the same class:" + clazz);
            try {
                Method[] oldMethods = Class.forName(oldPackageName + "." + clazz).getDeclaredMethods();
                List<String> oldMethodNames = getMethodNames(oldMethods);
                Method[] newMethods = Class.forName(newPackageName + "." + clazz).getDeclaredMethods();
                List<String> newMethodNames = getMethodNames(newMethods);
                Map<String, List<String>> methodNamesMap = compareOldAndNew(oldMethodNames, newMethodNames);
                if (methodNamesMap.get("onlyInOld").size() != 0 && methodNamesMap.get("onlyInNew").size() != 0) {
                    System.out.println("Method(s) only in old class: ");
                    methodNamesMap.get("onlyInOld").forEach(name -> System.out.println(name));
                    System.out.println("");
                    System.out.println("Method(s) only in new class: ");
                    methodNamesMap.get("onlyInNew").forEach(name -> System.out.println(name));
                } else {
                    System.out.println("None different");
                }

            } catch (SecurityException | ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public List<String> getClassNames(String path) {
        File dir = new File(path);
        File []files = dir.listFiles();
        return Arrays.asList(files).stream().map(file -> file.getName().replace(".class", "").replace(".java", "")).collect(Collectors.toList());
    }

    public List<String> getMethodNames(Method[] methods) {
        return Arrays.asList(methods).stream().filter(file -> file.getName().endsWith("Test")).map(method -> method.getName()).collect(Collectors.toList());
    }

    public Map<String, List<String>> compareOldAndNew(List<String> oldNames, List<String> newNames) {
        Set<String> same = new HashSet<String>();
        Set<String> onlyInNew = new HashSet<String>();
        Set<String> onlyInOld = new HashSet<String>();

        for (String name: oldNames) {
            if(newNames.contains(name)) {
                same.add(name);
            } else {
                onlyInOld.add(name);
            }
        }

        for (String name: newNames) {
            if(oldNames.contains(name)) {
                same.add(name);
            } else {
                onlyInNew.add(name);
            }
        }

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("same", same.stream().sorted().collect(Collectors.toList()));
        map.put("onlyInNew", onlyInNew.stream().sorted().collect(Collectors.toList()));
        map.put("onlyInOld", onlyInOld.stream().sorted().collect(Collectors.toList()));
        return map;
    }
}
