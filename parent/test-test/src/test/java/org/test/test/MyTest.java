package org.test.test;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class MyTest {

	@Test
	public void testClass() {
		Class clazz = null;
		clazz = Person.class;
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getMethods();
		Method[] declaredMethods = clazz.getDeclaredMethods();
		InputStream in1 = null;
		URL url = MyTest.class.getResource("");
		URL url1 = MyTest.class.getResource("/");
		
		System.out.println();
		
	}
	
	@Test
	public void test1() {
		URL url = MyTest.class.getResource("");
		URL url1 = MyTest.class.getResource("/");
		File file = new File("/Users/tao/eclipse-workspace/parent/test-dao/target/test-classes/org/test/dao");
		ArrayList<File> fileList = new ArrayList<File>();
		File []files = file.listFiles();
		System.out.println();
	}
	
	@Test
	public void test2() throws ClassNotFoundException {
		//org.test.dao.App aa;
		String name = "org.test.dao.MyTest1";
		Class clazz = Class.forName(name);
		Method[] declaredMethods = clazz.getDeclaredMethods();
		for(Method method : declaredMethods)  {
			System.out.println(method.getName());
		}
		
		System.out.println();
	}
	
	@Test
	public void test3() {
		  //1. get all classes from old SDK
		  //2. get all classes from new SDK
		  //3. find same classes
		  //4. find different classes
		  //4.1 different classes in old
		  //4.2 different classed in new
		  //5. different methods
		  //5.1 different methods in old 
		  //5.2 different method in new 
		String[] oldClassNames = {};
		String[] newClassNames = {};
	}
	
	public void compareSDK(String oldSDKPath, String newSDKPath, String oldPackageName, String newPackageName) {
		List<String> oldClassNames = getClassNames(oldSDKPath);
		List<String> newClassNames = getClassNames(newSDKPath);
		
		Map<String, Set<String>> classNamesMap = compareOldAndNew(oldClassNames, newClassNames);
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
				Map<String, Set<String>> methodNamesMap = compareOldAndNew(oldMethodNames, newMethodNames);
				System.out.println("Method(s) only in old class: ");
				methodNamesMap.get("onlyInOld").forEach(name -> System.out.println(name));
				System.out.println("");
				System.out.println("Method(s) only in new class: ");
				methodNamesMap.get("onlyInNew").forEach(name -> System.out.println(name));
			} catch (SecurityException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	public List<String> getClassNames(String path) {
		File dir = new File(path);
		File []files = dir.listFiles();
		return Arrays.asList(files).stream().map(file -> file.getName().replace(".class", "")).collect(Collectors.toList());
	}
	
	public List<String> getMethodNames(Method[] methods) {
		return Arrays.asList(methods).stream().map(method -> method.getName()).collect(Collectors.toList());
	}
	
	public Map<String, Set<String>> compareOldAndNew(List<String> oldNames, List<String> newNames) {
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
		
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		map.put("same", same.stream().sorted().collect(Collectors.toSet()));
		map.put("onlyInNew", onlyInNew.stream().sorted().collect(Collectors.toSet()));
		map.put("onlyInOld", onlyInOld.stream().sorted().collect(Collectors.toSet()));
		return map;
	}
}
