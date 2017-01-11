package com.tum.poll.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "Gasindex")
public class Gasindex {

	public Gasindex(){
		
	}
	
	public Gasindex(Long id, Double latitude, Double longitude, String gasname1,
			Double gasindex1, String gasname2, Double gasindex2, Geolocation geolocation){
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.gasname1 = gasname1;
		this.gasindex1 = gasindex1;
		this.gasname2 = gasname2;
		this.gasindex2 = gasindex2;
		this.location = geolocation;
	}
	@Id
	private Long id;
	
	@Column
	private Double latitude;
	
	@Column
	private Double longitude;
	
	@Column
	private String gasname1;
	
	@Column
	private Double gasindex1;
	
	@Column
	private String gasname2;
	
	@Column
	private Double gasindex2;
	
	@ManyToOne
	@JoinColumn(name = "location_fk", referencedColumnName = "ID", nullable = false)
	private Geolocation location;
	
	

	public Long getId(){
		return this.id;
	}
	
	public Double getLatitude(){
		return this.latitude;
	}
	
	public Double getLongitude(){
		return this.longitude;
	}
	
	public String getGasname1(){
		return this.gasname1;
	}
	
	public Double getGasindex1(){
		return this.gasindex1;
	}
	
	public String getGasname2(){
		return this.gasname2;
	}
	
	public Double getGasindex2(){
		return this.gasindex2;
	}
	
	public Geolocation getLocation(){
		return this.location;
	}
	
	public void setId(final Long id){
		this.id = id;
	}
	
	public void setLatitude(final Double latitude){
		this.latitude = latitude;
	}
	
	public void setLongitude(final Double longitude){
		this.longitude = longitude;
	}
	
	public void setLocation(final Geolocation location){
		this.location = location;
	}
	
	public void setGasname1(final String gasname1){
		this.gasname1 = gasname1;
	}
	
	public void setGasindex1(final Double gasindex1){
		this.gasindex1 = gasindex1;
	}
	
	public void setGasname2(final String gasname2){
		this.gasname2 = gasname2;
	}
	
	public void setGasindex2(final Double gasindex2){
		this.gasindex2 = gasindex2;
	}
}
