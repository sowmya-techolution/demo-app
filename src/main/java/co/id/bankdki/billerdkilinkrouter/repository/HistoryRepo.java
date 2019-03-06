package co.id.bankdki.billerdkilinkrouter.repository;

import co.id.bankdki.billerdkilinkrouter.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bankdki on 8/2/17.
 */
@Repository
public interface HistoryRepo extends JpaRepository<History, Long> {

    @Transactional
    @Query(value = "select *\n" +
                 "from history where \n" +
                 "replace(cast(data\\:\\:json->'0' as text),'\"','') = '0210' \n" +
                 "and \n" +
                 "replace(cast(data\\:\\:json->'39' as text),'\"','') = '00'\n" +
                 "and \n" +
                 "replace(cast(data\\:\\:json->'3' as text),'\"','') = '501000'\n" +
                 "and \n" +
                 "to_char(date,'YYYY-MM-DD')=?1\n" +
                 "and\n" +
                 "trim(replace(cast(data\\:\\:json->'103' as text),'\"','')) = ?2", nativeQuery = true)
    List<History> getDataRekon(String tgltrx, String kodebiller);

    @Transactional
    @Query(value = "select distinct trim(replace(cast(data\\:\\:json->'103' as text),'\"','')) from history",nativeQuery = true)
    List<String> getGroupKodebiller();
}
