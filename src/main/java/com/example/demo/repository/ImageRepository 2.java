package com.example.demo.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	Optional<Image> findByUserId(String userId);
}
