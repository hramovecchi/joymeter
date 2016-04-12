package com.joymeter.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Simple POJO that represents a historical level of joy for a given date.
 * 
 */
@JsonIgnoreProperties
@Entity
@Table(name = "LEVELOFJOY", uniqueConstraints={@UniqueConstraint(columnNames={"user_id", "milliseconds"})})
@NamedQueries( { @NamedQuery(name = "LevelOfJoy.findAllByUser", query = "SELECT a FROM LevelOfJoy a WHERE a.user.id=:userID"),
				 @NamedQuery(name = "LevelOfJoy.findLastEntriesByUser", query = "SELECT a FROM LevelOfJoy a WHERE a.user.id=:userID AND a.milliseconds >= :millis AND a.milliseconds <= :today"),
				 @NamedQuery(name = "LevelOfJoy.findByDateUser", query = "SELECT a FROM LevelOfJoy a WHERE a.user.id=:userID AND a.milliseconds = :millis"),
				 @NamedQuery(name = "LevelOfJoy.findLastByUser", query = "SELECT a FROM LevelOfJoy a WHERE a.user.id = :userID ORDER BY a.milliseconds DESC")})
public class LevelOfJoy implements Serializable{
	
	private static final long serialVersionUID = -3597156974325366320L;
	private static String pattern = "dd-MM-yyyy HH:mm";
	
	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	//millis of the date
	private long milliseconds;
	
	private Double level;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Double getLevel() {
		return level;
	}
	
	public void setLevel(Double level) {
		this.level = level;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public long getMilliseconds() {
		return milliseconds;
	}
	
	@JsonIgnore
	public DateTime getDate() {
		return new DateTime(milliseconds).withTimeAtStartOfDay();
	}
	
	public String getDateString() {
		return new DateTime(milliseconds).withTimeAtStartOfDay().toString(DateTimeFormat.forPattern(pattern));
	}

	public void setMilliseconds(long milliseconds) {
		this.milliseconds = milliseconds;
	}
	
	public void setDate(DateTime date) {
		this.milliseconds = date.withTimeAtStartOfDay().getMillis();
	}
}
