package com.calhacksalpha.dawwwskigrowl;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
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
					// Set the map center to the Vancouver region (no animation)
					map.setCenter(new GeoCoordinate(49.196261, -123.004773, 0.0), Map.Animation.NONE);
					// Set the zoom level to the average between min and max
					map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
				} else {
					System.out.println("ERROR: Cannot initialize Map Fragment");
//					Log.e("R2D2", "", error.toString());
				}
			}
		});
	}

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
}