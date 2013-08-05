package au.org.scoutmaster.util;

import java.util.Locale;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractSelect;

public class SingleSelectConverter<T> implements Converter<Object, T>
{
	private static final long serialVersionUID = 1L;
	
	private final AbstractSelect select;

	public SingleSelectConverter(AbstractSelect select)
	{
		this.select = select;
	}

	@SuppressWarnings("unchecked")
	private EntityContainer<T> getContainer()
	{
		return (EntityContainer<T>) select.getContainerDataSource();
	}

	@Override
	public T convertToModel(Object value, Class<? extends T> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value != select.getNullSelectionItemId())
		{
			return getContainer().getEntityProvider().getEntity(getContainer(), value);
		}
		else
		{
			return null;
		}
	}

	@Override
	public Object convertToPresentation(T value, Class<? extends Object> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value != null)
		{
			return getContainer().getEntityProvider().getIdentifier(value);
		}
		return select.getNullSelectionItemId();
	}

	public Class<T> getModelType()
	{
		return getContainer().getEntityClass();
	}

	public Class<Object> getPresentationType()
	{
		return Object.class;
	}

}
