package org.codecrafterslab;

import java.io.File;

public interface ProjectInfo {

    boolean isRootProject();

    File getDir();

    String getBuildFileName();

    String getPath();

    String getName();

}
