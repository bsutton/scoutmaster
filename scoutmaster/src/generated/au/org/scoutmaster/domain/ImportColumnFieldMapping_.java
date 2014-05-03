package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(ImportColumnFieldMapping.class)
public class ImportColumnFieldMapping_ extends BaseEntity_
{

	public static volatile SingularAttribute<ImportColumnFieldMapping, String> csvColumnName;
	public static volatile SingularAttribute<ImportColumnFieldMapping, String> dbFieldName;
	public static volatile SingularAttribute<ImportColumnFieldMapping, ImportUserMapping> userMapping;

}