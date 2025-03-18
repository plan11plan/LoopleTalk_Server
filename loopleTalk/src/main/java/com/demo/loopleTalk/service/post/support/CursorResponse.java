package com.demo.loopleTalk.service.post.support;

import java.util.List;

public record CursorResponse<T>(
	CursorRequest nextCursorRequest,
	List<T> contents

) {

}
