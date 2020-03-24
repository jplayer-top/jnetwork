package top.jplayer.networklibrary.net.retrofit;

import androidx.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Administrator on 2018/7/27.
 * 重构 Response Body实现 进度监听
 */

public class ProgressResponseBody extends ResponseBody {
    //实际的待包装响应体
    private final ResponseBody responseBody;
    //进度回调接口
    private final ProgressUpDownListener mListener;
    //包装完成的BufferedSource
    private BufferedSource bufferedSource;

    /**
     * 构造函数，赋值
     *
     * @param responseBody     待包装的响应体
     * @param progressListener 回调接口
     */
    public ProgressResponseBody(ResponseBody responseBody, ProgressUpDownListener progressListener) {
        this.responseBody = responseBody;
        this.mListener = progressListener;
    }


    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     */
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    /**
     * 重写进行包装source
     *
     * @return BufferedSource
     */
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            //包装
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 读取，回调进度接口
     *
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {

        return new ForwardingSource(source) {
            //当前读取字节数
            long progress = 0L;

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                final long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                progress += bytesRead != -1 ? bytesRead : 0;
                long total = responseBody.contentLength();
                if (mListener != null) {
                    Log.e("NetWork", progress + "---/---" + total);
                    boolean done = bytesRead == -1;
                    mListener.onResponseProgress(progress, total, done);
                }
                return bytesRead;
            }
        };
    }

}
