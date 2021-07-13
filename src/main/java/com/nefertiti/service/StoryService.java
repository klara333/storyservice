package com.nefertiti.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.nefertiti.domain.Story;
import com.nefertiti.repository.StoryRepository;

@Service
public class StoryService {

	private StoryRepository storyRepo;
	
	public StoryService(StoryRepository storyRepo) {
		this.storyRepo = storyRepo;
	}

	public List<Story> getStories() {
		return storyRepo.findAll();
	}
	
	public Story getStory(){
		return storyRepo.findFirstByOrderByPostedDesc();
	}

	public Story getSpecificStory(String title) {
		return storyRepo.findByTitle(title);
	}

	public void save(Story story) {
		storyRepo.save(story);
	}
}