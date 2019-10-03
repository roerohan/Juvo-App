package roerohan.com.myapplication;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnRequestLoc;
    private TextView txtCoordinates;
    private LocationManager locationManager;
    private LocationListener locationListener;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCallForHelp = findViewById(R.id.btnCallForHelp) ;
        btnCallForHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enter code for sending notification.

            }
        });

        //Attempt to launch second activity
        final Button btnContactInfo = findViewById(R.id.btnContactInfo);
        btnContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), SecondActivity.class);
                startIntent.putExtra("com.roerohan.MyApplication.SOMETHING", "Enter Contact Details");
                startActivity(startIntent);
            }
        });

        //Attempt to launch an activity outside our app

        final Button btnOpenMap = findViewById(R.id.btnOpenMap);
        btnOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String google = "https://www.google.co.in/maps/place/Bengaluru,+Karnataka/@12.9538477,77.3507442,10z/data=!3m1!4b1!4m5!3m4!1s0x3bae1670c9b44e6d:0xf8dfc3e8517e4fe0!8m2!3d12.9715987!4d77.5945627";
                Uri webAddress = Uri.parse(google);


                Intent gotoGoogle = new Intent(Intent.ACTION_VIEW, webAddress);
                if (gotoGoogle.resolveActivity(getPackageManager()) != null) {
                    startActivity(gotoGoogle);
                }
            }
        });

        final Button btnTestNotification = findViewById(R.id.btnTestNotification);
        btnRequestLoc = findViewById(R.id.btnRequestLoc);
        txtCoordinates = findViewById(R.id.txtCoordinates);

        final Button btnViewMore = findViewById(R.id.btnMoreOptions);
        btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnOpenMap.getVisibility() == View.INVISIBLE) {
                    //Options are currently hidden; this makes them visible
                    btnOpenMap.setVisibility(View.VISIBLE);
                    btnRequestLoc.setVisibility(View.VISIBLE);
                    btnContactInfo.setVisibility(View.VISIBLE);
                    txtCoordinates.setVisibility(View.VISIBLE);
                    btnTestNotification.setVisibility(View.VISIBLE);

                    btnViewMore.setText("Hide More Options");
                } else {
                    //Options are currently showing; this makes them invisible
                    btnOpenMap.setVisibility(View.INVISIBLE);
                    btnRequestLoc.setVisibility(View.INVISIBLE);
                    btnContactInfo.setVisibility(View.INVISIBLE);
                    txtCoordinates.setVisibility(View.INVISIBLE);
                    btnTestNotification.setVisibility(View.INVISIBLE);

                    btnViewMore.setText("View More Options");
                }
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                txtCoordinates.append("\n " + location.getLongitude() + " " + location.getLatitude());
    //            txtCoordinates.setText(txtCoordinates.getText() + "\n " + location.getLongitude() + ", " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
                return;
            } else {
                configureButton();
            }
        } else {
            configureButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                configureButton();
        }
    }

    private void configureButton() {
        btnRequestLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates("gps", 3000, 0, locationListener);
            }
        });


    }
    public void sendNotification(View view) {

        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("My notification")
                        .setContentText("You're in an unsafe area");


        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // When you issue multiple notifications about the same type of event,
        // it’s best practice for your app to try to update an existing notification
        // with this new information, rather than immediately creating a new notification.
        // If you want to update this notification at a later date, you need to assign it an ID.
        // You can then use this ID whenever you issue a subsequent notification.
        // If the previous notification is still visible, the system will update this existing notification,
        // rather than create a new one. In this example, the notification’s ID is 001//

        mNotificationManager.notify(001, mBuilder.build());

        long[] v = {500,1000};
        mBuilder.setVibrate(v);
    }
}
