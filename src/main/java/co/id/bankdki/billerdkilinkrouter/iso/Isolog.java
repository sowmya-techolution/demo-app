package co.id.bankdki.billerdkilinkrouter.iso;

import co.id.bankdki.billerdkilinkrouter.BillerdkilinkRouterApplication;
import co.id.bankdki.billerdkilinkrouter.domain.History;
import co.id.bankdki.billerdkilinkrouter.repository.HistoryService;
import com.google.gson.Gson;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.NameRegistrar;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
  * Created by bankdki on 9/22/16.
  */
 public class Isolog implements TransactionParticipant, Configurable

 {
     private Configuration cfg;
     private String isorequest;
     private String isosource;
     private String appname;
     private String mux;
     private String logger;
     private Long timeout;
     private String processingcode;
     private String productcode;
     private BillerdkilinkRouterApplication application = null;

     @Override
     public void setConfiguration(Configuration configuration) throws ConfigurationException {
         this.cfg = configuration;
         this.isorequest = cfg.get("isorequest");
         this.isosource = cfg.get("isosource");
         this.logger = cfg.get("logger");
         this.appname = cfg.get("appname");
         this.mux = cfg.get("mux");
         this.productcode = cfg.get("productcode");
         this.processingcode = cfg.get("processingcode");
         this.timeout = cfg.getLong("timeout");
     }

     @Override
     public int prepare(long l, Serializable serializable) {
         return PREPARED | READONLY;
     }

     @Override
     public void commit(long l, Serializable serializable) {
         ISOMsg m = (ISOMsg) ((Context) serializable).get(this.isorequest);
         HistoryService historyService = (HistoryService) NameRegistrar.getIfExists("historyImpl");
         History dto = new History();
         Gson gson = new Gson();
         Map<String, Object> map = new HashMap<>();
         for(int i=0;i<=m.getMaxField();i++){
             if(m.hasField(i)){
                 map.put(String.valueOf(i),m.getString(i));
             }
         }

         dto.setDate(new Date());
         dto.setData(gson.toJson(map));
         dto.setRekon(m.getString(60));
//         Log.getLog("Q2","Q2").info(gson.toJson(map));
//         Log.getLog("Q2","Q2").info(dto);
         historyService.save(dto);

     }

     @Override
     public void abort(long l, Serializable serializable) {

     }
 }