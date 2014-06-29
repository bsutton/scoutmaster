package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-06-29T20:00:48.654+1000")
@StaticMetamodel(ImportUserMapping.class)
public class ImportUserMapping_ extends BaseEntity_ {
	public static volatile SingularAttribute<ImportUserMapping, String> mappingName;
	public static volatile ListAttribute<ImportUserMapping, ImportColumnFieldMapping> columnFieldMappings;
}
