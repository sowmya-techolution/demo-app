package co.id.bankdki.billerdkilinkrouter.iso;

import co.id.bankdki.billerdkilinkrouter.BillerdkilinkRouterApplication;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.space.SpaceUtil;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

import java.io.Serializable;

/**
  * Created by bankdki on 9/22/16.
  */
 public class Route implements TransactionParticipant, Configurable

 {
     private Configuration cfg;
     private String isorequest;
     private String isosource;
     private String appname;
     private String mux;
     private String logger;
     private Long timeout;
     private BillerdkilinkRouterApplication application = null;

     @Override
     public void setConfiguration(Configuration configuration) throws ConfigurationException {
         this.cfg = configuration;
         this.isorequest = cfg.get("isorequest");
         this.isosource = cfg.get("isosource");
         this.logger = cfg.get("logger");
         this.appname = cfg.get("appname");
         this.mux = cfg.get("mux");
         this.timeout = cfg.getLong("timeout");
     }

     @Override
     public int prepare(long l, Serializable serializable) {
         return PREPARED | READONLY;
     }

     @Override
     public void commit(long l, Serializable serializable) {

         Space psp    = SpaceFactory.getSpace("jdbm:myspace");
         final String TRACES = "JPTS_TRACE";
         long traceNumber = SpaceUtil.nextLong(psp, TRACES) % 100000;

         ISOMsg m = (ISOMsg) ((Context) serializable).get(this.isorequest);
         ISOSource source = (ISOSource) ((Context)serializable).get(this.isosource);

         try {
             m.setResponseMTI();
             m.set(39,"00");
             source.send(m);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     @Override
     public void abort(long l, Serializable serializable) {

     }
 }