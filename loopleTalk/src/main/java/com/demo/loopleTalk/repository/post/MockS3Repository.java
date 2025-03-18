package com.demo.loopleTalk.repository.post;

import org.springframework.stereotype.Repository;

@Repository
public class MockS3Repository implements S3Repository {
	private final String MOCK_FILE_URL = "http://image";

	@Override
	public String getFileUrl(Long postId) {
		return MOCK_FILE_URL;
	}
}
