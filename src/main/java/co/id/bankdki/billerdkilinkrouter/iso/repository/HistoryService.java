package co.id.bankdki.billerdkilinkrouter.iso.repository;

import co.id.bankdki.billerdkilinkrouter.domain.History;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by bankdki on 8/2/17.
 */
@Component
public interface HistoryService {
    void save(History resp);
    List<History> getDataRekon(String tgltrx, String kodebiller);
    List<String> getGroupKodebiller();
}


