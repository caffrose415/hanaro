package com.hana7.hanaro.order.repository;

import static com.querydsl.core.types.Projections.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import com.hana7.hanaro.member.entity.QMember;
import com.hana7.hanaro.order.dto.AdminOrderSummaryDTO;
import com.hana7.hanaro.order.entity.Order;
import com.hana7.hanaro.order.entity.QOrder;
import com.hana7.hanaro.order.entity.QOrderItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

	private final JPAQueryFactory qf;

	@Override
	public List<AdminOrderSummaryDTO> searchSummaries(
		LocalDateTime start, LocalDateTime end,
		Long itemId, Long memberId) {

		QOrder o = QOrder.order;
		QOrderItem oi = QOrderItem.orderItem;
		QMember m = QMember.member;

		var where = new BooleanBuilder();
		if (start != null)    where.and(o.createdAt.goe(start));
		if (end != null)      where.and(o.createdAt.lt(end));
		if (itemId != null)   where.and(oi.item.id.eq(itemId));
		if (memberId != null) where.and(m.id.eq(memberId));

		return qf
			.select(Projections.constructor(
				AdminOrderSummaryDTO.class,
				o.id,
				m.id,
				m.nickname,
				o.state,
				o.createdAt,
				oi.cnt.sum().coalesce(0),
				oi.cnt.multiply(oi.price).sum().coalesce(0)
			))
			.from(o)
			.join(o.member, m)
			.join(o.orderItems, oi)
			.where(where)
			.groupBy(o.id, m.id, m.nickname, o.state, o.createdAt)
			.orderBy(o.createdAt.desc())
			.fetch(); // ← 페이징 없음
	}

	@Override
	public Optional<Order> findDetailById(Long id) {
		QOrder o = QOrder.order;
		QOrderItem oi = QOrderItem.orderItem;

		Order result = qf.selectFrom(o)
			.join(o.member).fetchJoin()
			.leftJoin(o.orderItems, oi).fetchJoin()
			.leftJoin(oi.item).fetchJoin()
			.where(o.id.eq(id))
			.fetchOne();

		return Optional.ofNullable(result);
	}
}
