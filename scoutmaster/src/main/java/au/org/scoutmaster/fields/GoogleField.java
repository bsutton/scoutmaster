package au.org.scoutmaster.fields;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class GoogleField extends VerticalLayout
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// private GoogleMap googleMap;
	@SuppressWarnings("unused")
	private final String apiKey = "";

	public GoogleField()
	{
		setSizeFull();

		// googleMap = new GoogleMap(new LatLon(60.440963, 22.25122), 10.0,
		// apiKey);
		// googleMap.setSizeFull();
		// googleMap.addMarker("DRAGGABLE: Paavo Nurmi Stadion", new
		// LatLon(60.442423, 22.26044), true);
		// googleMap.addMarker("DRAGGABLE: Kakolan vankila", new
		// LatLon(60.44291, 22.242415), true);
		// googleMap.addMarker("NOT DRAGGABLE: Iso-Heikkilä", new
		// LatLon(60.450403, 22.230399), false);
		// googleMap.setMinZoom(4.0);
		// googleMap.setMaxZoom(16.0);
		// this.addComponent(googleMap);
		// this.setExpandRatio(googleMap, 1.0f);

		final Panel console = new Panel();
		console.setHeight("100px");
		final CssLayout consoleLayout = new CssLayout();
		console.setContent(consoleLayout);
		this.addComponent(console);

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setHeight("26px");
		this.addComponent(buttonLayout);

		// googleMap.addMarkerClickListener(new MarkerClickListener()
		// {
		// @Override
		// public void markerClicked(GoogleMapMarker clickedMarker)
		// {
		// Label consoleEntry = new Label("Marker \"" +
		// clickedMarker.getCaption() + "\" at ("
		// + clickedMarker.getPosition().getLat() + ", " +
		// clickedMarker.getPosition().getLon()
		// + ") clicked.");
		// consoleLayout.addComponent(consoleEntry, 0);
		// }
		// });
		//
		// googleMap.addMapMoveListener(new MapMoveListener()
		// {
		// @Override
		// public void mapMoved(double zoomLevel, LatLon center, LatLon
		// boundsNE, LatLon boundsSW)
		// {
		// Label consoleEntry = new Label("Map moved to (" + center.getLat() +
		// ", " + center.getLon() + "), zoom "
		// + zoomLevel + ", boundsNE: (" + boundsNE.getLat() + ", " +
		// boundsNE.getLon() + "), boundsSW: ("
		// + boundsSW.getLat() + ", " + boundsSW.getLon() + ")");
		// consoleLayout.addComponent(consoleEntry, 0);
		// }
		// });
		//
		// googleMap.addMarkerDragListener(new MarkerDragListener()
		// {
		// @Override
		// public void markerDragged(GoogleMapMarker draggedMarker, LatLon
		// newPosition)
		// {
		// Label consoleEntry = new Label("Marker \"" +
		// draggedMarker.getCaption() + "\" dragged to ("
		// + newPosition.getLat() + ", " + newPosition.getLon() + ")");
		// consoleLayout.addComponent(consoleEntry, 0);
		//
		// }
		// });
		//
		// Button moveCenterButton = new
		// Button("Move over Luonnonmaa (60.447737, 21.991668), zoom 12",
		// new Button.ClickListener()
		// {
		// /**
		// *
		// */
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void buttonClick(ClickEvent event)
		// {
		// googleMap.setCenter(new LatLon(60.447737, 21.991668));
		// googleMap.setZoom(12.0);
		// }
		// });
		// buttonLayout.addComponent(moveCenterButton);
		//
		// Button limitCenterButton = new
		// Button("Limit center between (60.619324, 22.712753), (60.373484, 21.945083)",
		// new Button.ClickListener()
		// {
		// /**
		// *
		// */
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void buttonClick(ClickEvent event)
		// {
		// googleMap.setCenterBoundLimits(new LatLon(60.619324, 22.712753), new
		// LatLon(60.373484,
		// 21.945083));
		// event.getButton().setEnabled(false);
		// }
		// });
		// buttonLayout.addComponent(limitCenterButton);
		//
		// Button limitVisibleAreaButton = new Button(
		// "Limit visible area between (60.494439, 22.397835), (60.373484, 21.945083)",
		// new Button.ClickListener()
		// {
		// /**
		// *
		// */
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void buttonClick(ClickEvent event)
		// {
		// googleMap.setVisibleAreaBoundLimits(new LatLon(60.494439, 22.397835),
		// new LatLon(60.420632,
		// 22.138626));
		// event.getButton().setEnabled(false);
		// }
		// });
		// buttonLayout.addComponent(limitVisibleAreaButton);
		//
		// Button addPolyOverlayButton = new
		// Button("Add overlay over Luonnonmaa", new Button.ClickListener()
		// {
		// /**
		// *
		// */
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void buttonClick(ClickEvent event)
		// {
		// ArrayList<LatLon> points = new ArrayList<LatLon>();
		// points.add(new LatLon(60.484715, 21.923706));
		// points.add(new LatLon(60.446636, 21.941387));
		// points.add(new LatLon(60.422496, 21.99546));
		// points.add(new LatLon(60.427326, 22.06464));
		// points.add(new LatLon(60.446467, 22.064297));
		//
		// GoogleMapPolygon overlay = new GoogleMapPolygon(points, "#ae1f1f",
		// 0.8, "#194915", 0.5, 3);
		// googleMap.addPolygonOverlay(overlay);
		// event.getButton().setEnabled(false);
		// }
		// });
		// buttonLayout.addComponent(addPolyOverlayButton);
		//
		// Button addPolyLineButton = new
		// Button("Draw line from Turku to Raisio", new Button.ClickListener()
		// {
		// /**
		// *
		// */
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void buttonClick(ClickEvent event)
		// {
		// ArrayList<LatLon> points = new ArrayList<LatLon>();
		// points.add(new LatLon(60.448118, 22.253738));
		// points.add(new LatLon(60.455144, 22.24198));
		// points.add(new LatLon(60.460222, 22.211939));
		// points.add(new LatLon(60.488224, 22.174602));
		// points.add(new LatLon(60.486025, 22.169195));
		//
		// GoogleMapPolyline overlay = new GoogleMapPolyline(points, "#d31717",
		// 0.8, 10);
		// googleMap.addPolyline(overlay);
		// event.getButton().setEnabled(false);
		// }
		// });
		// buttonLayout.addComponent(addPolyLineButton);
		// Button changeToTerrainButton = new Button("Change to terrain map",
		// new Button.ClickListener()
		// {
		// private static final long serialVersionUID = -5716197767192690718L;
		//
		// @Override
		// public void buttonClick(ClickEvent event)
		// {
		// googleMap.setMapType(GoogleMap.MapType.Terrain);
		// event.getButton().setEnabled(false);
		// }
		// });
		// buttonLayout.addComponent(changeToTerrainButton);
		//
		// Button changeControls = new Button("Remove street view control", new
		// Button.ClickListener()
		// {
		// /**
		// *
		// */
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void buttonClick(ClickEvent event)
		// {
		// googleMap.removeControl(GoogleMapControl.StreetView);
		// event.getButton().setEnabled(false);
		// }
		// });
		// buttonLayout.addComponent(changeControls);

	}

}
