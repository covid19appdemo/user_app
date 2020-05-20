package com.sourcey.materiallogindemo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static final int REQUEST_LOCATION =1 ;
    private TextView _loginLink;
    private EditText _emailText, _name, _state, _dist, _area, _mno, _vno;
    private Button _signupbtn, loc;
    public static final String ROOT_URL = "https://tracksystem20.000webhostapp.com/";
    protected LocationListener locationListener;
    protected LocationManager locationManager;
    private static String LatLoc,LonLoc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        _emailText = (EditText) findViewById(R.id.input_email);
        _name = (EditText) findViewById(R.id.input_name);
        _dist = (EditText) findViewById(R.id.input_Dist);
        _area = (EditText) findViewById(R.id.input_area);
        _state = (EditText) findViewById(R.id.input_state);
        _mno = (EditText) findViewById(R.id.input_mno);
        _vno = (EditText) findViewById(R.id.input_vno);
        loc=(Button) findViewById(R.id.btn_loc);
        _signupbtn = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        _signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
                insertUser();
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        loc.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v){
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
             OnGps();
             }
            else {
            getLocation();
             }
          }
         });
}
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location locationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (locationGps != null) {
                double lat = locationGps.getLatitude();
                double lon = locationGps.getLongitude();
                LatLoc = String.valueOf(lat);
                LonLoc = String.valueOf(lon);
                Toast.makeText(this, "Location gps:" + LatLoc + " & " + LonLoc, Toast.LENGTH_SHORT).show();
                //tv.setText("your location: "+latitude+" and "+longitude);
            } else if (locationNetwork != null) {
                double lat = locationNetwork.getLatitude();
                double lon = locationNetwork.getLongitude();
                LatLoc = String.valueOf(lat);
                LonLoc = String.valueOf(lon);
                Toast.makeText(this, "Location gps:" + LatLoc + " & " + LonLoc, Toast.LENGTH_SHORT).show();
                //tv.setText("your location: "+latitude+" and "+longitude);
            } else if (locationPassive != null) {
                double lat = locationPassive.getLatitude();
                double lon = locationPassive.getLongitude();
                LatLoc = String.valueOf(lat);
                LonLoc = String.valueOf(lon);
                Toast.makeText(this, "Location gps:" + LatLoc + " & " + LonLoc, Toast.LENGTH_SHORT).show();
                //tv.setText("your location: "+latitude+" and "+longitude);
            } else {
                Toast.makeText(this, "Can't get your location", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void OnGps () {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

    }

    private void insertUser() {
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        reg_api api1 = adapter.create(reg_api.class);

        //Defining the method insertuser of our interface
        api1.insertUser(
                //Passing the values by getting it from editTexts
                _name.getText().toString(),
                _emailText.getText().toString(),
                _state.getText().toString(),
                _dist.getText().toString(),
                _area.getText().toString(),
                _mno.getText().toString(),
                _vno.getText().toString(),
                LatLoc,LonLoc,

                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

                        //An string to store output from the server
                        String output = "";
                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                            Toast.makeText(SignupActivity.this, "Out=" + output, Toast.LENGTH_SHORT).show();
                            if (output.equals("OK")) {
                                // loginCheck();
                                Toast.makeText(SignupActivity.this, "welcome", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(SignupActivity.this, "Invalis User name or Password", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Displaying the output as a toast
                        Toast.makeText(SignupActivity.this, output, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupbtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String name = _name.getText().toString();
        String dist = _dist.getText().toString();
        String area = _area.getText().toString();
        String state = _state.getText().toString();
        String mno = _mno.getText().toString();
        String vno=_vno.getText().toString();
        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupbtn.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupbtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String name = _name.getText().toString();
        String dist = _dist.getText().toString();
        String area = _area.getText().toString();
        String state = _state.getText().toString();
        String mno = _mno.getText().toString();
        String vno=_vno.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (name.isEmpty())
        {
            _name.setError("enter your name");
            valid = false;
        }
        else {
            _name.setError(null);
        }
        if (dist.isEmpty())
        {
            _dist.setError("enter your name");
            valid = false;
        }
        else {
            _dist.setError(null);
        } if (state.isEmpty())
        {
            _state.setError("enter your name");
            valid = false;
        }
        else {
            _state.setError(null);
        }
        if (area.isEmpty())
        {
            _area.setError("enter your name");
            valid = false;
        }
        else {
            _area.setError(null);
        }
        if(mno.length()!= 10|| mno.isEmpty()) {
            _mno.setError("enter valid mobile number");
            valid=false;
        }
        else {
            _mno.setError(null);
        }
        if( vno.isEmpty()) {
            _vno.setError("enter valid mobile number");
            valid=false;
        }
        else {
            _vno.setError(null);
        }
        return valid;
    }
}