package com.batch.base.config.app;

import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

public class DefaultReader<Input> implements ItemReader<Input> {

	private final Iterator<Input> iterator;

	public DefaultReader(List<Input> data) {
		this.iterator = data.iterator();
	}

	@Override
	public Input read() throws Exception {
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			return null;
		}
	}
}
