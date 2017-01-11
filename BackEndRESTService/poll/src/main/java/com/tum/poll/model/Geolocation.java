package com.tum.poll.model;

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



@Entity(name = "Geolocation")
public class Geolocation {
	
	public Geolocation(){
		
	}
	
	public Geolocation(Long id, String name, Double latitude, Double longitude,
			List<Gasindex> gasindexes){
		super();
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.gasindexes = gasindexes;
	}
	
	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private Double latitude;
	
	@Column
	private Double longitude;
	
	@OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
	private List<Gasindex> gasindexes;

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
	
	public List<Gasindex> getGasindexes(){
		return this.gasindexes;
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
	
	public void setGasindexes(final List<Gasindex> gasindexes){
		this.gasindexes = gasindexes;
	}

}
