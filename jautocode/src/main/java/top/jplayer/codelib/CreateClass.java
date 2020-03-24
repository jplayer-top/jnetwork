package top.jplayer.codelib;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;

import javax.lang.model.element.Modifier;

public class CreateClass {
    public static void main(String[] args) {
        ClassName activity = ClassName.get("android.app", "Activity");

        TypeSpec.Builder mainActivityBuilder = TypeSpec.classBuilder("MainActivity")
                .addModifiers(Modifier.PUBLIC)
                .superclass(activity);

        ClassName override = ClassName.get("java.lang", "Override");

        ClassName bundle = ClassName.get("android.os", "Bundle");

        ClassName nullable = ClassName.get("android.support.annotation", "Nullable");

        ParameterSpec savedInstanceState = ParameterSpec.builder(bundle, "savedInstanceState")
                .addAnnotation(nullable)
                .build();

        MethodSpec onCreate = MethodSpec.methodBuilder("onCreate")
                .addAnnotation(override)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(savedInstanceState)
                .addStatement("super.onCreate(savedInstanceState)")
                .addStatement("setContentView(R.layout.activity_main)")
                .build();

        TypeSpec mainActivity = mainActivityBuilder.addMethod(onCreate)
                .build();

        JavaFile javaFile = JavaFile.builder("com.test", mainActivity).build();
        File file = new File("app/src/main/java");
        try {
            javaFile.writeTo(file);
            System.out.println(file.getAbsolutePath());
            System.out.println(file.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
