package au.org.scoutmaster.domain.access;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.org.scoutmaster.domain.BaseEntity;

@Entity
@Table(name = "tenant")
public class Tenant extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Transient
	private static final Logger logger = LogManager.getLogger(Tenant.class);

	@Override
	public String getName()
	{
		return "Tenant " + this.getId();
	}
}
