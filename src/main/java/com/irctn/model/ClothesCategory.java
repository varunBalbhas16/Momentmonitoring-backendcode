package com.irctn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_sortingcategory")
public class ClothesCategory implements Serializable {

	@Id
	@GeneratedValue
	private Integer sortingcategoryid;

	private Double category1;
	private Double category2;
	private Double category3;
	private Double category4;
	private Double category5;
	private Double category6;
	private Double category7;
	private Double category8;
	private Double category9;
	private Double category10;
	private Double category11;
	private Double category12;
	private Double category13;
	private Double category14;
	private Double category15;
	private Double category16;
	private Double category17;
	private Double category18;
	private Double category19;
	
	private Double totalreusable;
	private Double totalwaste;
	
	private Integer numberofitems;

	public Integer getNumberofitems() {
		return numberofitems;
	}

	public void setNumberofitems(Integer numberofitems) {
		this.numberofitems = numberofitems;
	}

	public Double getCategory11() {
		return category11;
	}

	public void setCategory11(Double category11) {
		this.category11 = category11;
	}

	public Double getCategory12() {
		return category12;
	}

	public void setCategory12(Double category12) {
		this.category12 = category12;
	}

	public Double getCategory13() {
		return category13;
	}

	public void setCategory13(Double category13) {
		this.category13 = category13;
	}

	public Double getCategory14() {
		return category14;
	}

	public void setCategory14(Double category14) {
		this.category14 = category14;
	}

	public Double getCategory15() {
		return category15;
	}

	public void setCategory15(Double category15) {
		this.category15 = category15;
	}

	public Double getCategory16() {
		return category16;
	}

	public void setCategory16(Double category16) {
		this.category16 = category16;
	}

	public Double getCategory17() {
		return category17;
	}

	public void setCategory17(Double category17) {
		this.category17 = category17;
	}

	public Double getCategory18() {
		return category18;
	}

	public void setCategory18(Double category18) {
		this.category18 = category18;
	}

	public Double getCategory19() {
		return category19;
	}

	public void setCategory19(Double category19) {
		this.category19 = category19;
	}


	public Integer getSortingcategoryid() {
		return sortingcategoryid;
	}

	public void setSortingcategoryid(Integer sortingcategoryid) {
		this.sortingcategoryid = sortingcategoryid;
	}

	public Double getCategory1() {
		return category1;
	}

	public void setCategory1(Double category1) {
		this.category1 = category1;
	}

	public Double getCategory2() {
		return category2;
	}

	public void setCategory2(Double category2) {
		this.category2 = category2;
	}

	public Double getCategory3() {
		return category3;
	}

	public void setCategory3(Double category3) {
		this.category3 = category3;
	}

	public Double getCategory4() {
		return category4;
	}

	public void setCategory4(Double category4) {
		this.category4 = category4;
	}

	public Double getCategory5() {
		return category5;
	}

	public void setCategory5(Double category5) {
		this.category5 = category5;
	}

	public Double getCategory6() {
		return category6;
	}

	public void setCategory6(Double category6) {
		this.category6 = category6;
	}

	public Double getCategory7() {
		return category7;
	}

	public void setCategory7(Double category7) {
		this.category7 = category7;
	}

	public Double getCategory8() {
		return category8;
	}

	public void setCategory8(Double category8) {
		this.category8 = category8;
	}

	public Double getCategory9() {
		return category9;
	}

	public void setCategory9(Double category9) {
		this.category9 = category9;
	}

	public Double getCategory10() {
		return category10;
	}

	public void setCategory10(Double category10) {
		this.category10 = category10;
	}

	public Double getTotalreusable() {
		return totalreusable;
	}

	public void setTotalreusable(Double totalreusable) {
		this.totalreusable = totalreusable;
	}

	public Double getTotalwaste() {
		return totalwaste;
	}

	public void setTotalwaste(Double totalwaste) {
		this.totalwaste = totalwaste;
	}
}
