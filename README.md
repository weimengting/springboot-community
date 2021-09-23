## 社区项目搭建

### 简介

一个简化版的[elastic中文社区](https://elasticsearch.cn/)，功能包括：获取gitee授权登录；发起问题；回复问题；回复评论等一些交流社区网站通用的功能。

### 版本

都在[pom](./pom.xml)里

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

[json在线编辑](http://jsoneditoronline.cn/)

[开源在线 Markdown 编辑器](https://pandao.github.io/editor.md/)

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
		
CREATE TABLE notification(
	id INT AUTO_INCREMENT PRIMARY KEY,
	notifier INT NOT NULL DEFAULT 0,
	receiver INT NOT NULL DEFAULT 0,
	outerId INT NOT NULL DEFAULT 0,
	TYPE INT NOT NULL,
	gmtCreate BIGINT NOT NULL,
	gmtModified BIGINT NOT NULL,
	STATUS INT NOT NULL)CHARSET=utf8;

```

```bash
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```

### 问题（待完善部分）

1. ~~如果用户没有发起提问，现有的程序会抛出异常~~ 

   已解决

2. Cross-Origin Read Blocking (CORB)加载editor.md的时候出现跨域获取资源失败
