package co.id.bankdki.billerdkilinkrouter.simulator;

import org.jpos.iso.ISODate;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.NACChannel;
import org.jpos.iso.packager.ISO87BPackager;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.space.SpaceUtil;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;

import java.util.Date;

public class SignOn {

    public static Space psp    = SpaceFactory.getSpace("jdbm:myspace");
    public static final String TRACE = "JPTS_TRACE";
    static long traceNumber = SpaceUtil.nextLong(psp, TRACE) % 100000;

    public static void main(String args[]) throws Exception {

        ISOMsg m = new ISOMsg();
        ISO87BPackager p = new ISO87BPackager();
        NACChannel ch = new NACChannel("10.100.111.152",13001,p,null);

        m.setMTI("0800");
        m.set(7, ISODate.getDateTime(new Date()));
        m.set(11, ISOUtil.zeropad(traceNumber, 6));
        m.set(32, "111");
        m.set(70, "001");

//        m.set(70, "001"); SignOn
//        m.set(70, "301"); EchoTest



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