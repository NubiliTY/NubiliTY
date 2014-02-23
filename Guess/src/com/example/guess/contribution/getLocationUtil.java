package com.example.guess.contribution;

import static android.content.Context.LOCATION_SERVICE;
import static android.location.Criteria.ACCURACY_FINE;
import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.IOException;
import java.util.List;
/*
 * The helper class used to retrieve the user location
 */
public class getLocationUtil {


    // compare the location retrival time to get the most current location
    private static Location getMostCurrent(final Location location1, final Location location2) {
        if (location1 == null) return location2;
        if (location2 == null) return location1;
 
        return (location2.getTime() > location1.getTime()) ? location2 : location1;
    }

    // Return the most current location of the user
    public static Location getUserLocation(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(ACCURACY_FINE);
        Location location = null;
        
        if ( null != locationManager.getBestProvider(criteria, true) ) {
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        }
        location = getMostCurrent(location, locationManager.getLastKnownLocation(GPS_PROVIDER));
        location = getMostCurrent(location, locationManager.getLastKnownLocation(NETWORK_PROVIDER));
        
        return location;
    }

    // Translate the (long, atti) location to human readable address
    public static Address translateToAddress(final Context context, Location location) {
        if (null == location) return null;
        
        Geocoder geocoder = new Geocoder(context);
        List<Address> address;
        try {
            address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch(IOException e) {
            Log.e(null, "cannot translate location to address");
            return null;
        }
        
        if (address != null && address.size() != 0) {
            return address.get(0);
        }
        return null;
    }
}