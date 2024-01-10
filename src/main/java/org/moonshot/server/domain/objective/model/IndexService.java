package org.moonshot.server.domain.objective.model;

import org.moonshot.server.domain.objective.dto.request.ModifyIndexRequestDto;

public interface IndexService {

    void modifyIdx(ModifyIndexRequestDto request, Long userId);

}
