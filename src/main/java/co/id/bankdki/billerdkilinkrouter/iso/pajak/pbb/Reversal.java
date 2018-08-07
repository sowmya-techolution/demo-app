package co.id.bankdki.billerdkilinkrouter.iso.pajak.pbb;

import co.id.bankdki.billerdkilinkrouter.BillerdkilinkRouterApplication;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.q2.iso.QMUX;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.space.SpaceUtil;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;
import org.jpos.util.NameRegistrar;

import java.io.Serializable;

/**
  * Created by bankdki on 9/22/16.
  */
 public class Reversal implements TransactionParticipant, Configurable

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

         Space psp    = SpaceFactory.getSpace("jdbm:myspace");
         final String TRACES = "JPTS_TRACE";
         long traceNumber = SpaceUtil.nextLong(psp, TRACES) % 100000;

         ISOMsg m = (ISOMsg) ((Context) serializable).get(this.isorequest);
         ISOSource source = (ISOSource) ((Context)serializable).get(this.isosource);

         try {
             Log.getLog(this.logger,this.logger).info(m.getValue());

             ISOMsg f = new ISOMsg();

             f.setMTI("0400");
             f.set(2,m.getString(2));
             f.set(3,m.getString(3));
             f.set(4,m.getString(4));
             f.set(7,m.getString(7));
             f.set(11,m.getString(11));
             f.set(12,m.getString(12));
             f.set(13,m.getString(13));
             f.set(15,m.getString(15));
             f.set(27,m.getString(27));
             f.set(32,m.getString(32));
             f.set(37,m.getString(37));
             f.set(38,m.getString(38));
             f.set(43,m.getString(43));
             f.set(49,m.getString(49));
             f.set(59,m.getString(59));
             f.set(90,m.getString(90));
             f.set(98,m.getString(98));
             f.set(105,m.getString(105));
             f.set(106,m.getString(106));
             f.set(107,m.getString(107));

             QMUX qmux = (QMUX) NameRegistrar.get("mux." + this.mux);
             ISOMsg resp = qmux.request(f, 10000);

             if(resp != null){
                 switch (resp.getString(39)) {
                 case "00":
                 m.set(59,resp.getString(59));
                 m.set(61,resp.getString(61));

                 break;

                 }
                 m.set(39,resp.getString(39));
                 m.set(59,resp.getString(59));
                 m.set(61,resp.getString(61));

             } else {
                 m.set(39,"68");
                 m.set(61,"Transaction TimeOut");

             }
             m.setResponseMTI();
             source.send(m);

             Log.getLog(this.logger,this.logger).info(m.getValue());
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     @Override
     public void abort(long l, Serializable serializable) {

     }
 }