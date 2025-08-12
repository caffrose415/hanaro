package com.hana7.hanaro.stat.entity;

import com.hana7.hanaro.item.entity.Item;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemStat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "saledt",
		foreignKey = @ForeignKey(name = "fk_SaleItemStat_saledt_SaleStat",
			foreignKeyDefinition = """
					foreign key (saledt)
					references SaleStat(saledt)
					on delete cascade
				"""))
	private SaleStat saledt;

	@ManyToOne
	@JoinColumn(name = "item",
		foreignKey = @ForeignKey(name = "fk_OrderItem_item",
			foreignKeyDefinition = """
					foreign key (item) 
					references Item(id)
					on delete cascade
				"""))
	private Item item;

	private int cnt;
	private int amt;

	@Override
	public String toString() {
		return "SaleItemStat{" +
			"id=" + id +
			", saledt=" + saledt.getSaledt() +
			", item=" + item.getName() +
			", cnt=" + cnt +
			", amt=" + amt +
			'}';
	}
}
