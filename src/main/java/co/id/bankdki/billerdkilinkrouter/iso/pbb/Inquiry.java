package co.id.bankdki.billerdkilinkrouter.iso.pbb;

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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
  * Created by bankdki on 9/22/16.
  */
 public class Inquiry implements TransactionParticipant, Configurable

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

         DecimalFormat df = new DecimalFormat("#,###");
         DecimalFormatSymbols dfs = new DecimalFormatSymbols();
         dfs.setCurrencySymbol("");
         df.setDecimalFormatSymbols(dfs);

         try {
             Log.getLog(this.logger,this.logger).info(m.getValue());

             ISOMsg b = new ISOMsg();
             b.setMTI("0200");
             b.set(2,m.getString(2));
             b.set(3,m.getString(3));
             b.set(4,m.getString(4));
             b.set(7,m.getString(7));
             b.set(11,m.getString(11));
             b.set(12,m.getString(12));
             b.set(13,m.getString(13));
             b.set(14,m.getString(14));
             b.set(15,m.getString(15));
             b.set(18,m.getString(18));
             b.set(32,m.getString(32));
             b.set(33,m.getString(33));
             b.set(37,m.getString(37));
             b.set(41,m.getString(41));
             b.set(42,m.getString(42));
             b.set(43,m.getString(43));
             b.set(48,m.getString(48));
             b.set(49,m.getString(49));
             b.set(62,m.getString(62));
             b.set(98,m.getString(98));


             QMUX qmux = (QMUX) NameRegistrar.get("mux." + this.mux);
             ISOMsg resp = qmux.request(b, this.timeout);

             if(resp != null){
                 if(resp.getString(39).equalsIgnoreCase("00")){
                     m.set(4,resp.getString(4));
                     m.set(48,resp.getString(48));
                     m.set(59,resp.getString(59));
                     m.set(61,resp.getString(61));
                     m.set(63,resp.getString(63));
                     m.set(105,resp.getString(105));
                     m.set(106,resp.getString(106));
                 }
                 m.set(39,resp.getString(39));
                 m.set(61,resp.getString(61));
                 m.set(63,resp.getString(63));
                 m.set(105,resp.getString(105));
                 m.set(106,resp.getString(106));

                 m.setResponseMTI();
                 source.send(m);
             }else {
                 m.set(39,"68");
                 m.set(61,"Transaction TimeOut");

                 m.setResponseMTI();
                 source.send(m);
             }

             Log.getLog(this.logger,this.logger).info(m.getValue());
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     @Override
     public void abort(long l, Serializable serializable) {

     }
 }