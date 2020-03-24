package top.jplayer.codelib;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleElementVisitor7;

/**
 * Created by Obl on 2019/7/15.
 * top.jplayer.codelib
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
@AutoService(Processor.class)
public class AutoWiredProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        // 添加了关注的注解
        types.add(AutoWired.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoWired.class);
        if (elements != null && elements.size() > 0) {
            Element oneElement = elements.iterator().next();
            AutoWired elementAnnotation = oneElement.getAnnotation(AutoWired.class);
            PackageElement packageOf = processingEnv.getElementUtils().getPackageOf(oneElement);

            TypeElement classElement = (TypeElement) oneElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();
            System.out.println(packageOf);
            System.out.println(fullClassName);
            LinkedHashSet<MethodSpec> methodSpecs = new LinkedHashSet<>();
            MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.get(classElement.asType()), "targer")
                    .addCode("super(targer)")
                    .addStatement("")
                    .addCode("this.targer = targer")
                    .addStatement("");
            MethodSpec.Builder iBindBuilder = MethodSpec.methodBuilder("unbind")
                    .addModifiers(Modifier.PUBLIC);
            for (Element forElemenet : elements) {
                forElemenet.accept(new SimpleElementVisitor7<Void, Void>() {
                    @Override
                    public Void visitType(TypeElement typeElement, Void aVoid) {
                        System.out.println("typeElement");
                        return super.visitType(typeElement, aVoid);
                    }

                    @Override
                    public Void visitVariable(VariableElement variableElement, Void aVoid) {
                        System.out.println("variableElement");
                        TypeMirror mirror = variableElement.asType();
                        System.out.println(mirror);
                        PackageElement packageOf = processingEnv.getElementUtils().getPackageOf(oneElement);
                        System.out.println(packageOf);

                        String longName = mirror.toString();
                        String sortName = longName.substring(longName.lastIndexOf(".") + 1);
                        CodeBlock build = CodeBlock.builder()
                                .add("return ")
                                .add("new ")
                                .add("$L", sortName)
                                .add("(targer)")
                                .addStatement("")
                                .build();
                        MethodSpec methodSpec = MethodSpec
                                .methodBuilder("get" + sortName)
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(ParameterSpec.builder(TypeName.get(classElement.asType()), "targer").build())
                                .returns(ClassName.get(variableElement.asType()))
                                .addCode(build)
                                .build();
                        CodeBlock buildSet = CodeBlock.builder()
                                .add("targer." + variableElement.getSimpleName())
                                .add(" = new ")
                                .add("$L", sortName)
                                .add("(targer)")
                                .addStatement("")
                                .build();
                        MethodSpec methodSpecSet = MethodSpec
                                .methodBuilder("set" + sortName)
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(ParameterSpec.builder(TypeName.get(classElement.asType()), "targer").build())
                                .addCode(buildSet)
                                .build();
                        constructorBuilder.addCode(
                                CodeBlock.builder()
                                        .add("set" + sortName + "(targer)")
                                        .addStatement("")
                                        .build());
                        iBindBuilder.addCode(
                                CodeBlock.builder()
                                        .add("targer." + variableElement.getSimpleName() + ".detachView()")
                                        .addStatement("")
                                        .build());
                        methodSpecs.add(methodSpec);
                        methodSpecs.add(methodSpecSet);
                        return super.visitVariable(variableElement, aVoid);
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

            MethodSpec constructor = constructorBuilder.build();
            MethodSpec unBind = iBindBuilder.build();
            ClassName iBind = ClassName.get("top.jplayer.codelib", "IBind");
            ParameterizedTypeName bindSuper = ParameterizedTypeName.get(iBind,
                    TypeVariableName.get(classElement.asType()));
            TypeSpec classBuild =
                    TypeSpec.classBuilder(classElement.getSimpleName().toString() + "$Auto")
                            .superclass(bindSuper)
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(constructor)
                            .addMethods(methodSpecs)
                            .addMethod(unBind)
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
