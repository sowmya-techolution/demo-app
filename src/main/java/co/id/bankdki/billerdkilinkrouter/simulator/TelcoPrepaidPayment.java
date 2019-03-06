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

public class TelcoPrepaidPayment {

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
        NACChannel ch = new NACChannel("10.100.111.152",13001,p,null);
//        ch.setHeader("0000000000".getBytes());
        m.setMTI("0200");
//        m.set(2, ISOUtil.zeropad("0", 16));
        m.set(2, "504948250000000072");
        m.set(3, "501000"); // inquiry or payment
        m.set(4, "000000050000"); //ganti amount
        m.set(7, ISODate.getDateTime(new Date()));
        m.set(11, ISOUtil.zeropad(traceNumber, 6));
        m.set(12, ISODate.getTime(new Date()));
        m.set(13, ISODate.getDate(new Date()));
        m.set(14, sdf.format(new Date()));
//      m.set(15, ISODate.getDate(new Date()));
        m.set(15, sdfDate2.format(satuharilagi));
        m.set(18, "6014");
        m.set(32, "111");
        m.set(33, "111");
        m.set(37, ISOUtil.zeropad(traceNumber+1, 12));
        m.set(41, "JOMI");
        m.set(42, "000000000000900");
        m.set(43, "ATM DEV 01                           IDN");

        m.set(48,"01081122272001||||"); // TELKOMSEL SIMPATI PREPAID
//        m.set(48,"01085299989001||||"); // TELKOMSEL AS PREPAID
//        m.set(48,"01089688025124||||"); // THREE PREPAID
//        m.set(48,"010881999999001||||"); // SMARTFREN PREPAID
//        m.set(48,"01083812560355||||"); // XL PREPAID
//        m.set(48,"01081193999001||||"); // PULSA INTERNET SIMPATI PREPAID
//        m.set(48,"01085299999001||||"); // PULSA INTERNET AS PREPAID
//        m.set(48,"010818223340001||||"); // PULSA INTERNET XL PREPAID

        m.set(49,"360");

        m.set(59,"");

        m.set(62,"2018");
        m.set(63,"20000|1500/25000|1500/50000|1500/100000|1500/150000|1500/200000|1500/300000|1500/500000|1500/1000000|2500");

        m.set(98,"100007"); // TELKOMSEL PREPAID
//        m.set(98,"100003"); // THREE PREPAID
//        m.set(98,"100005"); // SMARTFREN PREPAID
//        m.set(98,"100001"); // XL PREPAID
//        m.set(98,"100009"); // PULSA INTERNET TSEL PREPAID
//        m.set(98,"100014"); // PULSA INTERNET XL PREPAID


        m.set(105,"91192254228");
        m.set(106,"91194423264");
        m.set(107,"10194423272");


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
