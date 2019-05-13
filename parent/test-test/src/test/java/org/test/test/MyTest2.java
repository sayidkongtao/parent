package org.test.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MyTest2 {
	public static void main(String args[]) throws ClassNotFoundException, IOException {
		String oldJarFilePath = "C:\\Users\\Wind\\Desktop\\a\\hadean-java-client\\1.0.86\\hadean-java-client-1.0.86-tests.jar";
		String newJarFilePath = "C:\\Users\\Wind\\Desktop\\a\\hadean-java-client\\1.0.102\\hadean-java-client-1.0.102-tests.jar";
		compareJar(oldJarFilePath, newJarFilePath);
		System.out.println(
				"##################################################################################################");
	}

	public static void compareJar(String oldJarFilePath, String newJarFilePath)
			throws ClassNotFoundException, IOException {
		List<String> oldClassNameList = getJarClassNames(oldJarFilePath);
		List<String> newClassNameList = getJarClassNames(newJarFilePath);
		List<String> sameClasses = sortTwoList(oldClassNameList, newClassNameList);
		System.out.println(
				"##################################################################################################");
		List<String> oldMethodNameList = null;
		List<String> newMethodNameList = null;

		for (String sameClasse : sameClasses) {
			oldMethodNameList = getDeclaredMethods(oldJarFilePath, sameClasse);
			newMethodNameList = getDeclaredMethods(newJarFilePath, sameClasse);
			sortTwoClassNameList(oldMethodNameList, newMethodNameList, sameClasse);
		}
	}

	public static void sortTwoClassNameList(List<String> oldJar, List<String> newJar, String className) {
		List<String> sameNames = new ArrayList<String>();
		List<String> onlyInOldJar = new ArrayList<String>();
		List<String> onlyInNewJar = new ArrayList<String>();

		for (String name : oldJar) {
			if (newJar.contains(name)) {
				sameNames.add(name);
			} else {
				onlyInOldJar.add(name);
			}
		}

		for (String name : newJar) {
			if (!sameNames.contains(name)) {
				onlyInNewJar.add(name);
			}
		}

		if (!(onlyInOldJar.size() == 0 && onlyInNewJar.size() == 0)) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("For: " + className);
			System.out.println("only in oldJar: " + onlyInOldJar.size());
			onlyInOldJar.forEach(System.out::println);
			System.out.println("Total number in oldJar: " + oldJar.size());
			System.out.println("**********************************");
			System.out.println("added in newJar: " + onlyInNewJar.size());
			onlyInNewJar.forEach(System.out::println);
			System.out.println("**********************************");
			System.out.println("Total number in newJar: " + newJar.size());
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
	}

	public static List<String> sortTwoList(List<String> oldJar, List<String> newJar) {
		List<String> sameNames = new ArrayList<String>();
		List<String> onlyInOldJar = new ArrayList<String>();
		List<String> onlyInNewJar = new ArrayList<String>();

		for (String name : oldJar) {
			if (newJar.contains(name)) {
				sameNames.add(name);
			} else {
				onlyInOldJar.add(name);
			}
		}

		for (String name : newJar) {
			if (!sameNames.contains(name)) {
				onlyInNewJar.add(name);
			}
		}

		System.out.println("only in oldJar: " + onlyInOldJar.size());
		onlyInOldJar.forEach(System.out::println);
		System.out.println("Total number in oldJar: " + oldJar.size());
		System.out.println("**********************************");
		System.out.println("added in newJar: " + onlyInNewJar.size());
		onlyInNewJar.forEach(System.out::println);
		System.out.println("**********************************");
		System.out.println("Total number in newJar: " + newJar.size());

		return sameNames;
	}

	public static List<String> getDeclaredMethods(String jarFilepath, String className)
			throws MalformedURLException, ClassNotFoundException {

		File file = new File(jarFilepath);
		URL url = file.toURI().toURL();
		ClassLoader loader = new URLClassLoader(new URL[] { url });
		List<String> declaredMethods = new ArrayList<String>();

		Class<?> clazz = loader.loadClass(className);
		Method method[] = clazz.getDeclaredMethods();

		for (int i = 0; i < method.length; ++i) {
			declaredMethods.add(method[i].getName());
		}

		return declaredMethods;
	}

	public static List<String> getJarClassNames(String jarFilePath) throws ClassNotFoundException, IOException {
		// "C:\\Users\\Wind\\Desktop\\a\\hadean-java-client\\1.0.102\\hadean-java-client-1.0.102-tests.jar"
		File file = new File(jarFilePath);
		URL url = file.toURI().toURL();
		ClassLoader loader = new URLClassLoader(new URL[] { url });
		List<String> jarClassNames = new ArrayList<String>();
		JarFile jar = new JarFile(file);
		Enumeration<JarEntry> enumFiles = jar.entries();
		JarEntry entry;

		while (enumFiles.hasMoreElements()) {
			entry = (JarEntry) enumFiles.nextElement();
			// System.out.println(entry.getName());
			if (entry.getName().indexOf("META-INF") < 0) {
				String classFullName = entry.getName();
				if (classFullName.indexOf(".class") < 0) {
					classFullName = classFullName.substring(0, classFullName.length() - 1);
					// System.out.println(classFullName);
				} else {
					String className = classFullName.substring(0, classFullName.length() - 6).replace("/", ".");
					jarClassNames.add(className);
					// System.out.println(className);
				}
			}
		}

		return jarClassNames;
	}
}
