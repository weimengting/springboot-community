## 社区项目搭建

### 简介（更新ing）

一个简化版的[elastic中文社区](https://elasticsearch.cn/)，功能包括：获取gitee授权登录；发起问题；查看回复等一些交流社区网站通用的功能。

关于gitee授权，初衷是想使用GitHub授权的，无奈国内网访问GitHub总是出现超时的问题，苦于找不到优秀的梯子，在几经周折后作罢，虽然gitee有时也会出现访问超时的情况，但比GitHub稳定得多。

### 版本

都写在[pom](./pom.xml)里，不详细列举了

### 资源

[Spring web 文档](https://spring.io/guides/gs/serving-web-content/)

[Spring 文档](https://spring.io/)

[OkHttp](https://square.github.io/okhttp/)

[spring-boot集成mybatis](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)

[mybatis自动生成crud](http://mybatis.org/generator/index.html)

### 工具

[git](https://git-scm.com/downloads/)

[bootstrap](https://v3.bootcss.com/)

[maven工具包](https://mvnrepository.com/)

### 脚本

```sql
SET GLOBAL time_zone='+8:00'

CREATE TABLE `user`(
	id INT PRIMARY KEY AUTO_INCREMENT,
	accountId VARCHAR(100),
	`name` VARCHAR(50),
	token CHAR(36),
	gmtCreate BIGINT,
	gmtModified BIGINT)CHARSET=utf8;
SELECT * FROM USER

CREATE TABLE question(
		id INT PRIMARY KEY AUTO_INCREMENT,
		title VARCHAR(50),
		description TEXT,
		gmtCreate BIGINT,
		gmtModified BIGINT,
		creator INT,
		commentCount INT DEFAULT 0,
		viewCount INT DEFAULT 0,
		likeCount INT DEFAULT 0,
		tag VARCHAR(256))CHARSET=utf8
SELECT * FROM question

ALTER TABLE USER ADD avatarUrl VARCHAR(100)
DELETE FROM USER WHERE avatarUrl IS NULL
```

### 问题（待完善部分）

1. 如果用户没有发起提问，现有的程序会抛出异常

   profile.html页面需要加一个判断语句

2. 如果退出登陆后想使用别的用户信息登录怎么办？对客户端来说可以通过清除浏览器的cookie，能不能通过设置服务器来完成这个要求？（不清空cookie，浏览器中会存有使用过的授权信息）

### 致谢

[**@cambridgejames**](https://github.com/cambridgejames)
