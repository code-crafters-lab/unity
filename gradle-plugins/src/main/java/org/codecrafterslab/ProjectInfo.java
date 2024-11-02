package org.codecrafterslab;

import java.io.File;

public interface ProjectInfo {

    boolean isRootProject();

    String getBuildFileName();

    File getDir();

    String getName();

    String getPath();

}
