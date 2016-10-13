package com.example.shaohu.sensorsmonitor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager=null;
    private Sensor mRotationVectorSensor, mAccelerationSensor,mPressureSensor;
    private  int count=0;
    private FileSave mFile = null;
    private volatile float[] accelMatrix = new float[3];
    private volatile double pressureValue;
    private long lastUpdate = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager =(SensorManager)getSystemService(SENSOR_SERVICE);
        mRotationVectorSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mAccelerationSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mPressureSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mFile = new FileSave("/sdata"  + System.currentTimeMillis()+ ".csv", false);
        String line="Time, accX,accY,accZ,pressure\n";
        mFile.appendLog(line);

    }
    @Override
    protected void onPause()
    { super.onPause();
        mSensorManager.unregisterListener(this);

    }
    @Override
    protected  void onResume(){
        super.onResume();
        mSensorManager.registerListener(this,mRotationVectorSensor,1000);
         mSensorManager.registerListener(this, mAccelerationSensor,200000);
        mSensorManager.registerListener(this,mPressureSensor,200000);
    }
    @Override
    public void onSensorChanged(SensorEvent event){
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelMatrix[0] = event.values[0] ;
            accelMatrix[1] = event.values[1] ;
            accelMatrix[2] = event.values[2] ;
            //System.out: AccelerationSensor.getMinDelay()-------->5000   =0.005s=200HZ

           // System.out.println("AccelerationSensor.getMinDelay()-------->"
              //      + mAccelerationSensor.getMinDelay());
            //count++;
            //Log.i( "accelerometer",Integer.toString(count) );
        //Log.i( Double.toString(event.values[0]), Double.toString(event.values[1]));
        }
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            // System.out: mPressureSensor.getMinDelay()-------->180000  =0.18 =5HZ
            //System.out.println("mPressureSensor.getMinDelay()-------->"
             //       + mPressureSensor.getMinDelay());
            Log.i( "pressure",Double.toString(event.values[0]));
            pressureValue=event.values[0];

        }
         //1 second = 1000ms
        long curTime = System.currentTimeMillis();
        //Log.i( "curTime",Double.toString(curTime));
        if ((curTime - lastUpdate) > 200){ // only reads data twice per second
            lastUpdate = curTime;
            Log.i( "lastUpdate",Double.toString(lastUpdate));
            String line= DateFormat.getTimeInstance().format(curTime) + ","+Double.toString(accelMatrix[0])+ ","
                    +Double.toString(accelMatrix[1])+ ","+
                    Double.toString(accelMatrix[2])+ ","+Double.toString(pressureValue)+ "\n";
            System.out.println(line);
            Log.i( "line",line);
           mFile.appendLog(line);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

            long time = SystemClock.uptimeMillis();
    }
}
