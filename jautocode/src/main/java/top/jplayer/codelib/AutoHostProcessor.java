package top.jplayer.codelib;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
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
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.SimpleElementVisitor7;

import jdk.nashorn.internal.codegen.types.Type;

/**
 * Created by Obl on 2019/7/15.
 * top.jplayer.codelib
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
@AutoService(Processor.class)
public class AutoHostProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        // 添加了关注的注解
        types.add(AutoHost.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoHost.class);
        if (elements != null && elements.size() > 0) {
            Element oneElement = elements.iterator().next();
            AutoHost elementAnnotation = oneElement.getAnnotation(AutoHost.class);
            PackageElement packageOf = processingEnv.getElementUtils().getPackageOf(oneElement);
            System.out.println(packageOf);
            System.out.println(elementAnnotation.key());
            TypeElement classElement = (TypeElement) oneElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();
            System.out.println(fullClassName);
            CodeBlock.Builder staticBuilder = CodeBlock.builder();
            ArrayList<FieldSpec> fieldSpecs = new ArrayList<>();
            for (Element forElemenet : elements) {

                forElemenet.accept(new SimpleElementVisitor7<Void, Void>() {
                    @Override
                    public Void visitVariable(VariableElement variableElement, Void aVoid) {
                        AutoHost annotation = variableElement.getAnnotation(AutoHost.class);
                        System.out.println(annotation.key());
                        String fieldKey =
                                "\"url_header_host:\" + " + "\"" + annotation.key() + "\"";
                        String fieldValue = "\"url_header_host:\"+JNetServer." + annotation.key();
                        FieldSpec fieldSpecA = FieldSpec.builder(
                                String.class,
                                "HEADER_" + variableElement.getSimpleName(),
                                Modifier.PUBLIC,
                                Modifier.FINAL,
                                Modifier.STATIC)
                                .initializer(CodeBlock.builder()
                                        .add(fieldKey)
                                        .build())
                                .build();
                        staticBuilder.add("NetworkApplication.mHostMap.put(")
                                .add("\"$L\"", annotation.key())
                                .add(",")
                                .add("$L", "JNetServer." + variableElement.getSimpleName())
                                .add(")")
                                .addStatement("");
                        fieldSpecs.add(fieldSpecA);
                        return super.visitVariable(variableElement, aVoid);
                    }
                }, null);

            }
            ClassName application = ClassName.get("top.jplayer.networklibrary", "NetworkApplication");
            ClassName packageName = ClassName.get(packageOf.getQualifiedName().toString(),
                    classElement.getSimpleName().toString());
            FieldSpec fieldSpec = FieldSpec.builder(application, "application", Modifier.PUBLIC).build();
            FieldSpec fieldSpecPackage =
                    FieldSpec.builder(packageName, "packageName", Modifier.PUBLIC).build();
            TypeSpec classBuild =
                    TypeSpec.classBuilder("Header$HOST")
                            .addField(fieldSpec)
                            .addField(fieldSpecPackage)
                            .addFields(fieldSpecs)
                            .addStaticBlock(staticBuilder.build())
                            .addModifiers(Modifier.PUBLIC)
                            .build();

            JavaFile javaFile = JavaFile.builder("top.jplayer.codelib", classBuild).build();
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
