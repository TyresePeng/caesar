# 使用基础镜像：openjdk 8 + Debian（支持 apt）
FROM openjdk:8-jdk

LABEL maintainer="tyrese"
WORKDIR /app

# 安装 curl、Node.js、Playwright 所需依赖
RUN apt-get update && \
    apt-get install -y curl wget gnupg tzdata ca-certificates \
                       fonts-liberation libappindicator3-1 libasound2 \
                       libatk-bridge2.0-0 libatk1.0-0 libcups2 libdbus-1-3 \
                       libgdk-pixbuf2.0-0 libnspr4 libnss3 libx11-xcb1 \
                       libxcomposite1 libxdamage1 libxrandr2 xdg-utils && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g playwright && \
    npx playwright install chromium --with-deps && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# 拷贝你的 Spring Boot 应用 JAR
COPY target/caesar-media-0.0.1-SNAPSHOT.jar app.jar

# 启动 Spring Boot 应用（使用 prod 配置）
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
