plugins {
}

dependencies {
    implementation(platform("org.codecrafterslab.unity:dependencies"))
    optional(project(":dict-core"))
    optional("org.mybatis.spring.boot:mybatis-spring-boot-starter")
    optional("com.baomidou:mybatis-plus-boot-starter")
}
