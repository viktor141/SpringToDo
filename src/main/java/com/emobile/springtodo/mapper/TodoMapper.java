package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.TodoRequestDto;
import com.emobile.springtodo.dto.TodoResponseDto;
import com.emobile.springtodo.model.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
    @Mapping(target = "updatedAt", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
    Todo toEntity(TodoRequestDto dto);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "timestampToString")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "timestampToString")
    TodoResponseDto toDto(Todo todo);

    @org.mapstruct.Named("timestampToString")
    default String timestampToString(Timestamp timestamp) {
        if (timestamp == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(timestamp);
    }
}
