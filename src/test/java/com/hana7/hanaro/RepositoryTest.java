package com.hana7.hanaro;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
	"spring.sql.init.mode=never",
	"spring.batch.job.enabled=false",
	"app.batch.enabled=false",
	"app.order.scheduler.enabled=false" // 테스트에서 data.sql 막기
})
public class RepositoryTest {
	protected <T> void print(T entity){
		System.out.println(entity);
	}
}
