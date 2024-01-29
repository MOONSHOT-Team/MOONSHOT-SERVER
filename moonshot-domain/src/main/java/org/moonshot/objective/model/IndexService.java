package org.moonshot.objective.model;


import org.moonshot.objective.dto.request.ModifyIndexRequestDto;

public interface IndexService {

    void modifyIdx(ModifyIndexRequestDto request, Long userId);

}
