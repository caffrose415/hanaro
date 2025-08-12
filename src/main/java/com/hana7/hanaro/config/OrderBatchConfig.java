package com.hana7.hanaro.config;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.hana7.hanaro.order.entity.OrderItem;
import com.hana7.hanaro.order.repository.OrderRepository;
import com.hana7.hanaro.stat.entity.SaleItemStat;
import com.hana7.hanaro.stat.entity.SaleStat;
import com.hana7.hanaro.stat.repository.SaleStatRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OrderBatchConfig {
	private final OrderRepository orderRepository;
	private final SaleStatRepository statRepository;

	@Bean
	public Job statJob(JobRepository jobRepository, Step statStep) {
		return new JobBuilder("statJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(statStep)
			.build();
	}

	@Bean
	public Step statStep(JobRepository jobRepository,
		PlatformTransactionManager transactionManager) {
		return new StepBuilder("statStep", jobRepository)
			.<SaleStat, SaleStat>chunk(5, transactionManager)
			.reader(statReader(null)) // StepScope로 CSV 경로 받음
			.processor(statProcessor(null))
			.writer(statWriter())
			.build();
	}

	@Bean
	public ItemWriter<SaleStat> statWriter() {
		return new RepositoryItemWriterBuilder<SaleStat>()
			.repository(statRepository)
			.methodName("save")
			.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<SaleStat, SaleStat> statProcessor(@Value("#{jobParameters['saledt']}") String saledt) {
		System.out.println("xxx - saledt = " + saledt);
		return stat -> {
			SaleStat todayStat = SaleStat.builder()
				.saledt(saledt)
				.ordercnt(stat.getOrdercnt())
				.build();
			System.out.println("bbb - todayStat = " + todayStat);

			List<OrderItem> oitems = orderRepository.getTodayItemStat(saledt);
			List<SaleItemStat> todayItems = oitems.stream()
				.map(oi -> SaleItemStat.builder()
					.saledt(todayStat)
					.item(oi.getItem())
					.amt(oi.getPrice() * oi.getCnt())
					.cnt(oi.getCnt())
					.build())
				.toList();
			System.out.println("bbb - todayItems = " + todayItems);

			todayStat.setSaleItemStats(todayItems);
			int sum = todayItems.stream().mapToInt(SaleItemStat::getAmt).sum();
			todayStat.setTotamt(sum);

			return todayStat;
		};
	}

	@Bean
	@StepScope
	public ItemReader<SaleStat> statReader(@Value("#{jobParameters['saledt']}") String saledt) {
		SaleStat todayStat = orderRepository.getTodayStat(saledt);
		System.out.println("bbr - todayStat = " + todayStat);
		return new ListItemReader<>(List.of(todayStat));
	}

}
