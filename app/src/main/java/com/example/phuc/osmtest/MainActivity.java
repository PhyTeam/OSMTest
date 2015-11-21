package com.example.phuc.osmtest;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<OverlayItem> pList = new ArrayList<OverlayItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(9);
        GeoPoint startPoint = new GeoPoint(48.8583, 2, 2944);
        mapController.setCenter(startPoint);

        ResourceProxy defautResImp = new DefaultResourceProxyImpl(this);
        MyOverlayer test = new MyOverlayer(pList, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return false;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        }, defautResImp);
        map.getOverlays().add(test);
        try {
            LocationManager manager = getLocationManager();
            Location loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            GeoPoint point = new GeoPoint(loc);
            mapController.setCenter(point);
            mapController.animateTo(point);
            pList.add(new OverlayItem("Heere","dfd", new GeoPoint(loc)));

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (SecurityException e){

        }

    }


    protected LocationManager getLocationManager() throws IllegalAccessException {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            Location loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc != null) showMessage(loc);
        }
        catch (SecurityException e){

        }
        return manager;

    }

    protected void showMessage(Location loc){
        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(MainActivity.this);
        alerBuilder.setTitle("Hello");
        alerBuilder.setMessage("Hello world!" + loc.getLatitude() + " " +loc.getLongitude());
        alerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alerBuilder.create().show();
    }

    class MyOverlayer extends ItemizedIconOverlay<OverlayItem> {
        private List<OverlayItem> pList;
        public MyOverlayer(List<OverlayItem> pList,org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
                                     ResourceProxy pResourceProxy)
        {
            super(pList, pOnItemGestureListener, pResourceProxy);
            this.pList = pList;
        }
        @Override
        protected void draw(Canvas canvas, MapView mapView, boolean shadow) {
            super.draw(canvas, mapView, shadow);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.marker_default);
            if (!pList.isEmpty()){
                for (OverlayItem item : pList) {
                    IGeoPoint geoPoint = item.getPoint();
                    Point out = new Point();
                    mapView.getProjection().toPixels(geoPoint, out);
                    canvas.drawBitmap(bm,out.x - bm.getWidth()/2,out.y - bm.getHeight()/2,null);
                }
            }
        }
    }
}
