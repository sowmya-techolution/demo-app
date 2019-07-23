package co.id.bankdki.billerdkilinkrouter.iso;

import java.util.HashMap;
import java.util.Map;

/**
 * co.id.bankdki.serveredc
 * Created by galih.lasahido@gmail.com on 9/21/17.
 * server-edc
 */
public class ResCode {

    public static String getRC(String rc){
        Map<String, String> map = new HashMap<>();
        map.put("00", "Approve. Transaction is succeed.");
        map.put("05", "Refer to Card Issuer");
        map.put("06", "Error. The transaction is declined.");
        map.put("09", "Id Pelanggan Terblokir/ Customer Id Blocked");
        map.put("10", "Phone number not exist / Billing ID not exist");
        map.put("12", "Invalid Transaction.");
        map.put("13", "Melebihi limit trx");
        map.put("14", "Invalid Card Number");
        map.put("20", "Invalid Response");
        map.put("25", "Unable to locate record on file.");
        map.put("26", "Melebihi limit trx");
        map.put("30", "Format Error");
        map.put("31", "Bank Not Supported");
        map.put("33", "Expired Card");
        map.put("36", "Restricted Card");
        map.put("38", "Exceeded PIN Tries");
        map.put("40", "Function Not Supported");
        map.put("41", "Lost Card");
        map.put("51", "Saldo Tidak Cukup");
        map.put("52", "No Checking Account");
        map.put("53", "No Saving Account");
        map.put("55", "PIN Salah");
        map.put("57", "Rekening Dormant");
        map.put("58", "Transaction NOT Permitted to Terminal");
        map.put("61", "Exceeds Withdrawal Amount Limit");
        map.put("62", "Kartu Tidak Aktif");
        map.put("63", "Reversal Data Original Not Found");
        map.put("65", "Exceeds Withdrawal Frequency");
        map.put("68", "Response Received too late");
        map.put("75", "Pin telah Terblokir");
        map.put("77", "Exceeded Bill Limit");
        map.put("78", "Rekening ditutup");
        map.put("79", "Special Number");
        map.put("80", "Invalid Reference Number");
        map.put("81", "Old Bill Exist");
        map.put("83", "Blokir");
        map.put("87", "Bill Not Ready");
        map.put("88", "Bill Already Paid");
        map.put("89", "Invalid Settlement Date");
        map.put("90", "Cut Off Is In Progress");
        map.put("91", "Issuer or Switch Inoperative");
        map.put("92", "Cannot route");
        map.put("93", "Transaction Already Reversed");
        map.put("96", "System Malfunction");
        map.put("97", "Bukan Peserta KJP");
        map.put("99", "Failed");
        map.put(""  , "Please Update EDC System");
        map.put("F1", "Unregistered Issuer");
        map.put("F2", "List Issuer Bank Empty");
        map.put("F3", "");
        map.put("E5", "SSRD tidak terdaftar");
        map.put("E7", "SSRD 10 digit");
        map.put("E8", "SSRD sudah terbayar");
        map.put("S0", "Susu hanya utk KJP");
        map.put("S4", "Subsidi sdh diambil");
        map.put("S3", "Paket tdk tersedia");
        map.put("S2", "TID blm terkait");
        map.put("S5", "Kombinasi Salah");
        map.put("S6", "Rek tdk terdaftar");
        map.put("S8", "Bkn Penerima Subsidi");
        return map.get(rc);
    }
}
