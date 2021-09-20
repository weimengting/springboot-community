package com.community.life.cache;

import com.community.life.dto.TagDto;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDto> get(){
        List<TagDto> tagDtos = new ArrayList<>();
        TagDto program = new TagDto();
        program.setCategoryName("技术");
        program.setTags(Arrays.asList("php", "java", "python", "css", "tomcat", "maven", "Spring", "Spring MVC",
                "SpringBoot", "MyBatis", "JPA", "Spring Security"));
        tagDtos.add(program);
        TagDto life = new TagDto();
        life.setCategoryName("生活");
        life.setTags(Arrays.asList("日常", "都市", "乡村", "政治", "文化", "艺术", "宗教", "民俗"));
        tagDtos.add(life);
        TagDto ml = new TagDto();
        ml.setCategoryName("机器学习");
        ml.setTags(Arrays.asList("逻辑回归二分类问题", "逻辑回归模型", "梯度下降法", "计算图及其求导", "向量化"
                , "jupyter使用", "logistic损失函数"));
        tagDtos.add(ml);
        TagDto dl = new TagDto();
        dl.setCategoryName("深度学习");
        dl.setTags(Arrays.asList("嵌入矩阵", "学习方法", "Word2vec", "负采样", "GloVe", "情感分类任务"));
        tagDtos.add(dl);
        TagDto models = new TagDto();
        models.setCategoryName("模型");
        models.setTags(Arrays.asList("Word", "Excel", "PPT", "PDF阅读", "PDF编辑", "PDF转换"
                , "PDF压缩", "思维导图", "在线设计", "OCR文字识别", "快速搜索", "文件快传"));
        tagDtos.add(models);
        return tagDtos;
    }

    public static String filterInValid(String tags){
        String[] split = StringUtils.split(tags, ",");
        List<TagDto> tagDtos = get();
        List<String> tagList = tagDtos.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String inValidString = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return inValidString;
    }
}
