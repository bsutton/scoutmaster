package au.org.scoutmaster;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.vaadin.server.widgetsetutils.ConnectorBundleLoaderFactory;
import com.vaadin.shared.ui.Connect.LoadStyle;

/**
 * This class is referenced in AppWidgetSet.gwt.xml and provides vaadin
 * connector optimisations.
 *
 * @author bsutton
 *
 */
public class OptimizedConnectorBundleLoaderFactory extends ConnectorBundleLoaderFactory
{
	private final Set<String> eagerConnectors = new HashSet<String>();
	{
		this.eagerConnectors.add(com.vaadin.client.ui.ui.UIConnector.class.getName());
	}

	@Override
	protected LoadStyle getLoadStyle(final JClassType connectorType)
	{
		if (this.eagerConnectors.contains(connectorType.getQualifiedBinaryName()))
		{
			return LoadStyle.EAGER;
		}
		else
		{
			// Loads all other connectors immediately after the initial view has
			// been rendered
			return LoadStyle.DEFERRED;
		}
	}
}
