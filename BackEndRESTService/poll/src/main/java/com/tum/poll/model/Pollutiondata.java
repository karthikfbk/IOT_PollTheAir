package com.tum.poll.model;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity(name = "Pollutiondata")
public class Pollutiondata {

	public Pollutiondata(){
		
	}
	
	public Pollutiondata(Long id, String name, Double latitude, Double longitude, Long pollution,
			Calendar datetime){
		super();
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.pollution = pollution;
		this.datetime = datetime;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private Double latitude;
	
	@Column
	private Double longitude;
	
	@Temporal(TemporalType.DATE)
	private Calendar datetime;
	
	@Column
	private Long pollution;
	
	public Long getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Double getLatitude(){
		return this.latitude;
	}
	
	public Double getLongitude(){
		return this.longitude;
	}
	
	public Calendar getDatetime() {
		return this.datetime;
	}
	
	public Long getPollution(){
		return this.pollution;
	}
	public void setId(final Long id){
		this.id = id;
	}
	
	public void setName(final String name){
		this.name = name;
	}
	
	public void setLatitude(final Double latitude){
		this.latitude = latitude;
	}
	
	public void setLongitude(final Double longitude){
		this.longitude = longitude;
	}
	
	public void setDatetime(Calendar datetime) {
		this.datetime = datetime;
	}
	
	public void setPollution(final Long pollution){
		this.pollution = pollution;
	}
	
}
