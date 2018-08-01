package co.id.bankdki.billerdkilinkrouter.iso;

import org.jpos.iso.ISODate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.q2.QBeanSupport;
import org.jpos.q2.iso.QMUX;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.space.SpaceUtil;
import org.jpos.util.Log;
import org.jpos.util.NameRegistrar;

import java.util.Date;

/**
 * Created by bankdki on 3/30/15.
 */

public class LogonManager extends QBeanSupport {
    private Space sp = SpaceFactory.getSpace();
    private Space psp;

    private static final String TRACE = "DKILINK_TRACE";
    private static final String LOGON = "DKILINK_LOGON.";
    private static final String ECHO = "DKILINK_ECHO.";
    private long echoInterval = 0;
    private long logonInterval = 0;
    private Boolean logonMode = false;
    private Boolean echoMode = false;
    protected QMUX mux = null;
    protected String readyKey = "";
    protected String logger = "Q2";
    private Thread echoThread;
    private Thread logonThread;
    private Thread muxThread;

    @Override
    protected void startService() {
        psp = SpaceFactory.getSpace(cfg.get("space"));
        echoInterval = cfg.getLong("echo-interval");
        logonInterval = cfg.getLong("logon-interval");
        logonMode = cfg.getBoolean("logon-mode");
        echoMode = cfg.getBoolean("echo-mode");
        logger = cfg.get("logger");
        readyKey = cfg.get("channel-ready");
        getLog().info(readyKey);

        try {
            mux = (QMUX) NameRegistrar.get("mux." + cfg.get("mux"));

            Log.getLog(logger, logger).error("cfg.get(mux) " + mux.getName());
            Log.getLog(logger, logger).error("mux.isConnected 1 " + mux.isConnected());
            if (!mux.isConnected()) {
                mux.start();
                Log.getLog(logger, logger).error("mux.start ");
            }
            Log.getLog(logger, logger).error("mux.isConnected 2 " + mux.isConnected());
        } catch (NameRegistrar.NotFoundException e) {
            Log.getLog(logger, logger).error("NameRegistrar.NotFoundException " + e.toString());
        }


        muxThread = new Thread(new ConnectMux());
        muxThread.start();

        if (logonMode.equals(true)) {
            logonThread = new Thread(new Logon());
            logonThread.start();
        }

        if (echoMode.equals(true)) {
            echoThread = new Thread(new Echo());
            echoThread.start();
        }

    }


    private void doEcho() {
        readyKey = cfg.get("channel-ready");
        ISOMsg resp = null;
        try {
            resp = mux.request(doEchoISOMsg(), cfg.getLong("timeout"));
            if (resp != null) {
                Log.getLog(logger, logger).error(resp.toString());
                NameRegistrar.register(ECHO + readyKey, true);
            } else {
                Log.getLog(logger, logger).error("res echo false");
                NameRegistrar.register(LOGON + readyKey, false);
                doLogon();
            }

        } catch (ISOException e) {
            Log.getLog(logger, logger).error("doEcho " + e);
            NameRegistrar.register(LOGON + readyKey, false);
        }
    }

    private void doLogon() {
        readyKey = cfg.get("channel-ready");
        ISOMsg resp = null;
        try {

            resp = mux.request(doLogonISOMsg(), cfg.getLong("timeout"));
            if (resp != null) {
                Log.getLog(logger, logger).error(resp.toString());
                NameRegistrar.register(LOGON + readyKey, true);
            } else {
                Log.getLog(logger, logger).error("resp logon false");
                NameRegistrar.register(LOGON + readyKey, false);
            }
        } catch (ISOException e) {
            Log.getLog(logger, logger).error("doLogon " + e);
        }
    }

    private ISOMsg doEchoISOMsg() {
        long traceNumber = SpaceUtil.nextLong(psp, TRACE) % 100000;
        ISOMsg m = new ISOMsg();
        try {
            m.setMTI("0800");
            m.set(7, ISODate.getDateTime(new Date()));
            m.set(11, ISOUtil.zeropad(Long.toString(traceNumber), 6));
            m.set(70, "301");
        } catch (ISOException e) {
            Log.getLog(logger, logger).error("doEchoISOMsg " + e);
        }
        return m;
    }

    private ISOMsg doLogonISOMsg() {
        long traceNumber = SpaceUtil.nextLong(psp, TRACE) % 100000;
        ISOMsg m = new ISOMsg();
        try {
            m.setMTI("0800");
            m.set(7, ISODate.getDateTime(new Date()));
            m.set(11, ISOUtil.zeropad(Long.toString(traceNumber), 6));
            m.set(70, "001");
        } catch (ISOException e) {
            Log.getLog(logger, logger).error("doLogonISOMsg " + e);
        }
        return m;
    }


    @SuppressWarnings("unchecked")
    public class Echo implements Runnable {
        public Echo() {
            super();
        }

        public void run() {
            readyKey = cfg.get("channel-ready");
            while (running()) {
                Object sessionId = sp.rd(readyKey, cfg.getLong("timeout"));
                Log.getLog(logger, logger).info("doEcho mux isConnected: " + mux.isConnected());
                Log.getLog(logger, logger).info("sessionId: " + sessionId);

                if (sessionId == null) {
                    Log.getLog(logger, logger).info("Channel Echo " + readyKey + " not ready");
                    NameRegistrar.register(LOGON + readyKey, false);
                    Log.getLog(logger, logger).info("NameRegistrar " + NameRegistrar.getIfExists(LOGON + readyKey));
                    continue;
                }

                Boolean registered = (Boolean) NameRegistrar.getIfExists(LOGON + readyKey);
                Log.getLog(logger, logger).info("registered>>>>"+registered.toString());

                try {
                    if (registered == null) {
                        doLogon();
                    } else if (registered.equals(true)) {
                        doEcho();
                    } else {
                        doLogon();
                    }
                } catch (Throwable t) {
                    NameRegistrar.register(LOGON + readyKey, false);
                    Log.getLog(logger, logger).error("Echo " + t);
                }
                
                ISOUtil.sleep(echoInterval);
            }
        }

    }

    @SuppressWarnings("unchecked")
    public class Logon implements Runnable {
        public Logon() {
            super();
        }

        public void run() {
            readyKey = cfg.get("channel-ready");
            while (running()) {
                Log.getLog(logger, logger).info("doEcho mux isConnected: " + mux.isConnected());
                Log.getLog(logger, logger).info("Channel Logon " + readyKey + " running ready");
                Object sessionId = sp.rd(readyKey, cfg.getLong("timeout"));

                if (sessionId == null) {
                    Log.getLog(logger, logger).info("Channel Logon " + readyKey + " not ready");
                    NameRegistrar.register(LOGON + readyKey, false);
                    continue;
                }

                doLogon();

                ISOUtil.sleep(logonInterval);
            }
        }
    }

    public class ConnectMux implements Runnable {
        public ConnectMux() {
            super();
        }

        public void run() {
            while (running()) {
                if(!mux.isConnected()) {
                    try {
                        mux = (QMUX) NameRegistrar.get("mux." + cfg.get("mux"));
                        Log.getLog(logger, logger).error("mux.isConnected " + mux.isConnected());
                    } catch (NameRegistrar.NotFoundException e) {
                        Log.getLog(logger, logger).error("NameRegistrar.NotFoundException " + e.toString());
                    }

                    ISOUtil.sleep(echoInterval);
                }
            }
        }
    }
}