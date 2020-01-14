AndExMapHelper ![API](https://img.shields.io/badge/API-17%2B-brightgreen.svg?style=flat) [![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)
===================

AndExMapHelper is a library to simplify the work with Android Google Map.
This library has many features such as: 
  - get and show current location
  - add one or many markers on map
  - manage markers icon with a concept called status. you can define an arbitrary count of status that each status has its own related icon. you can change status of markers which also changes the icon of marker.
  - remove a marker from map
  - draw circle and polygone on map
  - filter markers on map by status and also distance
  - ...
  



**This library is also available at JitPack.io**

[![](https://jitpack.io/v/salehyarahmadi/AndExMapHelper.svg)](https://jitpack.io/#salehyarahmadi/AndExMapHelper)



`this library is compatible to androidx`

## Preview
![](https://github.com/salehyarahmadi/AndExMapHelper/blob/master/andex-map-helper.gif)

## Setup
The simplest way to use this library is to add the library as dependency to your build.

## Gradle

Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency
  
    // builde.gradle(project level)
    dependencies {    
	      classpath 'com.google.gms:google-services:4.2.0'
	}

	// builde.gradle(app level)
    dependencies {    
	      implementation 'com.github.salehyarahmadi:AndExAlertDialog:v1.0.1'
          implementation 'com.google.android.gms:play-services-maps:17.0.0'
          implementation 'com.google.android.gms:play-services-location:17.0.0'
	}
 
Step 3. Add your google map api key to AndroidManifest.xml file
  
    <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="YOUR-GOOGLE-MAP-API-KEY" />
 


## Usage

### XML
    
    <?xml version="1.0" encoding="utf-8"?>
    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

    <fragment
        android:id="@+id/map"
        android:name="com.google.android.gms.maps.SupportMapFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity" />
    
    </RelativeLayout>

### Java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // initialize map
        AndExMapHelper.Init(context, googleMap,locationListener, minTimeToUpdateLocation, minDistanceToUpdateLocation);
        
        // define statuses and related marker icons for markers
        AndExMapHelper.setStatuses(new HashMap<String, Integer>(){{
            put("status_1", R.drawable.marker1);
            put("status_2", R.drawable.marker2);
            put("status_3", R.drawable.marker3);
        }});

        // get current location and move camera to it
        AndExMapHelper.animateCamera(AndExMapHelper.getCurrentLocation(),AndExMapHelper.DEFAULT_ZOOM);

        // set info windows adapter
        AndExMapHelper.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.item_marker_location, null);
                TextView tvTitle   = rowView.findViewById(R.id.tvMarkerTitle);
                TextView tvSnippet = rowView.findViewById(R.id.tvMarkerSnippet);
                tvTitle.setText(marker.getTitle());
                tvSnippet.setText(marker.getSnippet());
                return rowView;
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {
        // show current location on map with arbitary title, snippet and icon
        AndExMapHelper.showCurrentLocationOnMap(this,"Title", "Snippet", R.drawable.ic_current_location);
    }



#### Add Marker
    AndExMapHelper.addMarker(context,location, title, snippet, status);

#### Create a location object
    AndExMapHelper.locationInstance(latitude,longitude);

#### Get location of selected marker
    AndExMapHelper.getSelectedLocation();

#### Change Status
    AndExMapHelper.changeStatus(context, location, newStatus);

#### Remove marker
    AndExMapHelper.removeMarker(context, location);

#### Draw circle
    AndExMapHelper.drawCircle(centerLocation, radius, strokeWidth, strokeColor, fillColor);
    AndExMapHelper.drawCircle(centerLatLng, radius, strokeWidth, strokeColor, fillColor);

#### Remove circle
    AndExMapHelper.removeCircle(context, centerLocation);

#### Draw Polygon
    AndExMapHelper.drawPolygon(strokeWidth, strokeColor, fillColor, LatLng... latLngs);

#### Filter
    // filter with statues. only markers display whose its status be in statues
    AndExMapHelper.filter(context, String... statuses); 

    // filter with distance. only markers display whose distance to centerLocation be less than maxDistanceToCenterLocation parameter
    AndExMapHelper.filter(context, centerLocation, maxDistanceToCenterLocation);

    // filter with distance. only markers display whose distance to current Location be less than maxDistanceToCurrentLocation parameter
    AndExMapHelper.filter(context, maxDistanceToCurrentLocation);

#### Refresh the map
    AndExMapHelper.refreshMap(context);

#### Clear the map
    AndExMapHelper.clearMap();


    


        
 ## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2019 AndExMapHelper

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
       
