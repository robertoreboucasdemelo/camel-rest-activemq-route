package com.moduretick.simplerestmq.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Entity
@Table(name = "name_address")
@NamedQuery(name="fetchAllRows", query="Select x from NameAddress x")
@CsvRecord(separator = ";")
public class NameAddress implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@DataField(pos = 1, columnName = "name")
	private String name;
	
	@Column(name = "house_number")
	@DataField(pos = 2, columnName = "house_number")
	private String houseNumber;
	
	@DataField(pos = 3, columnName = "city")
	private String city;
	
	@DataField(pos = 4, columnName = "province")
	private String province;
	
	@Column(name = "postal_code")
	@DataField(pos = 5, columnName = "postal_code")
	private String postalCode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "NameAddress [id=" + id + ", name=" + name + ", houseNumber=" + houseNumber + ", city=" + city
				+ ", province=" + province + ", postalCode=" + postalCode + "]";
	}
	
	
	
	
	
}
