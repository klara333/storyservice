package com.nefertiti.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.nefertiti.domain.Story;

@Repository
public interface StoryRepository extends CrudRepository<Story, Long> {

	//SELECT * FROM STORY
	List<Story> findAll();
	
	//SELECT * FROM STORY WHERE posted IN (SELECT max(posted) FROM story) LIMIT 1;
	Story findFirstByOrderByPostedDesc();

	Story findByTitle(String title);
}
