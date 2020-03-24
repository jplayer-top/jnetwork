package top.jplayer.codelib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Obl on 2019/7/15.
 * top.jplayer.codelib
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface AutoMP {
    String callBackName() default "top.jplayer.networklibrary.net.retrofit";

    String baseModelName() default "top.jplayer.networklibrary";

    String modelName() default "CommonModel$Auto";
    String presenterName() default "CommonPresenter$Auto";
}
