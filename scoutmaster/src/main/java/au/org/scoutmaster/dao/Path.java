package au.org.scoutmaster.dao;

import javax.persistence.metamodel.SingularAttribute;

import au.org.scoutmaster.domain.BaseEntity;


public class Path
{
	SingularAttribute<? extends BaseEntity, ? extends BaseEntity > lhsEntity;
	SingularAttribute<? extends BaseEntity, ? extends Object> rhsField;
	

	public   Path(SingularAttribute<? extends BaseEntity, ? extends BaseEntity > lhsEntity, SingularAttribute<? extends BaseEntity, ? extends Object> rhsField)
	{
		this.lhsEntity = lhsEntity;
		this.rhsField = rhsField;
	}
	
	public String toString()
	{
		return this.lhsEntity.getName() + "." + this.rhsField.getName();
	}

}

