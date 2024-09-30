package com.Java_instagram_clone.cmn;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class EntityToDtoService {

  private final ModelMapper modelMapper = new ModelMapper();

  public <D, T> D convertToDto(T entity, Class<D> dtoClass) {
    return modelMapper.map(entity, dtoClass);
  }
}
