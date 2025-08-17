package com.ebook.userservice.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelMapper modelMapper;

    public <S,D> D map(S source, Class<D> destination){
        return this.modelMapper.map(source, destination);
    }

    public <S,D> List<D> map(List<S> source, Class<D> destination){
        return source.stream().map(s -> this.map(s, destination)).toList();
    }

}