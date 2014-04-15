package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-15T13:07:13.864+1000")
@StaticMetamodel(ImportUserMapping.class)
public class ImportUserMapping_ extends BaseEntity_ {
	public static volatile SingularAttribute<ImportUserMapping, String> mappingName;
	public static volatile ListAttribute<ImportUserMapping, ImportColumnFieldMapping> columnFieldMappings;
}
