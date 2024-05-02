package com.batch.base.config;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.ConfigurableApplicationContext;

public class BatchLauncher<Input, Output> {

	private Logger logger = Logger.getLogger(BatchLauncher.class);
	private JobBuilderFactory jobBuilderFactory;
	private StepBuilderFactory stepBuilderFactory;
	private final ConfigurableApplicationContext context;
	ItemReader<Input> reader;
	ItemProcessor<Input, Output> processor;
	ItemWriter<Output> writer;
//	private TaskExecutor taskExecutor;

	public BatchLauncher(ConfigurableApplicationContext context) {
		this.context = context;
	}

	private Job createJob(ItemReader<Input> reader, ItemProcessor<Input, Output> processor, ItemWriter<Output> writer) {
		return jobBuilderFactory.get("myJob").listener(new JobExecutionListener() {
			@Override
			public void beforeJob(JobExecution jobExecution) {
				logger.debug("**** Job Started *****");
			}

			@Override
			public void afterJob(JobExecution jobExecution) {
				logger.debug("**** Job Completed *****");
			}
		}).start(createStep(reader, processor, writer)).build();
	}

	private Step createStep(ItemReader<Input> reader, ItemProcessor<Input, Output> processor,
			ItemWriter<Output> writer) {
		return stepBuilderFactory.get("myStep").<Input, Output>chunk(2).reader(reader).processor(processor)
				.writer(writer).listener(new ItemReadListener<Input>() {

					@Override
					public void beforeRead() {
//						System.out.println("**** Reader Started *****");
					}

					@Override
					public void afterRead(Object item) {
//						System.out.println("**** Reader Completed *****");
					}

					@Override
					public void onReadError(Exception ex) {
						logger.debug("**** Reader Failed *****");
					}
				}).listener(new ItemWriteListener<Output>() {

					@Override
					public void beforeWrite(List<? extends Output> items) {
//						System.out.println("**** Writer Started *****");
					}

					@Override
					public void afterWrite(List<? extends Output> items) {
//						System.out.println("**** Writer Completed *****");
					}

					@Override
					public void onWriteError(Exception exception, List<? extends Output> items) {
						logger.debug("**** Writer Failed *****");
					}
				}).build();
	}

	public void run(ItemReader<Input> reader, ItemProcessor<Input, Output> processor, ItemWriter<Output> writer) {
		jobBuilderFactory = context.getBean(JobBuilderFactory.class);
		stepBuilderFactory = context.getBean(StepBuilderFactory.class);
//		taskExecutor = context.getBean(TaskExecutor.class);
		Job job = this.createJob(reader, processor, writer);
		try {
			JobExecution execution = context.getBean(JobLauncher.class).run(job, new JobParameters());
			logger.debug("Exit Status : " + execution.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception while running batch application....");
		} finally {
			context.close();
		}
	}
}