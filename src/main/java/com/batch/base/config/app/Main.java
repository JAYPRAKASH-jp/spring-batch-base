package com.batch.base.config.app;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import com.batch.base.config.BatchLauncher;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class Main {

	public static void main(String args) {
		Main main = new Main();
		ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
//		MyBatchConfiguration batchConfig = context.getBean(MyBatchConfiguration.class);
		BatchLauncher<String, String> batchLauncher = new BatchLauncher<String, String>(context);
		batchLauncher.run(main.myReader(), main.myProcessor(), main.myWriter());
	}

	public static void main2(String args) {
		Main main = new Main();
		ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
//		MyBatchConfiguration batchConfig = context.getBean(MyBatchConfiguration.class);
		BatchLauncher<List<Integer>, List<Integer>> batchLauncher = new BatchLauncher<List<Integer>, List<Integer>>(
				context);
		batchLauncher.run(main.myReader1(), main.myProcessor1(), main.myWriter1());
	}

	public DefaultReader<String> myReader() {
		List<String> data = Arrays.asList("One", "Two", "Three", "Four", "Five");
		return new DefaultReader<String>(data);
	}

	public DefaultProcessor<String, String> myProcessor() {
		return new DefaultProcessor<String, String>();
	}

	public DefaultWriter<String> myWriter() {
		return new DefaultWriter<String>();
	}

	public DefaultReader<List<Integer>> myReader1() {
		List<List<Integer>> data = Arrays.asList(Arrays.asList(1, 2, 3, 4));
		return new DefaultReader<List<Integer>>(data);
	}

	public DefaultProcessor<List<Integer>, List<Integer>> myProcessor1() {
		return new DefaultProcessor<List<Integer>, List<Integer>>();
	}

	public DefaultWriter<List<Integer>> myWriter1() {
		return new DefaultWriter<List<Integer>>();
	}

	public static void main(String[] args) {
		String line = "abc,\"xyz , abc\",jkl";
		String[] columns = new String[6];
		patterSplit(line, columns);
	}

	public static void patterSplit(String line, String[] columns) {
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|([^,]*)");
        Matcher matcher = pattern.matcher(line);
        
        // Iterate through the matches and print each part
        while (matcher.find()) {
        	String part = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
        	if(!part.isEmpty()) {
        		System.out.println(part.trim());
        	}
        }
	}

	public static void normalSplit(String line) {
		String[] columns = line.split(",");
		for (int i = 0; i < columns.length; i++) {
			System.out.println(columns[0]);
		}
	}
}
