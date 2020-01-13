package ir.androidexception.andexmaphelperexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import ir.androidexception.andexmaphelper.AndExMapHelper;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final String MARKER_STATUS_SUCCESS = "success";
    private static final String MARKER_STATUS_FAILED = "failed";
    private static final String MARKER_STATUS_DEFAULT = "default";

    private LatLng L1 = new LatLng(35.730991,51.344566);
    private LatLng L2 = new LatLng(35.728901,51.339439);
    private LatLng L3 = new LatLng(35.731270,51.336830);
    private LatLng L4 = new LatLng(35.728359,51.346344);
    private LatLng L5 = new LatLng(35.730148,51.335468);
    private LatLng L6 = new LatLng(35.730625,51.337208);
    private LatLng L7 = new LatLng(35.729153,51.336884);
    private LatLng L8 = new LatLng(35.728073,51.345558);
    private LatLng L9 = new LatLng(35.732361,51.336025);
    private LatLng L10 = new LatLng(35.727619,51.339596);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnChangeStatusToSuccess = findViewById(R.id.btn_change_status_to_success);
        Button btnChangeStatusToFailed = findViewById(R.id.btn_change_status_to_failed);
        Button btnRemoveSelectedMarker = findViewById(R.id.btn_remove_selected_marker);
        Button btnDrawCircleAroundMarker = findViewById(R.id.btn_draw_circle);
        Button btnRemoveCircleAroundMarker = findViewById(R.id.btn_remove_circle);
        Button btnFilterByStatus = findViewById(R.id.btn_filter_by_status);
        Button btnFilterByDistance = findViewById(R.id.btn_filter_by_distance);
        Button btnRefreshMap = findViewById(R.id.btn_refresh_map);
        btnChangeStatusToSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location selectedLocation = AndExMapHelper.getSelectedLocation();
                if(selectedLocation!=null) {
                    AndExMapHelper.changeStatus(MainActivity.this, selectedLocation, MARKER_STATUS_SUCCESS);
                }
            }
        });
        btnChangeStatusToFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location selectedLocation = AndExMapHelper.getSelectedLocation();
                if(selectedLocation!=null) {
                    AndExMapHelper.changeStatus(MainActivity.this, selectedLocation, MARKER_STATUS_FAILED);
                }
            }
        });
        btnRemoveSelectedMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location selectedLocation = AndExMapHelper.getSelectedLocation();
                if(selectedLocation!=null) {
                    AndExMapHelper.removeMarker(MainActivity.this, selectedLocation);
                }
            }
        });
        btnDrawCircleAroundMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location selectedLocation = AndExMapHelper.getSelectedLocation();
                if(selectedLocation!=null) {
                    AndExMapHelper.drawCircle(selectedLocation,300, 1, 0x33fa163f, 0x33fa163f);
                }
            }
        });
        btnRemoveCircleAroundMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location selectedLocation = AndExMapHelper.getSelectedLocation();
                if(selectedLocation!=null) {
                    AndExMapHelper.removeCircle(MainActivity.this, selectedLocation);
                }
            }
        });
        btnFilterByStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndExMapHelper.filter(MainActivity.this, MARKER_STATUS_SUCCESS, MARKER_STATUS_FAILED);
            }
        });
        btnFilterByDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndExMapHelper.filter(MainActivity.this, 700);
            }
        });
        btnRefreshMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndExMapHelper.refreshMap(MainActivity.this);
            }
        });




        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        AndExMapHelper.Init(this, googleMap,this, 0, 10);
        AndExMapHelper.setStatuses(new HashMap<String, Integer>(){{
            put(MARKER_STATUS_SUCCESS, R.drawable.ic_success_marker);
            put(MARKER_STATUS_FAILED, R.drawable.ic_failed_marker);
            put(MARKER_STATUS_DEFAULT, R.drawable.ic_defaulf_marker);
        }});
        //AndExMapHelper.showCurrentLocationOnMap(this,"My Location", null, R.drawable.ic_current_location);
        AndExMapHelper.animateCamera(AndExMapHelper.getCurrentLocation(),AndExMapHelper.DEFAULT_ZOOM);
        addMarkers();
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
        AndExMapHelper.showCurrentLocationOnMap(this,"My Location", null, R.drawable.ic_current_location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void addMarkers(){
        AndExMapHelper.addMarker(this,AndExMapHelper.locationInstance(L1.latitude,L1.longitude), "Title 1", "Snippet 1", MARKER_STATUS_DEFAULT);
        AndExMapHelper.addMarker(this,AndExMapHelper.locationInstance(L2.latitude,L2.longitude), "Title 2", "Snippet 2", MARKER_STATUS_DEFAULT);
        AndExMapHelper.addMarker(this,AndExMapHelper.locationInstance(L3.latitude,L3.longitude), "Title 3", "Snippet 3", MARKER_STATUS_DEFAULT);
        AndExMapHelper.addMarker(this,AndExMapHelper.locationInstance(L4.latitude,L4.longitude), "Title 4", "Snippet 4", MARKER_STATUS_DEFAULT);
        AndExMapHelper.addMarker(this,AndExMapHelper.locationInstance(L5.latitude,L5.longitude), "Title 5", "Snippet 5", MARKER_STATUS_DEFAULT);
        AndExMapHelper.addMarker(this,AndExMapHelper.locationInstance(L6.latitude,L6.longitude), "Title 6", "Snippet 6", MARKER_STATUS_DEFAULT);
        AndExMapHelper.addMarker(this,AndExMapHelper.locationInstance(L7.latitude,L7.longitude), "Title 7", "Snippet 7", MARKER_STATUS_DEFAULT);
        AndExMapHelper.addMarker(this,AndExMapHelper.locationInstance(L8.latitude,L8.longitude), "Title 8", "Snippet 8", MARKER_STATUS_DEFAULT);
        AndExMapHelper.addMarker(this,AndExMapHelper.locationInstance(L9.latitude,L9.longitude), "Title 9", "Snippet 9", MARKER_STATUS_DEFAULT);
        AndExMapHelper.addMarker(this,AndExMapHelper.locationInstance(L10.latitude,L10.longitude), "Title 10", "Snippet 10", MARKER_STATUS_DEFAULT);
    }
}
