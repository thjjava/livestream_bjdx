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
 * 用户点赞
 */
@Entity
@Table(name = "dev_comment")
public class DevComment implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private TblDev dev;
	private TblUser user;
	private String clientIP;
	private Integer isLegal;
	private String content;
	private String commentTime;
	
	public DevComment() {
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
	@JoinColumn(name="DevId")
	@NotFound(action=NotFoundAction.IGNORE)
	public TblDev getDev() {
		return dev;
	}

	public void setDev(TblDev dev) {
		this.dev = dev;
	}
	
	@ManyToOne
	@JoinColumn(name="UserId")
	@NotFound(action=NotFoundAction.IGNORE)
	public TblUser getUser() {
		return user;
	}

	public void setUser(TblUser user) {
		this.user = user;
	}
	
	@Column(name = "ClientIP", length = 50)
	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	
	@Column(name = "IsLegal")
	public Integer getIsLegal() {
		return this.isLegal;
	}

	public void setIsLegal(Integer isLegal) {
		this.isLegal = isLegal;
	}
	
	@Column(name = "Content", length = 4000)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "CommentTime", length = 20)
	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

}