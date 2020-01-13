package ir.androidexception.andexmaphelper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class AndExMapHelper {
    /* Constants */
    public static final float DEFAULT_ZOOM = 16f;
    public static final Integer ICON_RESOURCE_ID_NOT_EXIST = -1;

    /* Map */
    private static GoogleMap mMap;
    private static GoogleMap.OnMarkerClickListener mOnMarkerClickListener = null;
    private static GoogleMap.OnMapClickListener mOnMapClickListener = null;

    /* Current Location */
    private static Location mCurrentLocation;
    private static Marker mCurrentMarker;
    private static Integer mCurrentMarkerIconResourceId;

    /* Selected Marker */
    private static Location mSelectedLocation = null;
    private static Marker mSelectedMarker = null;

    /* Markers Group */
    private static ArrayList<MarkerStatus> mStatuses = new ArrayList<>();
    private static HashMap<Location,Marker> mMarkers = new HashMap<>();
    private static HashMap<Location,String> mMarkersTitles = new HashMap<>();
    private static HashMap<Location,String> mMarkersSnippets = new HashMap<>();
    private static HashMap<Location,MarkerStatus> mMarkerStatuses = new HashMap<>();

    /* Shape */
    private static ArrayList<CircleOptions> mCircles = new ArrayList<>();
    private static ArrayList<PolygonOptions> mPolygons = new ArrayList<>();



    /* Getters & Setters */
    public static Location getCurrentLocation(){
        return mCurrentLocation;
    }
    public static Marker getMarker(Location location){
        for (Map.Entry<Location, Marker> entry : mMarkers.entrySet()) {
            Location key = entry.getKey();
            if(isLocationsEquals(location,key)) return entry.getValue();
        }
        return null;
    }
    public static void setMarker(Location location, Marker marker){
        for (Map.Entry<Location, Marker> entry : mMarkers.entrySet()) {
            Location key = entry.getKey();
            if(isLocationsEquals(location,key)){
                mMarkers.remove(key);
                mMarkers.put(location,marker);
                return;
            }
        }
        mMarkers.put(location,marker);
    }
    public static String getMarkerTitle(Location location){
        for (Map.Entry<Location, String> entry : mMarkersTitles.entrySet()) {
            Location key = entry.getKey();
            if(isLocationsEquals(location,key)) return entry.getValue();
        }
        return "";
    }
    public static void setMarkerTitle(Location location, String title){
        for (Map.Entry<Location, String> entry : mMarkersTitles.entrySet()) {
            Location key = entry.getKey();
            if(isLocationsEquals(location,key)){
                mMarkersTitles.remove(key);
                mMarkersTitles.put(location,title);
                return;
            }
        }
        mMarkersTitles.put(location,title);
    }
    public static String getMarkerSnippet(Location location){
        for (Map.Entry<Location, String> entry : mMarkersSnippets.entrySet()) {
            Location key = entry.getKey();
            if(isLocationsEquals(location,key)) return entry.getValue();
        }
        return "";
    }
    public static void setMarkerSnippet(Location location, String snippet){
        for (Map.Entry<Location, String> entry : mMarkersSnippets.entrySet()) {
            Location key = entry.getKey();
            if(isLocationsEquals(location,key)){
                mMarkersSnippets.remove(key);
                mMarkersSnippets.put(location,snippet);
                return;
            }
        }
        mMarkersSnippets.put(location,snippet);
    }
    public static MarkerStatus getMarkerStatus(Location location){
        for (Map.Entry<Location, MarkerStatus> entry : mMarkerStatuses.entrySet()) {
            Location key = entry.getKey();
            if(isLocationsEquals(location,key)) return entry.getValue();
        }
        return null;
    }
    public static void setMarkerStatus(Location location, String status){
        for (Map.Entry<Location, MarkerStatus> entry : mMarkerStatuses.entrySet()) {
            Location key = entry.getKey();
            if(isLocationsEquals(location,key)){
                for(MarkerStatus ms : mStatuses){
                    if(ms.getName().equals(status)){
                        mMarkerStatuses.remove(key);
                        mMarkerStatuses.put(location,ms);
                    }
                }
                return;
            }
        }
        for(MarkerStatus ms : mStatuses){
            if(ms.getName().equals(status)){
                mMarkerStatuses.put(location,ms);
            }
        }

    }
    public static void setStatuses(ArrayList<MarkerStatus> statuses){
        mStatuses = statuses;
    }
    public static Integer getStatusIconResource(String status){
        if(status==null)
            return ICON_RESOURCE_ID_NOT_EXIST;
        for(MarkerStatus ms : mStatuses) if(ms.getName().equals(status)) return ms.getIconResourceId();
        return ICON_RESOURCE_ID_NOT_EXIST;
    }
    public static Location getSelectedLocation(){
        return mSelectedLocation;
    }
    public static Marker getSelectedMarker(){
        return mSelectedMarker;
    }
    public static void setStatuses(HashMap<String,Integer> statuses){
        ArrayList<MarkerStatus> ms = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : statuses.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            ms.add(new MarkerStatus(key,value));
        }
        mStatuses = ms;
    }
    private static void addMarkerToList(Location location, Marker marker, String title, String snippet, String status){
        removeMarker(location);
        setMarker(location,marker);
        setMarkerTitle(location,title);
        setMarkerSnippet(location,snippet);
        setMarkerStatus(location,status);
    }
    public static void setOnMarkerClickListener(final GoogleMap.OnMarkerClickListener onMarkerClickListener){
        if(onMarkerClickListener!=null)
            mOnMarkerClickListener = onMarkerClickListener;
    }
    public static void setOnMapClickListener(GoogleMap.OnMapClickListener onMapClickListener){
        if(onMapClickListener!=null)
            mOnMapClickListener = onMapClickListener;
    }
    public static void setInfoWindowAdapter(final GoogleMap.InfoWindowAdapter infoWindowAdapter){
        if(mMap!=null){
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    if(infoWindowAdapter!=null)
                        infoWindowAdapter.getInfoWindow(marker);
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    if(infoWindowAdapter!=null)
                        return infoWindowAdapter.getInfoContents(marker);
                    else
                        return null;
                }
            });
        }
    }



    public static void Init(Context context, GoogleMap map, final LocationListener locationListener, long minTime, float minDistance){
        mMap = map;
        mCurrentLocation = setupLocationManager(context, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                changeCurrentLocation(location);
                if(locationListener!=null)
                    locationListener.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                if(locationListener!=null)
                    locationListener.onStatusChanged(provider,status,extras);
            }

            @Override
            public void onProviderEnabled(String provider) {
                if(locationListener!=null)
                    locationListener.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                if(locationListener!=null)
                    locationListener.onProviderDisabled(provider);
            }
        }, minTime, minDistance);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                select(locationInstance(marker.getPosition().latitude, marker.getPosition().longitude));
                if(mOnMarkerClickListener!=null)
                    mOnMarkerClickListener.onMarkerClick(marker);
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                deselect();
                if(mOnMapClickListener!=null)
                    mOnMapClickListener.onMapClick(latLng);
            }
        });
    }

    public static void showCurrentLocationOnMap(Context context, String title, String snippet, Integer iconResId){
        if(mCurrentLocation==null)
            return;
        mCurrentMarkerIconResourceId = iconResId;
        BitmapDescriptor icon = (iconResId==null || iconResId.equals(ICON_RESOURCE_ID_NOT_EXIST)) ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED): BitmapDescriptorFactory.fromBitmap(getBitmap(context,iconResId));
        mCurrentMarker = mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()))
                        .title(title)
                        .snippet(snippet)
                        .icon(icon));
        refreshMap(context);
    }

    public static void showInfoWindow(Location location){
        Marker tmpMarker = getMarker(location);
        if(tmpMarker!=null)
            tmpMarker.showInfoWindow();
    }

    public static void changeCurrentLocation(Location location){
        if(location!=null){
            mCurrentLocation.setLatitude(location.getLatitude());
            mCurrentLocation.setLongitude(location.getLongitude());
        }
    }

    public static void animateCamera(Location location, float zoom){
        if(mMap!=null && location!=null)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
    }

    public static void animateCamera(LatLng latLng, float zoom){
        if(mMap!=null && latLng!=null)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private static Location setupLocationManager(Context context, LocationListener locationListener, long minTime, float minDistance) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);
                GPSTracker gps = new GPSTracker(context);
                Location currentLocation = gps.getLocation();
                while(currentLocation==null) currentLocation = gps.getLocation();
                return currentLocation;
            }
        }
        return null;
    }

    public static boolean isLocationsEquals(Location location1, Location location2){
        if(location1==null || location2==null)
            return false;
        return (location1.getLatitude()==location2.getLatitude() && location1.getLongitude()==location2.getLongitude());
    }

    public static boolean isMarkerExist(Location location){
        for (Map.Entry<Location, Marker> entry : mMarkers.entrySet()) {
            Location key = entry.getKey();
            if(isLocationsEquals(location,key))
                return true;
        }
        return false;
    }

    private static Bitmap getBitmap(Context context, int drawableRes) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),drawableRes,null);
        Canvas canvas = new Canvas();
        Bitmap bitmap = null;
        if (drawable != null) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
        }

        return bitmap;
    }

    public static Location locationInstance(double latitude, double longitude){
        Location location = new Location("location");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    private static boolean isInStatuses(String status){
        for(MarkerStatus ms : mStatuses){
            if(ms.getName().equals(status))
                return true;
        }
        return false;
    }

    // this function remove marker only from markers list(with icon,title,snippet,status), not from map
    private static void removeMarker(Location location){
        if(isMarkerExist(location)) {
            for (Iterator<Map.Entry<Location, Marker>> it = mMarkers.entrySet().iterator(); it.hasNext();) {
                Location key = it.next().getKey();
                if (isLocationsEquals(location,key)) {
                    it.remove();
                }
            }

            for (Iterator<Map.Entry<Location, String>> it = mMarkersTitles.entrySet().iterator(); it.hasNext();) {
                Location key = it.next().getKey();
                if (isLocationsEquals(location,key)) {
                    it.remove();
                }
            }

            for (Iterator<Map.Entry<Location, String>> it = mMarkersSnippets.entrySet().iterator(); it.hasNext();) {
                Location key = it.next().getKey();
                if (isLocationsEquals(location,key)) {
                    it.remove();
                }
            }

            for (Iterator<Map.Entry<Location, MarkerStatus>> it = mMarkerStatuses.entrySet().iterator(); it.hasNext();) {
                Location key = it.next().getKey();
                if (isLocationsEquals(location,key)) {
                    it.remove();
                }
            }
        }
    }

    // this function remove marker completely
    public static void removeMarker(Context context, Location location){
        removeMarker(location);
        refreshMap(context);
    }

    public static void addMarker(Context context, Location location, String title, String snippet, String status){
        if(mMap==null || location==null)
            return;
        BitmapDescriptor icon = (getStatusIconResource(status).equals(ICON_RESOURCE_ID_NOT_EXIST)) ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED) : BitmapDescriptorFactory.fromBitmap(getBitmap(context,getStatusIconResource(status)));
        Marker tmpMarker = mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(location.getLatitude(),location.getLongitude()))
                        .title(title)
                        .snippet(snippet)
                        .icon(icon));
        addMarkerToList(location, tmpMarker, title, snippet, status);
        refreshMap(context);
    }

    public static void addMarkers(Context context, ArrayList<Location> locations, ArrayList<String> titles, ArrayList<String> snippets, ArrayList<String> statues){
        if(locations==null || titles==null || snippets==null || statues==null) return;
        for(int i=0; i<locations.size(); i++){
            addMarker(context, locations.get(i), titles.get(i), snippets.get(i), statues.get(i));
        }
    }

    public static void changeStatus(Context context, Location location, String newStatus){
        setMarkerStatus(location,newStatus);
        refreshMap(context);
    }

    public static void select(Location location){
        if(location!=null) {
            mSelectedLocation = location;
            mSelectedMarker = getMarker(location);
        }
    }

    public static void deselect(){
        mSelectedMarker = null;
        mSelectedLocation = null;
    }

    public static boolean isSelected(Location location){
        return isLocationsEquals(location, mSelectedLocation);
    }

    public static void drawCircle(Location center, double radius, float strokeWidth, int strokeColor, int fillColor){
        if(mMap!=null && center!=null) {
            LatLng latLng = new LatLng(center.getLatitude(), center.getLongitude());
            CircleOptions co = new CircleOptions()
                    .center(latLng)
                    .radius(radius)
                    .strokeWidth(strokeWidth)
                    .strokeColor(strokeColor)
                    .fillColor(fillColor);
            mMap.addCircle(co);
            mCircles.add(co);
        }
    }

    public static void drawCircle(LatLng center, double radius, float strokeWidth, int strokeColor, int fillColor){
        if(mMap!=null && center!=null) {
            CircleOptions co = new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(strokeWidth)
                    .strokeColor(strokeColor)
                    .fillColor(fillColor);
            mMap.addCircle(co);
            mCircles.add(co);
        }
    }

    public static void drawPolygon(float strokeWidth, int strokeColor, int fillColor, LatLng... latLngs){
        if(mMap!=null && latLngs.length > 0) {
            PolygonOptions po = new PolygonOptions()
                    .add(latLngs)
                    .strokeWidth(strokeWidth)
                    .strokeColor(strokeColor)
                    .fillColor(fillColor);
            mMap.addPolygon(po);
            mPolygons.add(po);
        }
    }

    public static void removeCircle(Context context, Location center){
        for (Iterator<CircleOptions> iterator = mCircles.iterator(); iterator.hasNext(); ) {
            CircleOptions co = iterator.next();
            Location location = locationInstance(co.getCenter().latitude, co.getCenter().longitude);
            if (isLocationsEquals(location, center)) {
                iterator.remove();
            }
        }
        refreshMap(context);
    }

    public static void removeCircle(Context context, LatLng center){
        Location centerLocation = locationInstance(center.latitude, center.longitude);
        for (Iterator<CircleOptions> iterator = mCircles.iterator(); iterator.hasNext(); ) {
            CircleOptions co = iterator.next();
            Location location = locationInstance(co.getCenter().latitude, co.getCenter().longitude);
            if (isLocationsEquals(location, centerLocation)) {
                iterator.remove();
            }
        }
        refreshMap(context);
    }

    public static void refreshMap(Context context) {
        mMap.clear();
        if(mMarkers.size() > 0) {
            for (Map.Entry<Location, Marker> entry : mMarkers.entrySet()) {
                Location location = entry.getKey();
                MarkerStatus ms = getMarkerStatus(location);
                Integer iconResId = ms!=null ? getStatusIconResource(ms.getName()) : ICON_RESOURCE_ID_NOT_EXIST;
                BitmapDescriptor icon = (iconResId.equals(ICON_RESOURCE_ID_NOT_EXIST)) ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED) : BitmapDescriptorFactory.fromBitmap(getBitmap(context, iconResId));
                String title = getMarkerTitle(location);
                String snippet = getMarkerSnippet(location);
                mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(location.getLatitude(),location.getLongitude()))
                                .title(title)
                                .snippet(snippet)
                                .icon(icon));
            }
        }
        if(mCurrentLocation!=null && mCurrentMarker!=null) {
            BitmapDescriptor icon = (mCurrentMarkerIconResourceId==null || mCurrentMarkerIconResourceId.equals(ICON_RESOURCE_ID_NOT_EXIST)) ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED): BitmapDescriptorFactory.fromBitmap(getBitmap(context,mCurrentMarkerIconResourceId));
            mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                            .title(mCurrentMarker.getTitle())
                            .snippet(mCurrentMarker.getSnippet())
                            .icon(icon));
        }
        if(mCircles!=null && mCircles.size() > 0) {
            for(CircleOptions co : mCircles){
                mMap.addCircle(co);
            }
        }
        if(mPolygons!=null && mPolygons.size() > 0) {
            for(PolygonOptions po : mPolygons){
                mMap.addPolygon(po);
            }
        }
    }

    public static void filter(Context context, String... statues){
        if(mMap==null || statues.length==0) return;
        ArrayList<String> allowedStatuses = new ArrayList<>(Arrays.asList(statues));
        mMap.clear();
        if(mMarkers.size() > 0) {
            for (Map.Entry<Location, Marker> entry : mMarkers.entrySet()) {
                Location location = entry.getKey();
                MarkerStatus ms = getMarkerStatus(location);
                if(!allowedStatuses.contains(ms.getName())) continue;
                Integer iconResId = getStatusIconResource(ms.getName());
                BitmapDescriptor icon = (iconResId.equals(ICON_RESOURCE_ID_NOT_EXIST)) ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED) : BitmapDescriptorFactory.fromBitmap(getBitmap(context, iconResId));
                String title = getMarkerTitle(location);
                String snippet = getMarkerSnippet(location);
                mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(location.getLatitude(),location.getLongitude()))
                                .title(title)
                                .snippet(snippet)
                                .icon(icon));
            }
        }
        if(mCurrentLocation!=null) {
            BitmapDescriptor icon = (mCurrentMarkerIconResourceId==null || mCurrentMarkerIconResourceId.equals(ICON_RESOURCE_ID_NOT_EXIST)) ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED): BitmapDescriptorFactory.fromBitmap(getBitmap(context,mCurrentMarkerIconResourceId));
            mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                            .title(mCurrentMarker.getTitle())
                            .snippet(mCurrentMarker.getSnippet())
                            .icon(icon));
        }
        if(mCircles!=null && mCircles.size() > 0){
            for(CircleOptions co : mCircles){
                mMap.addCircle(co);
            }
        }
        if(mPolygons!=null && mPolygons.size() > 0){
            for(PolygonOptions po : mPolygons){
                mMap.addPolygon(po);
            }
        }
    }

    public static void filter(Context context, Location centerLocation, int maxDistanceToCenterLocation){
        if(mMap==null || centerLocation==null) return;
        mMap.clear();
        if(mMarkers.size() > 0) {
            for (Map.Entry<Location, Marker> entry : mMarkers.entrySet()) {
                Location location = entry.getKey();
                MarkerStatus ms = getMarkerStatus(location);
                if(centerLocation.distanceTo(location) > maxDistanceToCenterLocation) continue;
                Integer iconResId = getStatusIconResource(ms.getName());
                BitmapDescriptor icon = (iconResId.equals(ICON_RESOURCE_ID_NOT_EXIST)) ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED) : BitmapDescriptorFactory.fromBitmap(getBitmap(context, iconResId));
                String title = getMarkerTitle(location);
                String snippet = getMarkerSnippet(location);
                mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(location.getLatitude(),location.getLongitude()))
                                .title(title)
                                .snippet(snippet)
                                .icon(icon));
            }
        }
        if(mCurrentLocation!=null) {
            BitmapDescriptor icon = (mCurrentMarkerIconResourceId==null || mCurrentMarkerIconResourceId.equals(ICON_RESOURCE_ID_NOT_EXIST)) ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED): BitmapDescriptorFactory.fromBitmap(getBitmap(context,mCurrentMarkerIconResourceId));
            mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                            .title(mCurrentMarker.getTitle())
                            .snippet(mCurrentMarker.getSnippet())
                            .icon(icon));
        }
        if(mCircles!=null && mCircles.size() > 0){
            for(CircleOptions co : mCircles){
                mMap.addCircle(co);
            }
        }
        if(mPolygons!=null && mPolygons.size() > 0){
            for(PolygonOptions po : mPolygons){
                mMap.addPolygon(po);
            }
        }
    }

    public static void filter(Context context, int maxDistanceToCurrentLocation){
        Location currentLocation = getCurrentLocation();
        if(currentLocation!=null)
            filter(context, currentLocation, maxDistanceToCurrentLocation);
    }

    public static void clearMap(){
        if(mMap!=null) mMap.clear();
    }
}