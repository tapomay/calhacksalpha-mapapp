package com.calhacksalpha.dawwwskigrowl;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap map) {
		// Add a marker in Sydney, Australia, and move the camera.
		LatLng sydney = new LatLng(-34, 151);
		LatLng ucb = new LatLng(37, -122);
		map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
		map.addMarker(new MarkerOptions().position(ucb).title("Marker in Cal"));
		map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
		
	}
}
