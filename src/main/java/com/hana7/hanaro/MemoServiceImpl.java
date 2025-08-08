package com.hana7.hanaro;

import java.time.LocalDateTime;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService {
	private final MemoRepository repository;
	private final JobLauncher jobLauncher;
	private final Job csvJob;

	@Scheduled(cron = "0/15 * * * * *")
	public void updateStateBatch() {
		LocalDateTime now = LocalDateTime.now();
		System.out.println("--- 스케줄러 실행: " + now + " ---");

		// PAYED -> PREPARING (5초 후)
		int payedToPreparing = repository.updateStateBatch(
			MemoState.PAYED,
			now.minusSeconds(MemoState.PAYED.stateInterval()),
			MemoState.PAYED.getNextState()
		);
		if (payedToPreparing > 0) {
			System.out.println("PAYED -> PREPARING: " + payedToPreparing + "건 처리");
		}

		// PREPARING -> TRANSITING (15초 후)
		int preparingToTransiting = repository.updateStateBatch(
			MemoState.PREPARING,
			now.minusSeconds(MemoState.PREPARING.stateInterval()),
			MemoState.PREPARING.getNextState()
		);
		if (preparingToTransiting > 0) {
			System.out.println("PREPARING -> TRANSITING: " + preparingToTransiting + "건 처리");
		}

		// TRANSITING -> DELIVERED (60초 후)
		int transitingToDelivered = repository.updateStateBatch(
			MemoState.TRANSITING,
			now.minusSeconds(MemoState.TRANSITING.stateInterval()),
			MemoState.TRANSITING.getNextState()
		);
		if (transitingToDelivered > 0) {
			System.out.println("TRANSITING -> DELIVERED: " + transitingToDelivered + "건 처리");
		}
	}

	@Override
	// public BatchStatus runBatch(String filePath) throws Exception {
	public BatchStatus runBatch() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
			// .addString("filePath", filePath)
			.toJobParameters();

		return jobLauncher.run(csvJob, jobParameters).getStatus();
	}
}
