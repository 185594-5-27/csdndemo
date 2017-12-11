package com.csdn.demo.sys.mapper;


import com.csdn.demo.sys.dto.TreeDTO;
import com.csdn.demo.sys.entity.Tree;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
@Mapper(componentModel = "spring")
public interface TreeMapper {

    @Mapping(source = "id", target = "id")
    TreeDTO treeToTreeDTO(Tree entity);

    List<TreeDTO> treesToTressDTOs(List<Tree> treeList);

    TreeMapper INSTANCE = Mappers.getMapper(TreeMapper.class);

}
