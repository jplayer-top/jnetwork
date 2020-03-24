package top.jplayer.codelib;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor7;

/**
 * Created by Obl on 2019/7/15.
 * top.jplayer.codelib
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
@AutoService(Processor.class)
public class AutoResetProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        // 添加了关注的注解
        types.add(AutoResetUrl.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoResetUrl.class);
        if (elements != null && elements.size() > 0) {
            Element oneElement = elements.iterator().next();
            AutoResetUrl elementAnnotation = oneElement.getAnnotation(AutoResetUrl.class);
            PackageElement packageOf = processingEnv.getElementUtils().getPackageOf(oneElement);
            System.out.println(packageOf);

            TypeElement classElement = (TypeElement) oneElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();
            System.out.println(fullClassName);

            for (Element forElemenet : elements) {
                forElemenet.accept(new SimpleElementVisitor7<Void, Void>() {
                    @Override
                    public Void visitType(TypeElement typeElement, Void aVoid) {
                        System.out.println("typeElement");
                        return super.visitType(typeElement, aVoid);
                    }

                    @Override
                    public Void visitExecutable(ExecutableElement executableElement, Void aVoid) {
                        System.out.println("visitExecutable");
                        return super.visitExecutable(executableElement, aVoid);
                    }

                    @Override
                    public Void visitPackage(PackageElement packageElement, Void aVoid) {
                        System.out.println("packageElement");
                        return super.visitPackage(packageElement, aVoid);
                    }

                }, null);

            }
            TypeSpec classBuild = TypeSpec.classBuilder(classElement.getSimpleName().toString() + "$Reset")
                    .addModifiers(Modifier.PUBLIC)
                    .build();

            JavaFile javaFile = JavaFile.builder(packageOf.getQualifiedName().toString(), classBuild).build();
            try {
                Filer filer = processingEnv.getFiler();
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

        return true;
    }

}
