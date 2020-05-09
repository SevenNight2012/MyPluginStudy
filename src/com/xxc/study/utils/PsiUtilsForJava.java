package com.xxc.study.utils;

import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.kotlin.asJava.LightClassUtil;
import org.jetbrains.kotlin.idea.refactoring.fqName.FqNameUtilKt;
import org.jetbrains.kotlin.name.FqName;
import org.jetbrains.kotlin.nj2k.postProcessing.UtilsKt;
import org.jetbrains.kotlin.psi.KtFunction;
import org.jetbrains.kotlin.psi.KtNameReferenceExpression;
import org.jetbrains.kotlin.psi.KtNamedFunction;
import org.jetbrains.kotlin.psi.KtReferenceExpression;

public class PsiUtilsForJava {

    public static PsiMethod psiMethod(PsiElement element) {
        if (element instanceof PsiMethod) return (PsiMethod) element;
        else if (element instanceof KtNamedFunction) {
            return LightClassUtil.INSTANCE.getLightClassMethod((KtFunction) element);
        }
        return null;
    }

    public static boolean isLaunch(PsiElement element) {
        if (element instanceof PsiCallExpression) {
            FqName fqName = FqNameUtilKt.getKotlinFqName(element);
            if (fqName != null) {
                String string = fqName.asString();
                System.out.println(string);
                if (string.contains(".launch(")) {
                    return true;
                }
            }
        } else if (element instanceof KtNameReferenceExpression) {
            PsiElement resolve = UtilsKt.resolve((KtReferenceExpression) element);
            if (resolve == null) {
                return false;
            }
            FqName kotlinFqName = FqNameUtilKt.getKotlinFqName(resolve);
            if (kotlinFqName != null) {
                String string = kotlinFqName.asString();
                System.out.println(string);
                if (string.contains(".launch(")) {
                    return true;
                }
            }
        }
        return false;
    }
}
