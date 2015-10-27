package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.domain.iColor;
import au.com.vaadinutils.domain.iColorFactory;
import au.org.scoutmaster.domain.Color;

public class ColorDao extends JpaBaseDao<Color, Long> implements Dao<Color, Long>
{

	public ColorDao()
	{
		// inherit the default per request em.
	}


	@Override
	public JPAContainer<Color> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

	static public class ColorFactory implements iColorFactory
	{

		@Override
		public iColor createColor(final com.vaadin.shared.ui.colorpicker.Color color)
		{
			return new au.org.scoutmaster.domain.Color(color);
		}

	}

}
