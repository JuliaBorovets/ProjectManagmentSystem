package com.training.demo.service;

import com.training.demo.entity.Artifact;
import com.training.demo.repository.ArtifactRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtifactService {
    private final ArtifactRepository artifactRepository;

    public ArtifactService(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    public Artifact findArtifactById(Long id) {
        return artifactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("can not find"));
    }

    public List<Artifact> getAllProjects() {
        return (List<Artifact>) artifactRepository.findAll();
    }

    public void addArtifact(Artifact artifact) throws Exception {
        Artifact updatedArtifact = Artifact.builder()
                .name(artifact.getName())
                .type(artifact.getType())
                .content(artifact.getContent())
                .build();
        try {
            artifactRepository.save(updatedArtifact);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Помилка додавання артефакту");
        }
    }

    public void deleteArtifact(Artifact artifact) throws Exception {
        try {
            artifactRepository.delete(artifact);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Помилка видалення артефакту");
        }
    }


}
