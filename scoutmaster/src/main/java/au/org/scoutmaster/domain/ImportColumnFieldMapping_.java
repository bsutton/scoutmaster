package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.273+1000")
@StaticMetamodel(ImportColumnFieldMapping.class)
public class ImportColumnFieldMapping_ extends BaseEntity_ {
	public static volatile SingularAttribute<ImportColumnFieldMapping, ImportUserMapping> userMapping;
	public static volatile SingularAttribute<ImportColumnFieldMapping, String> csvColumnName;
	public static volatile SingularAttribute<ImportColumnFieldMapping, String> dbFieldName;
}
