package com.thesis.BackendApp.repository;

import com.thesis.BackendApp.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository  extends JpaRepository<Block, Long> {
}
