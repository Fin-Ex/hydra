package ru.finex.ws.l2.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.Query;
import ru.finex.ws.l2.model.entity.ClanEntity;

import java.util.List;

/**
 * @author m0nster.mind
 */
@Valid
public interface ClanRepository extends CrudRepository<ClanEntity, Integer> {

    @Query("SELECT clan FROM ClanEntity clan WHERE clan.allianceId = :allianceId")
    List<ClanEntity> findInAlliance(@NotNull Integer allianceId);

}
