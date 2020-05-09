package com.xxc.study;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.xxc.study.utils.PsiUtils;
import com.xxc.study.utils.SimpleUtils;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FindRouter implements LineMarkerProvider {

    public static final List<String> ALL_ROUTERS = new ArrayList<>();

    static {
        LocalFileSystem.getInstance().addVirtualFileListener(new VirtualFileListener() {
            @Override
            public void fileCreated(@NotNull VirtualFileEvent event) {
            }

            @Override
            public void fileDeleted(@NotNull VirtualFileEvent event) {

            }
        });
    }

    private final Icon ICON_NAV = IconLoader.getIcon("/icons/near_me.svg");

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        String routerClassName = PsiUtils.isRouterInvoke(element);
        if (TextUtils.isEmpty(routerClassName)) {
            return null;
        }
        Project project = element.getProject();
        PsiClass routerClass = SimpleUtils.findClassInGlobalProject(routerClassName, project);
        String nameValue = SimpleUtils.findNameInRouterClass(routerClass, project);

        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(ICON_NAV);
        PsiElement targetElement = element;
        if (TextUtils.isEmpty(nameValue)) {
            builder.setTooltipText("NAME field is empty");
        } else if (nameValue.startsWith("## ")) {
            builder.setTooltipText(nameValue);
        } else {
            String tipText = "Goto linked target~";
            targetElement = SimpleUtils.findClassInGlobalProject(nameValue, project);
            if (null == targetElement) {
                targetElement = element;
                tipText = "Class not found:" + nameValue;
            }
            builder.setTooltipText(tipText);
        }
        builder.setTargets(targetElement);
        return builder.createLineMarkerInfo(element);
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }
}
