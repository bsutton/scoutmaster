package au.org.scoutmaster.views;

import au.org.scoutmaster.domain.BaseEntity;


public interface Selected<T extends BaseEntity>
{

	T getCurrent();

}
