package top.jplayer.networklibrary.net.retrofit;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Obl on 2018/8/7.
 * top.jplayer.baseprolibrary.net.retrofit
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class FileConverterFactory extends Converter.Factory {

    public static FileConverterFactory create() {
        return new FileConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type instanceof ResponseBody) {
            return new FileConverter();
        } else {
            return null;
        }
    }

    public class FileConverter implements Converter<ResponseBody, ResponseBody> {


        @Override
        public ResponseBody convert(@NonNull ResponseBody value) throws IOException {
            return value;
        }

    }
}
