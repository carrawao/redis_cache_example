package com.redis.rosebowl.service;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis.rosebowl.entity.RoseBowl;
import com.redis.rosebowl.repository.RoseBowlRepository;

@Service
public class RoseBowlServiceImpl implements RoseBowlService {
	@Autowired
	private RoseBowlRepository rosebowlRepository;

	// Save operation
	@Override
	public RoseBowl saveRoseBowl(RoseBowl rosebowl) {
		return rosebowlRepository.save(rosebowl);
	}

	// Read operation
	@Override
	public List<RoseBowl> fetchRoseBowlRecords() {
		return (List<RoseBowl>) rosebowlRepository.fetchRoseBowlRecords();
	}

	@Override
	public RoseBowl fetchRoseBowlByDate(Long date) {
		return (RoseBowl) rosebowlRepository.fetchRoseBowlByDate(date);
	}

	// Update operation
	@Override
	public RoseBowl updateRoseBowl(Long date, RoseBowl rosebowl) {
		RoseBowl depDB = rosebowlRepository.findById(date).get();

		if (Objects.nonNull(rosebowl.getDate())) {
			depDB.setDate(rosebowl.getDate());
		}

		if (Objects.nonNull(rosebowl.getWinner()) && !"".equalsIgnoreCase(rosebowl.getWinner())) {
			depDB.setWinner(rosebowl.getWinner());
		}

		if (Objects.nonNull(rosebowl.getOpponent()) && !"".equalsIgnoreCase(rosebowl.getOpponent())) {
			depDB.setOpponent(rosebowl.getOpponent());
		}

		return rosebowlRepository.save(depDB);
	}

	// Delete operation
	@Override
	public void deleteRoseBowlByDate(Long date) {
		rosebowlRepository.deleteById(date);
	}
}
