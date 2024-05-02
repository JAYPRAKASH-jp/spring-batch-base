package com.batch.base.config.app;

import org.springframework.batch.item.ItemProcessor;

public class DefaultProcessor<Input, Output> implements ItemProcessor<Input, Output> {

	@SuppressWarnings("unchecked")
	@Override
	public Output process(Input item) throws Exception {
		System.out.println("Processor Started.");
		return (Output) item;
	}
}
