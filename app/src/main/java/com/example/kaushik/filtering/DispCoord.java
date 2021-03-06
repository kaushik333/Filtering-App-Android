package com.example.kaushik.filtering;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;

import static com.example.kaushik.filtering.R.*;

public class DispCoord extends AppCompatActivity implements OnTouchListener, CvCameraViewListener2{
    private CameraBridgeViewBase mOpenCVCameraView;
    private Mat mRgba;
    private Scalar mBlobColorRgba;
    private Scalar mBlobColorHsv;
    double x = -1;
    double y = -1;

    TextView touch_coordinates;
    TextView touch_color;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCVCameraView.enableView();
                    mOpenCVCameraView.setOnTouchListener(DispCoord.this);
                }break;
                default:
                {
                    super.onManagerConnected(status);
                }break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_coord);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        touch_coordinates = (TextView)findViewById(R.id.touch_coordinates);
        touch_color = (TextView)findViewById(R.id.touch_color);
        mOpenCVCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_tutorial_activity_surface_view);
        mOpenCVCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCVCameraView.setCvCameraViewListener(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCVCameraView != null)
            mOpenCVCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mOpenCVCameraView !=null)
            mOpenCVCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height){
        mRgba = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
    }

    @Override
    public void onCameraViewStopped(){
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame)
    {
        mRgba = inputFrame.rgba();
        // mRgba = inputFrame.rgba();
        //Mat mRgbaT = mRgba.t();
        //Core.flip(mRgba.t(), mRgbaT, 1);
        //Imgproc.resize(mRgbaT, mRgbaT, mRgba.size());
        //return mRgbaT;
        return mRgba;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        int cols = mRgba.cols();
        int rows = mRgba.rows();
        double yLow = (double)mOpenCVCameraView.getHeight()*0.2401961;
        double yHigh = (double)mOpenCVCameraView.getHeight()*0.7696078;
        double xScale = (double)cols/(double)mOpenCVCameraView.getWidth();
        double yScale = (double)rows/(yHigh-yLow);
        x = event.getX();
        y = event.getY();
        y = y - yLow;
        x = x*xScale;
        y = y*yScale;
        if((x<0)||(y<0)||(x>cols)||(y>rows))return false;
        touch_coordinates.setText("X: " + Double.valueOf(x) + ", Y: " + Double.valueOf(y));
        Rect touchedRect = new Rect();

        touchedRect.x = (int)x;
        touchedRect.y = (int)y;

        touchedRect.width = 8;
        touchedRect.height = 8;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width*touchedRect.height;
        for (int i=0;i<mBlobColorHsv.val.length;i++)
            mBlobColorHsv.val[i] /=pointCount;

        mBlobColorRgba =convertScalarHsv2Rgba(mBlobColorHsv);

        touch_color.setText("color: #" + String.format("%02X", (int)mBlobColorRgba.val[0]) + String.format("%02X", (int)mBlobColorRgba.val[1])+ String.format("%02X", (int)mBlobColorRgba.val[2]) );
        touch_color.setTextColor(Color.rgb((int)mBlobColorRgba.val[0],
                (int)mBlobColorRgba.val[1],
                (int)mBlobColorRgba.val[2]));
        touch_coordinates.setTextColor(Color.rgb((int)mBlobColorRgba.val[0],
                (int)mBlobColorRgba.val[1],
                (int)mBlobColorRgba.val[2]));
        return false;
    }

    private Scalar convertScalarHsv2Rgba(Scalar hsvColor)
    {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0,0));
    }


}