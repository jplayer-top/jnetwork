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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
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
import javax.lang.model.util.SimpleElementVisitor8;
import javax.lang.model.util.SimpleTypeVisitor8;

/**
 * Created by Obl on 2019/7/15.
 * top.jplayer.codelib
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
@AutoService(Processor.class)
public class AutoModelProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        // 添加了关注的注解
        types.add(AutoMP.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoMP.class);
        if (elements != null && elements.size() > 0) {
            Element oneElement = elements.iterator().next();
            AutoMP elementAnnotation = oneElement.getAnnotation(AutoMP.class);
            PackageElement packageOf = processingEnv.getElementUtils().getPackageOf(oneElement);
            TypeElement classElement = (TypeElement) oneElement.getEnclosingElement();

            ClassName ioMain = ClassName.get("top.jplayer.networklibrary.net.retrofit", "IoMainSchedule");

            ClassName baseModel = ClassName.get(elementAnnotation.baseModelName() + ".model", "BaseModel");
            ClassName basePresenter = ClassName.get(elementAnnotation.baseModelName() + ".contract", "BasePresenter");

            ClassName typeServer = ClassName.get(packageOf.getQualifiedName().toString(), classElement.getSimpleName().toString());
            ParameterizedTypeName name = ParameterizedTypeName.get(baseModel, typeServer);
            ParameterizedTypeName namePresenter = ParameterizedTypeName.get(basePresenter, TypeVariableName.get("T"));
            ClassName typeIView = ClassName.get(elementAnnotation.baseModelName() + ".contract.IContract", "IView");
            ClassName typeField = ClassName.get(packageOf.toString(), "CommonModel$Auto");

            ParameterizedTypeName typeConstructor = ParameterizedTypeName.get(ClassName.get(Class.class), typeServer);
            ParameterSpec constructorParam = ParameterSpec.builder(typeConstructor, "t").build();
            MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(constructorParam)
                    .addStatement("super(t)")
                    .build();
            MethodSpec constructorPresenter = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeVariableName.get("T"), "t")
                    .addStatement("super(t)")
                    .addCode(CodeBlock.builder()
                            .add("mModel = ")
                            .add("new $T", typeField)
                            .addStatement("($T.class)", typeServer)
                            .build())
                    .build();

            LinkedHashSet<MethodSpec> methodSpecs = new LinkedHashSet<>();
            LinkedHashSet<MethodSpec> methodSpecsPresenter = new LinkedHashSet<>();
            for (Element forElemenet : elements) {
                forElemenet.accept(new SimpleElementVisitor7<Void, Void>() {
                    @Override
                    public Void visitType(TypeElement typeElement, Void aVoid) {
                        System.out.println("typeElement");
                        return super.visitType(typeElement, aVoid);
                    }

                    @Override
                    public Void visitExecutable(ExecutableElement executableElement, Void aVoid) {
                        List<? extends VariableElement> parameters = executableElement.getParameters();
                        LinkedHashSet<ParameterSpec> parameterSpecs = new LinkedHashSet<>();
                        ArrayList<String> strings = new ArrayList<>();
                        for (VariableElement parameter : parameters) {
                            parameterSpecs.add(ParameterSpec.get(parameter));
                            strings.add(parameter.getSimpleName().toString());
                        }
                        DeclaredType returnType = (DeclaredType) executableElement.getReturnType();
                        CodeBlock.Builder builder = CodeBlock.builder()
                                .add("return ")
                                .add("mServer.")
                                .add("$L", executableElement.getSimpleName().toString());
                        if (strings.size() > 0) {
                            builder.add("(");
                            for (int i = 0; i < strings.size(); i++) {
                                builder.add("$L", strings.get(i));
                                if (i < strings.size() - 1) {
                                    builder.add(",");
                                }
                            }
                            builder.add(").");
                        } else {
                            builder.add("().");
                        }
                        CodeBlock codeBlock = builder.add("compose(new $T<>());", ioMain).build();
                        MethodSpec methodSpec = MethodSpec
                                .methodBuilder(executableElement.getSimpleName().toString())
                                .addModifiers(Modifier.PUBLIC)
                                .addParameters(parameterSpecs)
                                .returns(ClassName.get(returnType))
                                .addCode(codeBlock)
                                .build();
                        methodSpecs.add(methodSpec);

                        CodeBlock.Builder builderPresenter = CodeBlock.builder()
                                .add("mModel.")
                                .add("$L", executableElement.getSimpleName().toString());
                        if (strings.size() > 0) {
                            builderPresenter.add("(");
                            for (int i = 0; i < strings.size(); i++) {
                                builderPresenter.add("$L", strings.get(i));
                                if (i < strings.size() - 1) {
                                    builderPresenter.add(",");
                                }
                            }
                            builderPresenter.add(")");
                        } else {
                            builderPresenter.add("()");
                        }
                        ClassName observer = ClassName.get(elementAnnotation.callBackName(), "DefaultCallBackObserver");
                        TypeMirror typeMirror = returnType.getTypeArguments().get(0);
                        TypeName bean = ClassName.get(typeMirror);
                        CodeBlock codeBlockPresenter = builderPresenter
                                .add(".subscribe(")
                                .add(CodeBlock.builder()
                                        .add("new $T", observer)
                                        .add("<$T>", bean)
                                        .add("(this)")
                                        .beginControlFlow("")
                                        .add("$L", MethodSpec.methodBuilder("responseSuccess")
                                                .addModifiers(Modifier.PUBLIC)
                                                .addStatement("//responseSuccess")
                                                .addParameter(bean, "bean")
                                                .build())
                                        .add("$L", MethodSpec.methodBuilder("responseFail")
                                                .addModifiers(Modifier.PUBLIC)
                                                .addStatement("//responseFail")
                                                .addParameter(bean, "bean")
                                                .build())
                                        .endControlFlow(")")
                                        .build())
                                .build();

                        MethodSpec methodSpecPresenter = MethodSpec
                                .methodBuilder(executableElement.getSimpleName().toString())
                                .addModifiers(Modifier.PUBLIC)
                                .addParameters(parameterSpecs)
                                .addCode(codeBlockPresenter)
                                .build();
                        methodSpecsPresenter.add(methodSpecPresenter);
                        return super.visitExecutable(executableElement, aVoid);
                    }

                    @Override
                    public Void visitPackage(PackageElement packageElement, Void aVoid) {
                        System.out.println("packageElement");
                        return super.visitPackage(packageElement, aVoid);
                    }

                }, null);

            }
            //model
            TypeSpec classBuild = TypeSpec.classBuilder(elementAnnotation.modelName())
                    .addModifiers(Modifier.PUBLIC)
                    .superclass(name)
                    .addMethod(constructor)
                    .addMethods(methodSpecs)
                    .build();

            FieldSpec fieldSpec = FieldSpec.builder(typeField, "mModel", Modifier.PUBLIC).build();
            //presenter
            TypeSpec classBuildPresenter = TypeSpec.classBuilder(elementAnnotation.presenterName())
                    .addModifiers(Modifier.PUBLIC)
                    .addTypeVariable(TypeVariableName.get("T", typeIView))
                    .superclass(namePresenter)
                    .addMethod(constructorPresenter)
                    .addField(fieldSpec)
                    .addMethods(methodSpecsPresenter)
                    .build();

            JavaFile javaFile = JavaFile.builder(packageOf.getQualifiedName().toString(), classBuild).build();

            JavaFile javaFilePresenter = JavaFile.builder(packageOf.getQualifiedName().toString(), classBuildPresenter).build();
            try {
                Filer filer = processingEnv.getFiler();
                javaFile.writeTo(filer);
                javaFilePresenter.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        return true;
    }

}
