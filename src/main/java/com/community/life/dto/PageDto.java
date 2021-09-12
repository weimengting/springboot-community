package com.community.life.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDto {
    private List<QuestionDto> questionDtos;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer totalPage;
    private Integer page;
    private List<Integer> pages;

    public void setPageDto(Integer page, Integer totalPage){
        System.out.println(page);
        this.page = page;
        this.totalPage = totalPage;
        pages = new ArrayList<>();
        pages.add(page);  // 添加当前页面
        for (int i = 1; i <= 3; i ++){
            if (page - i > 0){
                pages.add(0, page - i);
            }
            if (page + i <= totalPage){
                pages.add(page + i);
            }
        }
        System.out.println(pages);
        //是否展示上一页
        showPrevious = page != 1;
        //是否展示下一页
        showNext = !page.equals(totalPage);
        //是否展示第一页
        showFirstPage = !pages.contains(1);
        //是否展示最后一页
        showEndPage = !pages.contains(totalPage);
    }
}
