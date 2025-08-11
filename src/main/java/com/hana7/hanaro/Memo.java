// package com.hana7.hanaro;
//
// import java.time.LocalDateTime;
//
// import org.hibernate.annotations.ColumnDefault;
// import org.hibernate.annotations.DynamicInsert;
//
// import com.hana7.hanaro.BaseEntity;
// import com.hana7.hanaro.MemoState;
//
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.EqualsAndHashCode;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.ToString;
//
// @Entity
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// @Getter
// @Setter
// @ToString(callSuper = true)
// @EqualsAndHashCode(callSuper = true)
// @DynamicInsert
// public class Memo extends BaseEntity {
// 	@Id
// 	@GeneratedValue(strategy = GenerationType.IDENTITY)
// 	private int mno;
//
// 	@Column(nullable = false, length = 200)
// 	private String memoText;
//
// 	@Enumerated(EnumType.STRING)
// 	@ColumnDefault("'PAYED'")
// 	private MemoState state;
//
// 	@Column(columnDefinition
// 		= "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
// 	private LocalDateTime statedAt;
// }
