package co.id.bankdki.billerdkilinkrouter.simulator;

import co.id.bankdki.billerdkilinkrouter.iso.ISO87BPackager;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.NACChannel;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.space.SpaceUtil;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Reversal {

    public static Space psp    = SpaceFactory.getSpace("jdbm:myspace");
    public static final String TRACE = "JPTS_TRACE";
    static long traceNumber = SpaceUtil.nextLong(psp, TRACE) % 100000;

    public static void main(String args[]) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("YYMM");

        SimpleDateFormat sdfDate2 = new SimpleDateFormat("MMdd");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date satuharilagi = cal.getTime(); //membuat variabel yang bertipe Date yang menyimpan 3 hari setelah hari ini


        ISOMsg m = new ISOMsg();
//        ISO87APackager p = new ISO87APackager();
//        ASCIIChannel ch = new ASCIIChannel("10.100.111.152",13000, null);
        ISO87BPackager p = new ISO87BPackager();
        NACChannel ch = new NACChannel("10.100.111.152",7272,p,null);
//        ch.setHeader("0000000000".getBytes());


        m.setMTI("0400");
        m.set(2, ISOUtil.zeropad("0", 18));
        m.set(3, "501000"); // inquiry or payment
        m.set(4, "000000050000"); //ganti amount
        m.set(7, ISODate.getDateTime(new Date()));
        m.set(11, ISOUtil.zeropad(traceNumber, 6));
        m.set(12, ISODate.getTime(new Date()));
        m.set(13, ISODate.getDate(new Date()));
//      m.set(14, sdf.format(new Date()));
//      m.set(15, ISODate.getDate(new Date()));
        m.set(15, sdfDate2.format(satuharilagi));
        m.set(27, "1");
        m.set(32, "111");
//      m.set(33, "111");
        m.set(37, ISOUtil.zeropad(traceNumber+1, 12));
        m.set(38, "123456");
//      m.set(41, "JOMI");
//      m.set(42, "000000000000900");
        m.set(43, "ATM DEV 01                           IDN");
        m.set(49,"360");
//        m.set(57,"");

//        m.set(60,"090");

        m.set(59, "");

        m.set(90, "02000000289000000026790802105833111        111        JOMI    ");
        // MTI|STAN|REFERENCE NUMBER|TRANSMISSION DATE|ACQ ID|FWD ID|TERMINAL ID

        //JOMI KODE PRODUK TRXMANAGER SELECTOR
          m.set(98,"100007"); //SMARTFREN POSTPAID
//        m.set(98,"100002"); //XL POSTPAID
//        m.set(98,"100006"); //SMARTFREN POSTPAID
//        m.set(98,"100004"); //THREE POSTPAID
//        m.set(98,"100008"); //HALO POSTPAID
//        m.set(98,"100009"); //PULSA INTERNET TELKOMSEL PREPAID
//        m.set(98,"100012"); //JASTEL
//        m.set(98,"100010"); //KAI
//        m.set(98,"100015"); //CITILINK
//          m.set(98,"100013"); //GARUDA
//        m.set(98,"100010"); //INDOVISION
//        m.set(98,"100056"); //VA






        m.setPackager(p);
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener(System.out));
        p.setLogger(logger, "pkgr");

        ch.setPackager(p);
        ch.setLogger(logger, "channel");

        ch.connect();
        ch.send(m);

        ISOMsg r = ch.receive();
//        System.out.println(r.getBytes());
        ch.disconnect();



    }

}