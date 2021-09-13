## 社区项目搭建

### 简介（更新ing）

一个简化版的[elastic中文社区](https://elasticsearch.cn/)，功能包括：获取gitee授权登录；发起问题；查看回复等一些交流社区网站通用的功能。

关于gitee授权的问题，初衷是想使用GitHub授权的，无奈国内网访问GitHub总是出现超时的问题，苦于找不到优秀的梯子，在几经周折后作罢，虽然gitee有时也会出现访问超时的情况，但比GitHub稳定得多。

## 版本

都写在[pom](./pom.xml)里，不详细列举了

### 资源

[Spring web 文档](https://spring.io/guides/gs/serving-web-content/)

[Spring 文档](https://spring.io/)

[OkHttp](https://square.github.io/okhttp/)

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

2. 通过userId来获取问题列表不太合适，如果清空浏览器cookie的话，再次登录时会添加一个新的cookie，并生成一个新的userId，而实际上用户是没有发生改变的，所以一个合理的做法是通过用户名来获取问题列表

### 致谢

非常感谢[**@cambridgejames**](https://github.com/cambridgejames)在学习过程中给予的帮助，很多情况下别人的一句话要比自己debug几个小时管用。

   
