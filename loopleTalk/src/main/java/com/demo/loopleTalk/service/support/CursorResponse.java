package com.demo.loopleTalk.service.support;

import java.util.List;

public record CursorResponse<T>(
	CursorRequest nextCursorRequest,
	List<T> contents

) {

}
