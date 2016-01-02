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
		// eagerConnectors.add(PasswordFieldConnector.class.getName());
		// eagerConnectors.add(VerticalLayoutConnector.class.getName());
		// eagerConnectors.add(HorizontalLayoutConnector.class.getName());
		// eagerConnectors.add(ButtonConnector.class.getName());
		// eagerConnectors.add(UIConnector.class.getName());
		// eagerConnectors.add(CssLayoutConnector.class.getName());
		// eagerConnectors.add(TextFieldConnector.class.getName());
		// eagerConnectors.add(PanelConnector.class.getName());
		// eagerConnectors.add(LabelConnector.class.getName());
		// eagerConnectors.add(WindowConnector.class.getName());
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
