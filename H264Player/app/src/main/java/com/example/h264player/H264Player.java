package com.example.h264player;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.view.Surface;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class H264Player implements Runnable {
    private final static String TAG = "H264Player";
    private Context mContext;
    private MediaCodec mVideoCodec;
    private Surface mSurface;
    private String mVideoPath;

    public H264Player(Context context, String videoPath, Surface surface) {
        this.mContext = context;
        this.mSurface = surface;
        this.mVideoPath = videoPath;
        initCode();
    }

    private void initCode() {
        try {
            mVideoCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            MediaFormat videoFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, 720, 1280);
            videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
            videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
            videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, 10000000);
            videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
            mVideoCodec.configure(videoFormat, mSurface, null, 0);
        } catch (Exception e) {

        }

    }

    public void start() {
        mVideoCodec.start();
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            decodeH264();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void decodeH264() {
        byte[] bytes = null;
        try {
            bytes = getFileByte(1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int startIndex = 0;
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        while (true) {
            int nextFrameIndex = findFrame(bytes, startIndex + 2, bytes.length);
            int index = mVideoCodec.dequeueInputBuffer(10000);
            if (index >= 0) {
                ByteBuffer byteBuffer = mVideoCodec.getInputBuffer(index);
                int length = nextFrameIndex - startIndex;
                byteBuffer.put(bytes, startIndex, length);
                mVideoCodec.queueInputBuffer(index, 0, length, 0, 0);
                startIndex = nextFrameIndex;
            }
            int outIndex = mVideoCodec.dequeueOutputBuffer(info, 10000);
            if (outIndex >= 0) {
                mVideoCodec.releaseOutputBuffer(outIndex, true);
            }
        }
    }

    private byte[] getFileByte(int size) throws Exception {
        InputStream inputStream = null;
        if (mVideoPath == "") {
            AssetManager assetManager = this.mContext.getAssets();
            inputStream = assetManager.open("test.h264");
        } else {
            inputStream = new DataInputStream(new FileInputStream(mVideoPath));
        }
        int len = 0;
        byte[] buf;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        buf = new byte[size];
        while ((len = inputStream.read(buf, 0, size)) != -1) {
            byteArrayOutputStream.write(buf, 0, len);
            buf = byteArrayOutputStream.toByteArray();
        }

        return buf;
    }

    private int findFrame(byte[] bytes, int startIndex, int totalSize) {
        for (int i = startIndex; i < totalSize - 4; i++) {
            if ((bytes[i] == 0x00 && bytes[i + 1] == 0x00 && bytes[i + 2] == 0x00 && bytes[i + 3] == 0x01) ||
                    (bytes[i] == 0x00 && bytes[i + 1] == 0x00 && bytes[i + 2] == 0x01)) {
                return i;
            }
        }
        return -1;
    }
}
