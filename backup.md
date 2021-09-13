* **thymeleaf模板使用踩坑**：
    %input标签与后端做交互时，与%textarea需要输入的位置不一样：
    
    一个是th:value=，另一个是th:text="${description}

```html
<!-- -->
<input type="text" class="form-control" id="tag" name="tag" th:value="${tag}" placeholder="创建或搜索添加新话题">

<textarea class="form-control" name="description" th:text="${description}" id="description" cols="30" rows="10"></textarea>
```

thymeleaf模板可以将多个页面公用的元素封装到一个html文件中，在需要用到的地方进行调用，类似于调用Java类文件中的某个方法。

