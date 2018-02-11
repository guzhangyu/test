package jvm.codecheck;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner6;
import javax.tools.Diagnostic;
import java.util.EnumSet;
import static javax.lang.model.element.Modifier.*;

/**
 * Created by guzy on 16/12/29.
 */
public class NameChecker {

    private Messager messager;

    NameCheckScanner nameCheckScanner=new NameCheckScanner();


    public NameChecker(ProcessingEnvironment processingEnv) {
        messager=processingEnv.getMessager();
    }

    public void check(Element e) {
        nameCheckScanner.scan(e);
    }

    private class NameCheckScanner extends ElementScanner6<Void,Void>{

        /**
         * 检查是否为驼峰
         * @param element
         * @param firstCap 首字母是否大写
         */
        private void checkCamelCase(Element element,Boolean firstCap){
            String name=element.getSimpleName().toString();
            char[]cs=name.toCharArray();
           if(firstCap == Character.isLowerCase(cs[0])){
               messager.printMessage(Diagnostic.Kind.WARNING,"首字母"+(firstCap?"必须":"不能")+"为大写",element);
           }

            for(int i=1;i<cs.length;i++){
                if(Character.isUpperCase(cs[i]) && Character.isUpperCase(cs[i-1])){
                    messager.printMessage(Diagnostic.Kind.WARNING,"写法不符合驼峰",element);
                    break;
                }
            }
        }

        /**
         * 检查是否全部为大写
         * @param e
         */
        private void checkAllCaps(VariableElement e) {
            String name=e.getSimpleName().toString();
            if(!name.equals(name.toUpperCase())){
                messager.printMessage(Diagnostic.Kind.WARNING,"必须全部为大写",e);
            }
        }

        @Override
        public Void visitType(TypeElement e, Void aVoid) {
            scan(e.getTypeParameters(),aVoid);
            checkCamelCase(e,true);
            return super.visitType(e, aVoid);
        }

        @Override
        public Void visitVariable(VariableElement e, Void aVoid) {
            if(e.getKind() == ElementKind.ENUM_CONSTANT || e.getConstantValue()!=null || heuristicallyConstant(e)){
                checkAllCaps(e);
            }else{
                checkCamelCase(e,false);
            }
            return super.visitVariable(e, aVoid);
        }

        private Boolean heuristicallyConstant(VariableElement e){
            if(e.getEnclosingElement().getKind()==ElementKind.INTERFACE){
                return true;
            }else if(e.getKind()==ElementKind.FIELD && e.getModifiers().containsAll(EnumSet.of(PUBLIC,STATIC,FINAL))){
                return true;
            }else{
                return false;
            }
        }


        @Override
        public Void visitExecutable(ExecutableElement e, Void aVoid) {
            if(e.getKind()==ElementKind.METHOD){
                Name name=e.getSimpleName();
                if(name.contentEquals(e.getEnclosingElement().getSimpleName())){
                    messager.printMessage(Diagnostic.Kind.WARNING,"一个普通方法 "+name+" 不应当与类名重复，避免与构造函数产生混淆",e);
                }
                checkCamelCase(e,false);
            }
            return super.visitExecutable(e, aVoid);
        }

        @Override
        public Void visitTypeParameter(TypeParameterElement e, Void aVoid) {
            return super.visitTypeParameter(e, aVoid);
        }
    }
}
