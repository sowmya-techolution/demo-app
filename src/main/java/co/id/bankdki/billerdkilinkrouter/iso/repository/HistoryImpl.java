package co.id.bankdki.billerdkilinkrouter.iso.repository;

import co.id.bankdki.billerdkilinkrouter.domain.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bankdki on 8/2/17.
 */
@Service
public class HistoryImpl implements HistoryService {

    @Autowired(required = false)
    private HistoryRepo service;

    @Override
    public void save(History resp) {
        service.save(resp);
    }

    @Override
    public List<History> getDataRekon(String tgltrx, String kodebiller) {
        return service.getDataRekon(tgltrx, kodebiller);
    }

    @Override
    public List<String> getGroupKodebiller() {
        return service.getGroupKodebiller();
    }

}
