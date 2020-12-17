package com.github.piasy.mediacodecrctest;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.github.piasy.mediacodecrctest.chart.EchartOptionUtil;
import com.github.piasy.mediacodecrctest.chart.EchartView;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;
import org.webrtc.EglBase;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RcTest.Notifier {
    private final String TAG="MainActivity";

    RxPermissions rxPermissions;

    private static final String[] BIT_RATE_MODES = new String[] {
            "CQ", "VBR", "CBR"
    };

    static { //xlog需要使用到我们前面编译出的两个so库：
        System.loadLibrary("stlport_shared");
        System.loadLibrary("marsxlog");
    }

    @BindView(R.id.mBtnStart)
    Button mBtnStart;
    @BindView(R.id.mBtnStop)
    Button mBtnStop;
    @BindView(R.id.mTvInfo)
    TextView mTvInfo;
    @BindView(R.id.mEtInitBr)
    EditText mEtInitBr;
    @BindView(R.id.mEtBrStep)
    EditText mEtBrStep;
    @BindView(R.id.mEtQuality)
    EditText mEtQuality;
    @BindView(R.id.mSpBitrateMode)
    Spinner mSpBitrateMode;
    @BindView(R.id.mCbAsyncEnc)
    CheckBox mCbAsyncEnc;
    @BindView(R.id.mCbUpdateBr)
    CheckBox mCbUpdateBr;
    @BindView(R.id.mSurface)
    SurfaceViewRenderer mSurface;
    @BindView(R.id.webview)
    EchartView webview;

    private RcTest mRcTest;
    private Config mConfig;
    private EglBase mEglBase;

    private List<String> typeTitles;//码率调整模式种类
    private List<String> time;//码率调整频率时间
    private List<List<Double>> datas;//码率效果
    private List<Double> brData;//每次调整结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = new RxPermissions(this);


        final String logPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                               + "/rc_test_log";
        final String cachePath = this.getFilesDir() + "/xlog";
        Xlog.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, cachePath, logPath, "RcTest");
        Xlog.setConsoleLogOpen(true);
        Log.setLogImp(new Xlog());

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ArrayAdapter outputSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, BIT_RATE_MODES);
        outputSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_list_item_single_choice);
        mSpBitrateMode.setAdapter(outputSpinnerAdapter);

        mEglBase = EglBase.create();
        mSurface.init(mEglBase.getEglBaseContext(), null);
        mSurface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                mBtnStart.setEnabled(true);
            }

            @Override
            public void surfaceChanged(final SurfaceHolder holder, final int format,
                    final int width, final int height) {
            }

            @Override
            public void surfaceDestroyed(final SurfaceHolder holder) {
            }
        });

        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                    } else {
                        // Oups permission denied
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSurface.release();
        mEglBase.release();
        if (mRcTest != null) {
            mRcTest.stop();
        }

        Log.appenderFlush(true);
        Log.appenderClose();
        mSurface.release();
    }

    @OnClick(R.id.mBtnStart)
    public void startTest() {
        mSurface.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);

        if (mRcTest != null && !mRcTest.finished()) {
            Toast.makeText(this, "Last test still running", Toast.LENGTH_SHORT).show();
            return;
        }
        mSurface.setVisibility(View.VISIBLE);
        webview.setVisibility(View.GONE);
        mConfig = Config.builder()
                .updateBr(mCbUpdateBr.isChecked())
                .asyncEnc(mCbAsyncEnc.isChecked())
                .initBr(Integer.parseInt(mEtInitBr.getText().toString()))
                .brStep(Integer.parseInt(mEtBrStep.getText().toString()))
                .quality(Integer.parseInt(mEtQuality.getText().toString()))
                .brMode(mSpBitrateMode.getSelectedItemPosition())
                .outputWidth(448)
                .outputHeight(800)
                .outputFps(30)
                .outputKeyFrameInterval(2)
                .build();
        mRcTest = new RcTest(mConfig, mEglBase, mSurface, this);
        brData=new ArrayList<Double>();
        mRcTest.start(new RcTest.EncodeCallBack() {
            @Override
            public void callBack(double br) {
                Log.e("haha","当前标准br值="+br);
                runOnUiThread(new  Runnable() {
                    public void run() {
                        Log.e("haha","br值="+ br+";brData值="+Arrays.asList(brData));

                        if(typeTitles==null){
                            typeTitles=new ArrayList<>();
                            typeTitles.add("标准");
                        }
                        typeTitles.add(BIT_RATE_MODES[mSpBitrateMode.getSelectedItemPosition()]);
                        if(time==null){
                            time=new ArrayList<String>();
                            for(int i=0;i<brData.size();i++){
                                time.add(i+"");
                            }
                        }
                        if(datas==null){
                            datas=new ArrayList<List<Double>>();
                            List<Double> data=new ArrayList<Double>();
                            for(int i=0;i<brData.size();i++){
                                double v = Double.parseDouble(mEtInitBr.getText().toString() + "000");
                                data.add(v);
                            }
                            datas.add(data);
                        }
                        List<Double> data=new ArrayList<>();
                        for(int j=0;j<brData.size();j++){
                            data.add(brData.get(j));
                        }
                        datas.add(data);
                        String s = EchartOptionUtil.getLineChartOptions(typeTitles, time, datas).toString();
                        Log.e("haha","option值="+s);
                        webview.load("file:///android_asset/echart/web_zhts_meeting.html", s);
                        mSurface.setVisibility(View.GONE);
                        webview.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
    }

    @OnClick(R.id.mBtnStop)
    public void stop(){
        if (mRcTest != null) {
            mRcTest.stop();
        }

    }

    @Override
    public void reportBr(final int br) {
        Log.e(TAG,"reportBr工作了br值="+br);
        brData.add((double)br);
        mTvInfo.post(new Runnable() {
            @Override
            public void run() {
                mTvInfo.setText("br: " + br);
            }
        });
    }
}
