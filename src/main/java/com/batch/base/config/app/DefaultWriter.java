package com.batch.base.config.app;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class DefaultWriter<Output> implements ItemWriter<Output> {


	@Override
	public void write(List<? extends Output> items) throws Exception {
		 items.forEach(System.out::println);
	}
}
