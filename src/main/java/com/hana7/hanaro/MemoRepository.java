package com.hana7.hanaro;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import jakarta.transaction.Transactional;

public interface MemoRepository extends JpaRepository<Memo, Integer>, QuerydslPredicateExecutor<Memo> {
	@Modifying
	@Transactional
	@Query("update Memo m set m.state = :nextState, m.statedAt = now() where m.state = :state and m.statedAt <= :timeToUp")
	int updateStateBatch(@Param("state") MemoState state, @Param("timeToUp") LocalDateTime timeToUp, @Param("nextState") MemoState nextState);
}
