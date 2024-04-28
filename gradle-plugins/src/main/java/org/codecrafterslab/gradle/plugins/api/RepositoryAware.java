package org.codecrafterslab.gradle.plugins.api;

import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URI;

/**
 * @author Wu Yujie
 * @since 0.7.0
 */
public interface RepositoryAware extends ProjectAware {

    /**
     * 添加 maven 仓库
     *
     * @param repositoryUrl 仓库地址
     * @param username      用户名
     * @param password      密码
     */
//    default void addMavenRepository(RepositoryHandler repositoryHandler, VersionRepositoryInfo repositoryInfo) {
//        repositoryHandler.maven(mavenArtifactRepository -> {
//            mavenArtifactRepository.setAllowInsecureProtocol(true);
//            Optional.of(info.getName()).ifPresent(mavenArtifactRepository::setName);
//            mavenArtifactRepository.setUrl(realUrl);
//            mavenArtifactRepository.credentials(credentials -> {
//                Optional.ofNullable(info.getUsername()).ifPresent(credentials::setUsername);
//                Optional.ofNullable(info.getPassword()).ifPresent(credentials::setPassword);
//            });
//        });
//    }

    /**
     * 添加 maven 仓库
     *
     * @param versionRepositoryInfo 仓库信息
     */
//    default void addMavenRepository(VersionRepositoryInfo versionRepositoryInfo) {
//        Optional.ofNullable(versionRepositoryInfo).ifPresent(info -> {
//            info.getUrls().entrySet().stream()
//                    .filter(versionEntry -> StringUtils.hasText(versionEntry.getValue()))
//                    .forEach(versionEntry -> {
//                        RepositoryHandler repositoryHandler = getProject().getRepositories();
//                        repositoryHandler.maven(mavenArtifactRepository -> {
//                            mavenArtifactRepository.setAllowInsecureProtocol(true);
//                            Optional.of(info.getName()).ifPresent(name -> {
//                                mavenArtifactRepository.setName(String.join("-", name,
//                                        versionEntry.getKey().getType()));
//                            });
//                            mavenArtifactRepository.setUrl(versionEntry.getValue());
//                            mavenArtifactRepository.credentials(credentials -> {
//                                Optional.ofNullable(info.getUsername()).ifPresent(credentials::setUsername);
//                                Optional.ofNullable(info.getPassword()).ifPresent(credentials::setPassword);
//                            });
//                        });
//                    });
//        });
//    }

    /**
     * 添加 maven 仓库
     *
     * @param repositoryInfoAction {@code Action<RepositoryInfo>}
     */
//    default void addMavenRepository(Action<RepositoryInfo> repositoryInfoAction) {
////        RepositoryInfo repositoryInfo = new MavenRepository(getProject());
////        repositoryInfoAction.execute(repositoryInfo);
////        addMavenRepository(repositoryInfo);
//    }

    /**
     * 添加阿里云效仓库
     *
     * @param aliYunRepositoryInfo AliYunRepositoryInfo
     */
//    default void addAliYunMavenRepository(AliYunRepositoryInfo aliYunRepositoryInfo) {
//        RepositoryHandler repositoryHandler = getProject().getRepositories();
//        RepositoryUtil.addAliYunRepository(repositoryHandler, aliYunRepositoryInfo);
//    }

    /**
     * 添加无需认证的 maven 仓库
     *
     * @param url 仓库地址
     */
    default void addMavenRepository(String url) {
//        addMavenRepository(url, null, null);
    }

    /**
     * 添加本地仓库地址
     *
     * @param localMavenRepository 本地路径
     */
    default void addMavenLocal(String localMavenRepository) {
        RepositoryHandler repositoryHandler = getProject().getRepositories();
        MavenArtifactRepository mavenLocal = repositoryHandler.mavenLocal();
        if (StringUtils.hasText(localMavenRepository)) {
            File file = new File(localMavenRepository);
            URI uri = file.toURI();
            if (file.exists() && !mavenLocal.getUrl().equals(uri)) {
                mavenLocal.setUrl(uri);
            }
        }
        repositoryHandler.add(mavenLocal);
    }

    /**
     * 添加中央仓库地址
     */
    default void addMavenCentral() {
        RepositoryHandler repositoryHandler = getProject().getRepositories();
        repositoryHandler.add(repositoryHandler.mavenCentral());
    }

}
