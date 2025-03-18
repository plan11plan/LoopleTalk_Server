package com.demo.loopleTalk.dto.post;

public record NearestPostResponse(
	SinglePostResponse postResponse,
	double distance
) {
}
