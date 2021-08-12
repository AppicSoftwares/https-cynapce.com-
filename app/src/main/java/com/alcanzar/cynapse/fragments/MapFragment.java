package com.alcanzar.cynapse.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.alcanzar.cynapse.activity.TicketDetails;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    double latitude, longitude;
    //    Activity activity;
    private GoogleMap mMap;
    boolean clickbool = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

       // Log.e("LATTTTTTT", "= " + getArguments().getDouble("lat", 0.0));

        latitude = getArguments().getDouble("lat", 0.0);
        longitude = getArguments().getDouble("log", 0.0);
        Log.e("LATTTTTTT", "= " + getArguments().getDouble("lat", 0.0));

        this.getMapAsync(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TicketDetails.REQUEST_PLACE_PICKER) {
            // This result is from the PlacePicker dialog.


            if (resultCode == Activity.RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(getActivity(), data);

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */
//                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
//                final CharSequence phone = place.getPhoneNumber();
//                final String placeId = place.getId();

                LatLng latLng = place.getLatLng();
                ((TicketDetails) getActivity()).latitude = latLng.latitude;
                ((TicketDetails) getActivity()).longitude = latLng.longitude;

                latitude = latLng.latitude;
                longitude = latLng.longitude;

                LatLng user = new LatLng(latitude, longitude);

                mMap.addMarker(new MarkerOptions().position(user));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
                CameraUpdate updateZoom = CameraUpdateFactory.newLatLngZoom(user, 15);
                mMap.animateCamera(updateZoom);


//                ((TicketDetails) getActivity()).setGetPreferences.setSharedPreferences_Latitude(String.valueOf(latLng.latitude));
//                ((TicketDetails) getActivity()).setGetPreferences.setSharedPreferences_Longitude(String.valueOf(latLng.longitude));

                clickbool = false;
//                String attribution = PlacePicker.getAttributions(data);
//                if (attribution == null) {
//                    attribution = "";
//                }

              //  ((TicketDetails) getActivity()).tv_fragmentMap_pharmacyAddress.setText(address.toString());

                // Print data to debug log
                Log.e("PLACE", "Place selected: " + " (" + address.toString() + ")");
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // END_INCLUDE(activity_result)
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.getUiSettings().setScrollGesturesEnabled(false);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    11);

            return;
        }
        mMap.setMyLocationEnabled(true);
        LatLng user = new LatLng(latitude, longitude);

        googleMap.addMarker(new MarkerOptions().position(user));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
        CameraUpdate updateZoom = CameraUpdateFactory.newLatLngZoom(user, 15);
        mMap.animateCamera(updateZoom);

//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//                try {
//
//                    if (!clickbool) {
//                        clickbool = true;
//                        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
//                        Intent intent = intentBuilder.build(getActivity());
//                        // Start the Intent by requesting a result, identified by a request code.
//                        startActivityForResult(intent, TicketDetails.REQUEST_PLACE_PICKER);
//
//                    }
//                } catch (GooglePlayServicesRepairableException e) {
//                    GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    Toast.makeText(getActivity(), "Google Play Services is not available.",
//                            Toast.LENGTH_LONG)
//                            .show();
//                }
//
//
//            }
//        });
    }
}
