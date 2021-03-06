package com.calhacksalpha.dawwwskigrowl.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.here.android.mpa.common.GeoCoordinate;

public class LocationEntities {

	private Map<String, LocationAware> entities = new HashMap<String, LocationAware>();
	com.here.android.mpa.mapping.Map map;
	
	public LocationEntities(com.here.android.mpa.mapping.Map map) {
		super();
		this.map = map;
	}

	public void addEntity(User d) {
		entities.put(d.id, d);
		map.addMapObject(d.myMapMarker);
	}

	public void removeEntity(User d) {
		entities.remove(d);
		map.removeMapObject(d.myMapMarker);
	}

	public void updateLocation(String id, GeoCoordinate coordinate) {
		LocationAware locationAware = entities.get(id);
		locationAware.updateLocation(coordinate);
	}

	public User mock(double latitude, double longitude) throws IOException {
		User ret = new User("someid", new GeoCoordinate(latitude+0.1, longitude+0.1));
		addEntity(ret);
		return ret;
	}
}
