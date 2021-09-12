## 社区项目搭建

### 资源

[Spring web 文档](https://spring.io/guides/gs/serving-web-content/)

[Spring 文档](https://spring.io/)

[OkHttp](https://square.github.io/okhttp/)

[maven工具包](https://mvnrepository.com/)

### 工具

[git](https://git-scm.com/downloads/)

[bootstrap](https://v3.bootcss.com/)

[Visual Paradigm](https://www.visual-paradigm.com/cn/download/community.jsp)

### 脚本

```sql
CREATE TABLE "PUBLIC"."USER"(
    ID int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ACCOUNT_ID VARCHAR(100),
    NAME VARCHAR(50),
    TOKEN CHAR(36),
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT
)

ALTER TABLE USER
	ADD BIO VARCHAR(256);
```

**踩坑记录于[log](./log)文件**

