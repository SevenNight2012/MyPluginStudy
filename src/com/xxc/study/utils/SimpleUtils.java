package com.xxc.study.utils;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.search.JavaFilesSearchScope;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleUtils {

    public static List<PsiJavaFile> findProperties(Project project) {
        List<PsiJavaFile> result = new ArrayList<>();
//        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.allScope(project));
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(JavaFileType.INSTANCE,
                new JavaFilesSearchScope(project));

        for (VirtualFile virtualFile : virtualFiles) {
            PsiJavaFile simpleFile = (PsiJavaFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile == null) {
                continue;
            }
            String name = simpleFile.getName();
            if (name.endsWith("Router.java")) {
                System.out.println(">>> " + name);
                result.add(simpleFile);
            }
        }
        return result;
    }

    @Nullable
    public static PsiClass findClassInGlobalProject(String className, Project project) {
        return JavaPsiFacade.getInstance(project).findClass(className, GlobalSearchScope.allScope(project));
    }

    /**
     * 从Router类中获取Activity的类名
     *
     * @param routerClass Router类
     * @param project     Project对象
     * @return 常量类名
     */
    @Nullable
    public static String findNameInRouterClass(PsiClass routerClass, Project project) {
        if (null == routerClass) {
            return "## router class is null";
        }
        //从类中搜索名为Name的静态常量，不需要查找父类
        PsiField nameField = routerClass.findFieldByName("NAME", false);
        if (null == nameField) {
            return "## NAME is not found";
        }
        PsiExpression initializer = nameField.getInitializer();
        if (initializer != null) {
            return initializer.getText().trim().replaceAll("\"", "");
        }
        return "## NAME field expression is null";
    }
}
