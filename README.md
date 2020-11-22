##  社区练习
##  资料
[Spring resource](https://spring.io/guides)
[video resource](https://www.bilibili.com/video/BV1r4411r7au?p=4)
[format resource](https://docs.github.com/en/free-pro-team@latest/developers/apps/creating-an-oauth-app)
[spring](https://docs.spring.io/spring-boot/docs/2.0.0.RC2/reference/htmlsingle/)
##  工具
[github](https://github.com/)
[mybatis](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/index.html)
[flyway](https://flywaydb.org/documentation/getstarted/firststeps/maven)


## 脚本
'''sql 
CREATE  TABLE USER(
    "ID" INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    "ACCOUNT_ID" VARCHAR(100),
    "NAME" VARCHAR(50),
    "TOKEN" CHAR(36),
    "GMT_CREATE" BIGINT,
    "GMT_MODIFIED" BIGINT
)

'''