package com.calhacksalpha.dawwwskigrowl;

import java.io.IOException;
import java.lang.ref.WeakReference;

import com.calhacksalpha.dawwwskigrowl.data.LocationEntities;
import com.calhacksalpha.dawwwskigrowl.data.User;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.common.PositioningManager.LocationMethod;
import com.here.android.mpa.common.PositioningManager.LocationStatus;
import com.here.android.mpa.common.PositioningManager.OnPositionChangedListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	// map embedded in the map fragment
	private Map map = null;

	// map fragment embedded in this activity
	private MapFragment mapFragment = null;

	private Boolean paused = true;

	private LocationEntities locationEntities;
	private User mock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Search for the map fragment to finish setup by calling init().
		mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
		mapFragment.init(new OnEngineInitListener() {
			@Override
			public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
				if (error == OnEngineInitListener.Error.NONE) {
					// retrieve a reference of the map from the map fragment
					map = mapFragment.getMap();
					locationEntities = new LocationEntities(map);
					map.getPositionIndicator().setVisible(true);

					// Set the map center to the Vancouver region (no animation)
					GeoPosition lastKnownPosition = PositioningManager.getInstance().getLastKnownPosition();

					map.setCenter(lastKnownPosition.getCoordinate(), Map.Animation.NONE);
					// Set the zoom level to the average between min and max
//					map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
					map.setZoomLevel(map.getMaxZoomLevel() * 0.8);

					// Register positioning listener
					PositioningManager.getInstance()
							.addListener(new WeakReference<OnPositionChangedListener>(positionListener));

					try {
						mock = locationEntities.mock(lastKnownPosition.getCoordinate().getLatitude(),
								lastKnownPosition.getCoordinate().getLongitude());
						map.addMapObject(mock.myMapMarker);
					} catch (IOException e) {
						Log.e("", "", e);
					}

				} else {
					System.out.println("ERROR: Cannot initialize Map Fragment");
					// Log.e("R2D2", "", error.toString());
				}
			}
		});
	}

	// Define positioning listener
	private OnPositionChangedListener positionListener = new OnPositionChangedListener() {

		public void onPositionUpdated(LocationMethod method, GeoPosition position, boolean isMapMatched) {
			// set the center only when the app is in the foreground
			// to reduce CPU consumption
			if (!paused) {
				map.setCenter(position.getCoordinate(), Map.Animation.NONE);

				GeoCoordinate coordinate = position.getCoordinate();
				coordinate.setLatitude(coordinate.getLatitude() + 0.1);
				coordinate.setLongitude(coordinate.getLongitude() + 0.1);
				locationEntities.updateLocation(mock.id, coordinate);
				map.addMapObject(mock.myMapMarker);
			}
		}

		public void onPositionFixChanged(LocationMethod method, LocationStatus status) {
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void forward1(View v) {
		Intent intent = new Intent(this, MapsActivity.class);
		startActivity(intent);
	}

	public void forward2(View v) {
		Intent intent = new Intent(this, MyLocationDemoActivity.class);
		startActivity(intent);
	}

	// Resume positioning listener on wake up
	public void onResume() {
		super.onResume();
		paused = false;
		if (null != map)
			PositioningManager.getInstance().start(PositioningManager.LocationMethod.GPS_NETWORK);
	}

	// To pause positioning listener
	public void onPause() {
		if (null != map)
			PositioningManager.getInstance().stop();
		super.onPause();
		paused = true;
	}

	// To remove the positioning listener
	public void onDestroy() {
		// Cleanup
		PositioningManager.getInstance().removeListener(positionListener);
		map = null;
		super.onDestroy();
	}
}
