package top.jplayer.codelib;

import java.lang.reflect.Constructor;

/**
 * Created by Obl on 2020/3/23.
 * top.jplayer.qyue.base
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public class AutoWiredBind {
    public static IBind bind(Object target) {
        System.out.println("-------------------------------------");
        Class<?> targetClass = target.getClass();
        String targetName = targetClass.getName();
        String autoName = targetName + "$Auto";
        try {
            Class<? extends IBind> bindingClass = (Class<? extends IBind>) targetClass.getClassLoader().loadClass(autoName);
            Constructor constructor = bindingClass.getDeclaredConstructor(target.getClass());
            IBind iBind = (IBind) constructor.newInstance(target);

            System.out.println(iBind.getClass().toString() + "------------");
            return iBind;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-------------------------------------");
        return new IBind<Object>(target){
            @Override
            public void unbind() {

            }
        };
    }
}
