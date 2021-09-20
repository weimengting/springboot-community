package com.community.life.service;

import com.community.life.bean.*;
import com.community.life.dto.NotificationDto;
import com.community.life.dto.PageDto;
import com.community.life.dto.QuestionDto;
import com.community.life.enums.NotificationEnum;
import com.community.life.exception.CustomizeErrorCode;
import com.community.life.exception.CustomizeException;
import com.community.life.mapper.NotificationMapper;
import com.community.life.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PageDto list(Integer userId, Integer page, Integer size) {
        PageDto<NotificationDto> pageDto = new PageDto<>();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);

        Integer totalPage;
        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }
        else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1){
            page = 1;
        }
        if (page > totalPage){
            page = totalPage;
        }
        Integer offset = size * (page - 1);

        //返回收件人是userId的所有通知
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        List<Notification> notifications =
                notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        if (notifications.isEmpty()){
            return pageDto;
        }
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDto notificationDto = new NotificationDto();
            BeanUtils.copyProperties(notification, notificationDto);
            notificationDto.setTypeName(NotificationEnum.nameOfType(notification.getType()));
            notificationDtos.add(notificationDto);
        }

        pageDto.setData(notificationDtos);
        pageDto.setPageDto(page, totalPage);
        return pageDto;
    }

    public Long unreadCount(Integer id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        long num = notificationMapper.countByExample(notificationExample);
        return num;
    }

    public NotificationDto read(Integer id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        Integer receiver = notification.getReceiver();
        if (!user.getId().equals(receiver)){  //防止用户在路径中输入错误的通知id
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        NotificationDto notificationDto = new NotificationDto();
        BeanUtils.copyProperties(notification, notificationDto);
        notificationDto.setTypeName(NotificationEnum.nameOfType(notification.getType()));
        return notificationDto;
    }
}
