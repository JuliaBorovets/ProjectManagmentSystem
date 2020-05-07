package com.training.demo.service;

import com.training.demo.entity.ArtfRole;
import com.training.demo.repository.ArtfRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtfRoleService {
    private final ArtfRoleRepository artfRoleRepository;

    public ArtfRoleService(ArtfRoleRepository artfRoleRepository)
    {
        this.artfRoleRepository = artfRoleRepository;
    }

    public ArtfRole findArtfRoleById(Long id){
        return artfRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("can not find"));
    }

    public List<ArtfRole> getAllProjects(){
        return (List<ArtfRole>) artfRoleRepository.findAll();
    }
}
