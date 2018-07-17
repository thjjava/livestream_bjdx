package com.sttri.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/**
 * 黑名单
 */
@Entity
@Table(name = "system_alarm")
public class SystemAlarm implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private MediaServer server;
	private String memTotal;
	private String memUsed;
	private String cpuUsage;
	private String threshold;
	private Integer alarmLevel;
	private String addTime;
	
	public SystemAlarm() {
	}
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="ServerId")
	@NotFound(action=NotFoundAction.IGNORE)
	public MediaServer getServer() {
		return server;
	}

	public void setServer(MediaServer server) {
		this.server = server;
	}

	@Column(name = "MemTotal", length = 50)
	public String getMemTotal() {
		return memTotal;
	}

	public void setMemTotal(String memTotal) {
		this.memTotal = memTotal;
	}

	@Column(name = "MemUsed", length = 50)
	public String getMemUsed() {
		return memUsed;
	}

	public void setMemUsed(String memUsed) {
		this.memUsed = memUsed;
	}

	@Column(name = "CpuUsage", length = 50)
	public String getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	@Column(name = "Threshold", length = 50)
	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	@Column(name = "AlarmLevel")
	public Integer getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(Integer alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	@Column(name = "AddTime", length = 20)
	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

}