package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public class Timestampable //extends Model
{

	@Id
	@GeneratedValue
	public Long id;

	@Column(name = "created_at")
	public Date createdAt;

	@Column(name = "updated_at")
	public Date updatedAt;

//	@Version
//	public int version;
//
//	@Override
//	public void save()
//	{
//		createdAt();
//		super.save();
//	}
//
//	@Override
//	public void update()
//	{
//		updatedAt();
//		super.update();
//	}

	@PrePersist
	void createdAt()
	{
		java.util.Date today = new java.util.Date();
		this.createdAt = this.updatedAt = new java.sql.Date(today.getTime());
	}

	@PreUpdate
	void updatedAt()
	{
		java.util.Date today = new java.util.Date();
		this.updatedAt = new java.sql.Date(today.getTime());
	}
}