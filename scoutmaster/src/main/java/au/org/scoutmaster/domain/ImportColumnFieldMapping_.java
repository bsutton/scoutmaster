package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-15T13:07:13.856+1000")
@StaticMetamodel(ImportColumnFieldMapping.class)
public class ImportColumnFieldMapping_ extends BaseEntity_ {
	public static volatile SingularAttribute<ImportColumnFieldMapping, ImportUserMapping> userMapping;
	public static volatile SingularAttribute<ImportColumnFieldMapping, String> csvColumnName;
	public static volatile SingularAttribute<ImportColumnFieldMapping, String> dbFieldName;
}
