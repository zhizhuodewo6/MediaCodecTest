package com.github.piasy.mediacodecrctest;

import android.media.MediaFormat;
import android.os.Environment;
import com.tencent.mars.xlog.Log;
import java.io.File;
import org.webrtc.EglBase;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;

/**
 * Created by Piasy{github.com/Piasy} on 01/08/2017.
 */

public class RcTest implements FrameProducer.Callback {
    private static final String TAG = "RcTest";

    private final SurfaceViewRenderer mSurfaceViewRenderer;
    private final EglBase mEglBase;
    private final FrameProducer mFrameProducer;
    private final EncoderWrapper mEncoderWrapper;

    private int mVideoWidth;
    private int mVideoHeight;

    private volatile boolean mFinished;

    public RcTest(final Config config, final EglBase eglBase, final SurfaceViewRenderer renderer,
            final Notifier notifier) {
        mEglBase = eglBase;
        mSurfaceViewRenderer = renderer;
        File videoFile = new File(Environment.getExternalStorageDirectory(),
                "alien-covenant.mp4");
        String s = videoFile.getAbsolutePath().toString();
        Log.e("haha","文件路径="+s);
        if(videoFile.exists()){
            Log.e("haha","该文件存在");
        }
        mFrameProducer = new FrameProducer(mEglBase, videoFile, config.outputFps(), this);
        mEncoderWrapper = new EncoderWrapper(config, notifier);
    }

    public void start(EncodeCallBack encodeCallBack) {
        mFrameProducer.start();
        mEncoderWrapper.start(mEglBase,encodeCallBack);
    }

    public boolean finished() {
        return mFinished;
    }

    public void stop() {
        mFrameProducer.stop();
    }

    @Override
    public void onReady(final MediaFormat mediaFormat) {
        mVideoWidth = mediaFormat.getInteger(MediaFormat.KEY_WIDTH);
        mVideoHeight = mediaFormat.getInteger(MediaFormat.KEY_HEIGHT);
        Log.i(TAG, "Video size: %dx%d", mVideoWidth, mVideoHeight);
        Log.i(TAG, "VideoFormat decoder " + mediaFormat);
    }

    @Override
    public void onFrame(final int oesTextureId, final float[] transformMatrix,
            final long timestampNs) {//生产者返回解码的数据
        VideoRenderer.I420Frame frame = new VideoRenderer.I420Frame(mVideoWidth, mVideoHeight, 0,
                oesTextureId, transformMatrix, 0, timestampNs);
        mSurfaceViewRenderer.renderFrame(frame);//用于显示
        mEncoderWrapper.encodeFrame(frame);//用于编码
    }

    @Override
    public void onStop() {
        mFinished = true;
        mEncoderWrapper.stop();
    }

    public interface Notifier {
        void reportBr(int br);
    }

    public interface EncodeCallBack{
        void callBack(double br);
    }
}
